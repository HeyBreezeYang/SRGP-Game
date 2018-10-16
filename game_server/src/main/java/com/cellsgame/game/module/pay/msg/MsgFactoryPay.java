package com.cellsgame.game.module.pay.msg;

import com.cellsgame.game.module.MsgFactory;

import java.util.Map;

/**
 * Created by yfzhang on 2016/8/22.
 */
public class MsgFactoryPay extends MsgFactory {
    public static final String PAY = "pay";


    public static final String PAY_LIST = "lst";
    public static final String PAY_UPDATE = "upd";

    private static final MsgFactoryPay instance = new MsgFactoryPay();

    public static final MsgFactoryPay instance() {
        return instance;
    }

    @Override
    public String getModulePrefix() {
        return PAY;
    }


    public Map<String, Object> getPayPrizeMsg(Map<String, Object> parent, Map prizeMap) {
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        addPrizeMsg(op, prizeMap);
        return parent;
    }

    public Map<String, Object> getBuyOrderGoodsMapMsg(Map<String, Object> parent, Map<String, Integer> buyOrderGoodsNum) {
        parent = creIfNull(parent);
        Map op = gocInfoMap(parent);
        Map<String, Integer> info = gocMapIn(op, PAY_LIST);
        info.putAll(buyOrderGoodsNum);
        return parent;
    }

    public Map<String, Object> getBuyOrderGoodsUpdateMsg(Map<String, Object> parent, String itemId, int buyNum) {
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        Map<String, Integer> info = gocMapIn(op, PAY_UPDATE);
        info.put(itemId, buyNum);
        return parent;
    }

}
