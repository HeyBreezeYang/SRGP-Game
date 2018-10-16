package com.cellsgame.game.module.func.impl.func;

import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.func.SyncFunc;
import com.cellsgame.game.module.func.cons.ICheckerType;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cons.FuncTimes;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

public class ChangeFuncTimesFunc extends SyncFunc {

    @Resource
    private PlayerBO playerBO;

    public void setPlayerBO(PlayerBO playerBO) {
        this.playerBO = playerBO;
    }

    @Override
    protected Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) throws LogicException {
        return null;
    }

    @Override
    protected Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        int funcTimeId = param.getParam();
        int value = (int)param.getValue();
        playerBO.changeFuncTimes(parent, player, Enums.get(FuncTimes.class, funcTimeId), value);
        return parent;
    }

    @Override
    public IChecker getParamChecker() {
        return ICheckerType.FuncTimes.getChecker();
    }
}
