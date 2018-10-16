package com.cellsgame.game.core.event;

import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.game.cons.ModuleID;

/**
 * File Description.
 *
 * @author Yang
 */
public enum EventTypeAll implements EvtType {
    ALL;

    private static final AtomicInteger incr = new AtomicInteger(ModuleID.System);

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
        this.evtCode = code;
    }

	
}
