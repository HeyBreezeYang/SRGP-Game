package com.cellsgame.game.module.friend.cons;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtType;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * File Description.
 *
 * @author Yang
 */
public enum EventTypeFriend implements EvtType {
    /**
     * 添加好友
     *
     */
    Add,
    ;
    private static final AtomicInteger incr = new AtomicInteger(ModuleID.Friends);

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
