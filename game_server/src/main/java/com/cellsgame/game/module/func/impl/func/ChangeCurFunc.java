package com.cellsgame.game.module.func.impl.func;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.func.*;
import com.cellsgame.game.module.func.cons.CheckRecType;
import com.cellsgame.game.module.func.cons.IRecCheckerType;
import com.cellsgame.game.module.func.cons.PrizeConstant;
import com.cellsgame.game.module.func.formula.FormulaType;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecCur;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.google.common.collect.Table;

/**
 * User: 阚庆忠
 * Date: 2016/9/23 16:09
 * Desc:
 */
public class ChangeCurFunc extends SyncFunc {

    @Resource
    private DepotBO depotBO;

    @Override
    public Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        long changeVal = 0;
        FormulaType formulaType = Enums.get(FormulaType.class, param.getParam2());
        if(formulaType != null) {
            changeVal = changeVal + formulaType.getFormula().getValue(player);
        }
        if(param.getParam3() != 0){
            changeVal = (int)(changeVal * (long)param.getParam3() / 10000L);
        }
        changeVal += param.getValue();
        changeVal = changeVal * execNum;
        CurrencyType type = Enums.get(CurrencyType.class, param.getParam());
        if (type != null && type != CurrencyType.NULL) {
            parent = depotBO.changeCurByType(parent, player, type, changeVal, true, param.isOverMax(), cmd, param.getExtra(EvtParamType.PAY, false));
        }
        if (changeVal > 0 && prizeMap != null) {
            PrizeConstant.addCur(prizeMap, param.getParam(), changeVal);
        }
        return parent;
    }

    @Override
    public String checkCfg(FuncParam config, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        CurrencyType type = Enums.get(CurrencyType.class, config.getParam());
        if (type == null || type == CurrencyType.NULL)
            return "--FuncConfig CnyType Error is null>>>> " + JSONUtils.toJSONString(config);
        return null;
    }

    @Override
    public Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) {
        if (param.getValue() < 0) {
            List<CheckRec<?>> ret = GameUtil.createList();
            CheckRecCur curRec = CheckRecType.Cur.getCheckRec();
            CurrencyType cType = Enums.get(CurrencyType.class, param.getParam());
            
            FormulaType formulaType = Enums.get(FormulaType.class, param.getParam2());
            long changeVal = param.getValue();
            if(formulaType != null) {
                changeVal = formulaType.getFormula().getValue(player);
            }
            if(param.getParam3() > 0){
                changeVal = (int)((long)changeVal * (long)param.getParam3() / 10000L);
            }
           
            curRec.addCost(cType, changeVal);
            ret.add(curRec);
            return ret;
        }
        return null;
    }


    @Override
    public IChecker getParamChecker() {
        return IRecCheckerType.CurCost.getChecker();
    }


}
