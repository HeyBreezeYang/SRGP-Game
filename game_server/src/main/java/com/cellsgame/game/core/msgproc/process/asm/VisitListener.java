package com.cellsgame.game.core.msgproc.process.asm;

import com.cellsgame.game.core.msgproc.process.ILogicDispatcher;

public interface VisitListener {
    void onEnd(Class<?> boInterface, ILogicDispatcher dispatcher);
}