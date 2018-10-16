package com.cellsgame.game.core.event;

/**
 * @author Aly on  2016-10-26.
 */
public class EvtParam<T> {
    EvtParamType<T> type;
    T val;

    EvtParam(EvtParamType<T> type, T val) {
        this.type = type;
        this.val = val;
    }

    public EvtParamType<T> getType() {
        return type;
    }

    public T getVal() {
        return val;
    }
}
