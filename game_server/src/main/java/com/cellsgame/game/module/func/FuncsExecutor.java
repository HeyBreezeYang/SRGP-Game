package com.cellsgame.game.module.func;

import java.util.*;
import java.util.function.Function;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.event.EvtParam;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.func.impl.exec.BaseFuncsExecutor;
import com.cellsgame.game.module.func.vo.FixedDropVO;
import com.cellsgame.game.module.player.csv.FixedExecConfig;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;

public abstract class FuncsExecutor<T extends FuncsExecutor<T>> {
    private static BaseDAO<FixedDropVO> fixedDropDAO;// = SpringBeanFactory.getBean("fixedDropDAO");

    protected List<AbstractFunc> allFuncs = GameUtil.createList();            // 裸配置项

    protected Collection<AbstractFunc> selectedFuncs;
    private Map<Class<? extends CheckRec>, CheckRec<?>> checkRec = GameUtil.createSimpleMap();

    private boolean firstSelect = true;
    // 请求发的CMD
    private CMD cmd;
    private Function function;

    public void setFunction(Function function) {
        this.function = function;
    }

    /**
     * 获取 有执行逻辑的结果输出
     */
    public abstract Collection<AbstractFunc> getFuncs(Object ... params);

    /**
     * 获取 未参加执行逻辑的 裸配置项
     */
    public Collection<AbstractFunc> getAllFuncs() {
        return allFuncs;
    }

    public final T copy() {
        T t = innerCopy();
        t.setCmd(cmd);
        return t;
    }

    protected abstract T innerCopy();

    public void setExecutorParam(int param) {
    }

    public void addExecutor(FuncsExecutor<?> exec, Object ... inputParams) {
        if (exec == this)
            return;
        addFuncs(exec.getFuncs(inputParams));
    }

    protected void whenAcceptFunc(AbstractFunc func) {
    }

    public final FuncsExecutor acceptFunc(AbstractFunc func) {
        allFuncs.add(func);
        whenAcceptFunc(func);
        return this;
    }

    public final FuncsExecutor addFuncs(Collection<AbstractFunc> funcs) {
        for (AbstractFunc func : funcs) {
            acceptFunc(func);
        }
        return this;
    }

    public final FuncsExecutor addSyncFunc(SyncFuncType funcType, int param, long value) {
        acceptFunc(funcType.getFunc().setParam(new FuncParam(param, value)));
        return this;
    }

    public final FuncsExecutor addSyncFunc(FuncConfig funcConfig, int quantity) {
        FuncConfig config = funcConfig.copy();
        config.setValue(config.getValue() * quantity);
        SyncFuncType funcType = Enums.get(SyncFuncType.class, funcConfig.getType());
        SyncFunc func = funcType.getFunc();
        func.setParam(config);
        acceptFunc(func);
        return this;
    }


    public final FuncsExecutor addSyncFunc(FuncConfig funcConfig) {
        return addSyncFunc(funcConfig, null);
    }

    public final FuncsExecutor addSyncFunc(Collection<FuncConfig> funcConfigs) {
        return addSyncFunc(funcConfigs, null);
    }


    public final FuncsExecutor addSyncFunc(FuncConfig funcConfig, EvtParam<T> param) {
        SyncFuncType funcType = Enums.get(SyncFuncType.class, funcConfig.getType());
        SyncFunc func = funcType.getFunc();
        funcConfig = funcConfig.copy();
        if(param != null) {
            funcConfig.addExtra(param.getType(), param.getVal());
        }
        func.setParam(funcConfig);
        acceptFunc(func);
        return this;
    }

    public final FuncsExecutor addSyncFunc(Collection<FuncConfig> funcConfigs, EvtParam<T> param) {
        if (null != funcConfigs)
            for (FuncConfig funcConfig : funcConfigs) {
                addSyncFunc(funcConfig, param);
            }
        return this;
    }

    /**
     * 功能执行器执行时会优先检查所有待检查项目能否正确执行, 之后再集中执行, 一旦检查失败将抛出异常,终止执行
     */
    public final void exec(Map parent, PlayerVO pvo) throws LogicException {
        exec(parent, null, pvo, true, 1);
    }

    /**
     * 功能执行器执行时会优先检查所有待检查项目能否正确执行, 之后再集中执行, 一旦检查失败将抛出异常,终止执行
     */
    public final void exec(Map parent, PlayerVO pvo, boolean checkDepot) throws LogicException {
        exec(parent, null, pvo, checkDepot, 1);
    }

    /**
     * 功能执行器执行时会优先检查所有待检查项目能否正确执行, 之后再集中执行, 一旦检查失败将抛出异常,终止执行
     */
    public final void exec(Map parent, Map prizeMap, PlayerVO pvo) throws LogicException {
        exec(parent, prizeMap, pvo, true, 1);
    }

