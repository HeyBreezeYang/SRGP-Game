package com.cellsgame.game.module.shop.evt;

import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;

/**
 * @author Aly on 2017-05-09.
 */
public enum EvtTypeShop implements EvtType {
    /**
     * 购买商店物品
     *
     * @see EvtParamType#SHOP_CID 商城ID
     */
    BUY,
    /**
     * 购买商店物品
     *
     * @see EvtParamType#SHOP_CID 商城ID
     * @see EvtParamType#NUM 剩余次数
     */
    Refresh,;
    private static final AtomicInteger incr = new AtomicInteger(ModuleID.Shop);

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
