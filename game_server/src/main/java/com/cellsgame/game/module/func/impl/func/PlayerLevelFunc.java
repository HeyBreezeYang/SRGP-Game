package com.cellsgame.game.module.func.impl.func;

import java.util.Collection;
import java.util.Map;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.func.SyncFunc;
import com.cellsgame.game.module.func.cons.ICheckerType;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author Yang.
 */
public class PlayerLevelFunc extends SyncFunc {

    @Override
    protected Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) throws LogicException {
        return null;
    }

    @Override
    protected Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        //
        return parent;
    }

    @Override
    public IChecker getParamChecker() {
        return ICheckerType.PlayerLevel.getChecker();
    }
}
