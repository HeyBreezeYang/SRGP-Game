package com.cellsgame.game.module.goods.csv.adjust;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.csv.GoodsConfig;

import java.util.Map;

public class GoodsAdjuster implements CfgAdjuster<GoodsConfig> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<GoodsConfig> getProType() {
        return GoodsConfig.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, GoodsConfig> map) {
        for (GoodsConfig cfg : map.values()) {
            CacheGoods.getGoodsMap().put(cfg.getId(), cfg);
            CacheGoods.getQuestCollectibleMap().put(cfg.getId(), cfg);
        }
    }

}
