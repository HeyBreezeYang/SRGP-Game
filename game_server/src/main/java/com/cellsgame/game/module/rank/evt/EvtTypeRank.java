package com.cellsgame.game.module.rank.evt;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtType;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Aly on 2017-05-09.
 */
public enum EvtTypeRank implements EvtType {
    ADD_LIKE
    ;
    private static final AtomicInteger incr = new AtomicInteger(ModuleID.RANK);

    static {
        EvtType[] values = values();
        for (EvtType eType : values) {
            eType.setEvtCode(incr.incrementAndGet());
        }
    }

    private int evtCode;

    @Override
    public int getEvtCode() {
        return evtCode;
    }

    @Override
    public void setEvtCode(int code) {
        evtCode = code;
    }
}
