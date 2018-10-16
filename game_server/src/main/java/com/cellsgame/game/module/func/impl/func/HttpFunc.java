package com.cellsgame.game.module.func.impl.func;

import java.util.Collection;
import java.util.Map;

import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.func.AsyncFunc;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * File Description.
 *
 * @author Yang
 */
public abstract class HttpFunc extends AsyncFunc {

    @Override
    protected Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) throws LogicException {
        return null;
    }

    @Override
    public DispatchType getExecDisruptor() {
        return DispatchType.HTTP;
    }

    @Override
    public DispatchType getEndDisruptor() {
        return DispatchType.GAME;
    }

}
