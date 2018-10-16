package com.cellsgame.game.util;

public class VolatileLongRef {
    private volatile long elem;

    private VolatileLongRef(long elem) {
        this.elem = elem;
    }

    public static VolatileLongRef create(long e) {
        return new VolatileLongRef(e);
    }

    public static VolatileLongRef zero() {
        return new VolatileLongRef(0);
    }

    public long getElem() {
        return elem;
    }

    public void setElem(long elem) {
        this.elem = elem;
    }
}