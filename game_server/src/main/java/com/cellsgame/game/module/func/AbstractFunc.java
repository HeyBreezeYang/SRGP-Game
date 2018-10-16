package com.cellsgame.game.module.func;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;


public abstract class AbstractFunc implements Cloneable {
    private static final Logger log = LoggerFactory.getLogger(AbstractFunc.class);
    private int rate;

    private int steadyExecId;

    private FuncParam param;

    public IChecker getParamChecker() {
        return null;
    }

    public IChecker getDepotCapacityChecker() {
        return null;
    }

    public Object checkAndExec(Map<?, ?> parent, CMD cmd, PlayerVO player) throws LogicException {
        return checkAndExec(parent, null, cmd, player, param);
    }

    public Object checkAndExec(Map<?, ?> parent, Map prizeMap, CMD cmd, PlayerVO player) throws LogicException {
        return checkAndExec(parent, prizeMap, cmd, player, param);
    }

    public Object checkAndExec(Map<?, ?> parent, CMD cmd, PlayerVO player, FuncParam param) throws LogicException {
        return checkAndExec(parent, null, cmd, player, param);
    }

    public Object checkAndExec(Map<?, ?> parent, Map prizeMap, CMD cmd, PlayerVO player, FuncParam param) throws LogicException {
        IChecker paramChecker = getParamChecker();
        if (paramChecker != null)
            paramChecker.check(player, param);
        recAndCheck(player, param);
        return exec(parent, prizeMap, cmd, player, param, 1);
    }

    private void recAndCheck(PlayerVO player, FuncParam param) throws LogicException {
        Collection<CheckRec<?>> recs = record(player, param);
        if (recs != null) {
            Map<Class<? extends CheckRec>, CheckRec<?>> checkRec = GameUtil.createSimpleMap();
            groupRecord(recs, checkRec);

            if (!checkRec.isEmpty()) {
                Collection<CheckRec<?>> finalRecs = checkRec.values();
                for (CheckRec rec : finalRecs) {
                    rec.check(player);
                }
            }
        }
    }

    void groupRecord(Collection<CheckRec<?>> recs, Map<Class<? extends CheckRec>, CheckRec<?>> checkRec) throws LogicException {
        if (recs == null) return;
        for (CheckRec<?> rec : recs) {
            if (rec == null)
                continue;
            Class<? extends CheckRec> recClass = rec.getClass();
            CheckRec oldRec = checkRec.get(recClass);
            if (oldRec != null)
                //noinspection unchecked
                oldRec.accept(rec);
            else
                checkRec.put(recClass, rec);
        }
    }

    Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player,int execNum) throws LogicException {
        return exec(parent, prizeMap, cmd, player, param, execNum);
    }

    protected abstract Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) throws LogicException;

    protected abstract Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException;

    public String checkCfg(FuncParam config, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        return "";
    }

    public FuncParam getParam() {
        return param;
    }

    public AbstractFunc setParam(FuncParam param) {
        this.param = param;
        return this;
    }

    public AbstractFunc clone() {
        try {
            return (AbstractFunc) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("", e);
        }
        return null;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getSteadyExecId() {
        return steadyExecId;
    }

    public void setSteadyExecId(int steadyExecId) {
        this.steadyExecId = steadyExecId;
    }


}
