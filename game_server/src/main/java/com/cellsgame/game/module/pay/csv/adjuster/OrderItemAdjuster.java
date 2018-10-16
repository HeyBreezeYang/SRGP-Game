package com.cellsgame.game.module.pay.csv.adjuster;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.pay.cache.CacheOrderItem;
import com.cellsgame.game.module.pay.csv.OrderItemConfig;

import java.util.Map;

public class OrderItemAdjuster implements CfgAdjuster<OrderItemConfig> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<OrderItemConfig> getProType() {
        return OrderItemConfig.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, OrderItemConfig> map) {
        for (OrderItemConfig config : map.values()) {
            CacheOrderItem.cache(config);
        }

    }
}
