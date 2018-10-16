package com.cellsgame.game.module.depot.cons;

import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;

/**
 * File Description.
 *
 * @author Yang
 */
public enum EventTypeDepot implements EvtType {
    /**
     * 物品事件
     *
     * @see EvtParamType#GOODS_CID 物品CID
     * @see EvtParamType#BEFORE 数量变化前
     * @see EvtParamType#AFTER 数量变化后
     */
    Goods,
    /**
     * 物品收集类型事件
     *
     * @see EvtParamType#COLLECT_TYPE 物品收集类型
     * @see EvtParamType#BEFORE 数量变化前
     * @see EvtParamType#AFTER 数量变化后
     */
    GoodsCollectType,
    /**
     * 货币数量
     *
     * @see EvtParamType#CURRENCY_TYPE 货币ID
     * @see EvtParamType#BEFORE 数量变化前
     * @see EvtParamType#AFTER 数量变化后
     */
    Currency,
    ;
    private static final AtomicInteger incr = new AtomicInteger(ModuleID.Depot);

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