    /**
     * 功能执行器执行时会优先检查所有待检查项目能否正确执行, 之后再集中执行, 一旦检查失败将抛出异常,终止执行
     */
    public final void exec(Map parent, Map prizeMap, PlayerVO pvo, boolean checkDepot, int execNum, Object ... inputParams) throws LogicException {
        if (checkDepot)
            doDepotChecker(pvo);
        selectAndCheck(pvo, inputParams);
        runSelectedFuncs(parent, prizeMap, pvo, execNum);
    }

    public final void selectAndCheck(PlayerVO pvo,  Object ... inputParams) throws LogicException {
        selectAndRec(pvo, inputParams);
        reCheckRecs(pvo);
    }

    public final void doDepotChecker(PlayerVO playerVO) throws LogicException {
        for (AbstractFunc func : allFuncs) {
            IChecker checker = func.getDepotCapacityChecker();
            if (checker != null)
                checker.check(playerVO, func.getParam());
        }
    }

    public final void runSelectedFuncs(Map parent, Map prizeMap, PlayerVO pvo, int execNum) throws LogicException {
        for (AbstractFunc func : selectedFuncs) {
            func.exec(parent, prizeMap, cmd, pvo, execNum);
        }
        if(function != null) function.apply(selectedFuncs);
    }

    private List<AbstractFunc> select(PlayerVO pvo, Object ... params) throws LogicException {
        if (isSupportManySelect() || firstSelect) {
            List<AbstractFunc> select = fix(pvo, getFuncs(params));
            firstSelect = false;
            addSelectFunc(select);
            return select;
        } else
            CodeGeneral.General_UnSupportOperation.throwException();
        return Collections.emptyList();
    }

    /**
     * 是否支持反复select
     * 反复select会造成 掉率异常
     * 目前只有 {@link BaseFuncsExecutor} 支持此操作
     */
    protected boolean isSupportManySelect() {
        return false;
    }

    protected void addSelectFunc(List<AbstractFunc> select) {
        selectedFuncs = select;

    }

    public final Collection<CheckRec<?>> getCheckRec() {
        return checkRec.values();
    }

    public void selectAndRec(PlayerVO pvo, Object ... params) throws LogicException {
        checkAndRec(pvo, select(pvo, params));
    }

    private List<AbstractFunc> fix(PlayerVO pvo, Collection<AbstractFunc> funcs) {
        FixedDropVO fdvo = pvo.getFixedDropVO();
        Map<Integer, Integer> counts = fdvo.getTriggerCountMap();
        Map<Integer, Integer> execs = fdvo.getDropCountMap();
        boolean flag = false;
        ArrayList<AbstractFunc> ret = new ArrayList<>();
        for (AbstractFunc func : funcs) {
            int fixedId = func.getSteadyExecId();
            FixedExecConfig cfg = CacheConfig.getCfg(FixedExecConfig.class, fixedId);
            if (cfg == null) {
                ret.add(func);
                continue;
            }
            flag = true;
            int total = cfg.getTotal();
            Integer count = counts.get(fixedId);
            if (count == null)
                count = 0;
            Integer exec = execs.get(fixedId);
            if (exec == null)
                exec = 0;
            boolean finalCount = count + 1 >= total;
            counts.put(fixedId, count + 1);
            if (checkFix(total, cfg.getFixed(), count, exec)) {
                execs.put(fixedId, exec + 1);
                ret.add(func);
            }
            if (finalCount) {
                execs.put(fixedId, 0);
                counts.put(fixedId, 0);
            }
        }
        if (flag) {
            //
            if (fixedDropDAO == null) fixedDropDAO = SpringBeanFactory.getBean("fixedDropDAO");
            fixedDropDAO.save(fdvo); //保存触发次数
        }
        return ret;
    }


    private void reCheckRecs(PlayerVO pvo) throws LogicException {
        if (!checkRec.isEmpty()) {
            Collection<CheckRec<?>> recs = checkRec.values();
            for (CheckRec rec : recs) {
                rec.check(pvo);
            }
        }
    }


    @SuppressWarnings("unchecked")
    private void checkAndRec(PlayerVO pvo, Collection<AbstractFunc> selectedFuncs) throws LogicException {
        if (null == selectedFuncs || selectedFuncs.size() == 0) return;

        for (AbstractFunc func : selectedFuncs) {
            IChecker checker = func.getParamChecker();
            if (checker != null)
                checker.check(pvo, func.getParam());
        }
        for (AbstractFunc func : selectedFuncs) {
            FuncParam param = func.getParam();
            Collection<CheckRec<?>> record = func.record(pvo, param);
            func.groupRecord(record, checkRec);
        }
    }


    private boolean checkFix(int cfgCount, int cfgDrop, int count, int drop) {
        if (drop < cfgDrop) {
            if (cfgDrop - drop >= cfgCount - count)
                return true;
            int dropRate = (cfgDrop * 100 / cfgCount);
            int ran = GameUtil.r.nextInt(100);
            return dropRate > ran;
        }
        return false;
    }

    public CMD getCmd() {
        return cmd;
    }

    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }
}
