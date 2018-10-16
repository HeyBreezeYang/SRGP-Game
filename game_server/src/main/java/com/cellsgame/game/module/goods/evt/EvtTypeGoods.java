package com.cellsgame.game.module.goods.evt;

import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;

/**
 * @author Aly on 2017-05-09.
 */
public enum EvtTypeGoods implements EvtType {
    /**
     * 玩家开宝箱
     *
     * @see EvtParamType#GOODS_CID 物品CID
     * @see EvtParamType#NUM 使用数量
     */
    USE_BOX,
    /**
     * 收集到指定物品
     */
    GOODS_COLLC,
    /**
     * 使用指定物品
     */
    GOODS_USE,
    ;
    private static final AtomicInteger incr = new AtomicInteger(ModuleID.Goods);

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
