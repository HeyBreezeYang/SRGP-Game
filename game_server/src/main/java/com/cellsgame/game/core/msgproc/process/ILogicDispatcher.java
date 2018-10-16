package com.cellsgame.game.core.msgproc.process;

import java.util.Map;

import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.GameMessage;

/**
 * @author Aly on  2016-07-12.
 */
public interface ILogicDispatcher {
    int getModuleID();

    Object[] getParam(int subMethod, Map<?, ?> params) throws LogicException;

    Map<?, ?> dispatchLogic(MessageController controller, int cmd, Object[] param, GameMessage message) throws LogicException;

}
