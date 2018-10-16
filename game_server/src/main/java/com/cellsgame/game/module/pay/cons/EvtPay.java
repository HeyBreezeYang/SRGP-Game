package com.cellsgame.game.module.pay.cons;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtType;

import java.util.concurrent.atomic.AtomicInteger;

public  enum EvtPay implements EvtType {
    Pay

    ;
    private static final AtomicInteger inr = new AtomicInteger(ModuleID.Pay);

    static {
        EvtType[] values = values();
        for (EvtType eType : values) {
            eType.setEvtCode(inr.incrementAndGet());
        }
    }

    private int code;

    @Override
    public int getEvtCode() {
        return code;
    }

    @Override
    public void setEvtCode(int code) {
        this.code = code;
    }
}
