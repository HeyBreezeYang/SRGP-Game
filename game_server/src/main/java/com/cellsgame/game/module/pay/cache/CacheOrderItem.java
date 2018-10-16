package com.cellsgame.game.module.pay.cache;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.pay.csv.OrderItemConfig;

import java.util.Map;

public class CacheOrderItem {

    private static Map<String, OrderItemConfig> orderItemConfigMap = GameUtil.createSimpleMap();

    public static void cache(OrderItemConfig config){
        OrderItemConfig c = orderItemConfigMap.get(config.getItemId());
        if(c != null){
            throw new RuntimeException("OrderItemConfig itemId repeat");
        }
        orderItemConfigMap.put(config.getItemId(), config);
    }

    public static OrderItemConfig getConfig(String itemId){
        return orderItemConfigMap.get(itemId);
    }
}
