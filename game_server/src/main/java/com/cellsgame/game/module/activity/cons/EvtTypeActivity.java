package com.cellsgame.game.module.activity.cons;

import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtType;

public enum EvtTypeActivity implements EvtType {
	
	AcceptNewActivity,
	RefActivity,
	StopActivity
    
    ;
    private static final AtomicInteger incr = new AtomicInteger(ModuleID.Activity);

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

    public void setEvtCode(int evtCode) {
        this.evtCode = evtCode;
    }

}
