package com.cellsgame.game.cons;

import com.cellsgame.game.core.excption.LogicException;

public interface ICode {

    int getModule();

    int getCode();

    default void throwIfTrue(boolean IF) throws LogicException {
        if (IF) throw new LogicException(get());
    }

    default void throwException() throws LogicException {
        throw new LogicException(get());
    }

    default int get() {
        return getModule() + getCode();
    }
}
