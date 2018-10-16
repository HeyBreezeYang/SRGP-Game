package com.cellsgame.game.module.goods.csv.adjust;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.csv.ItemGoodsConfig;

import java.util.Map;

/**
 * User: 阚庆忠
 * Date: 2016/9/18 19:44
 * Desc:
 */
public class ItemConfigAdjuster implements CfgAdjuster<ItemGoodsConfig> {

    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<ItemGoodsConfig> getProType() {
        return ItemGoodsConfig.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, ItemGoodsConfig> map) {
        for (ItemGoodsConfig cfg : map.values()) {
            CacheGoods.getGoodsMap().put(cfg.getId(), cfg);
            CacheGoods.getItemGoodsMap().put(cfg.getId(), cfg);
            CacheGoods.getQuestCollectibleMap().put(cfg.getId(), cfg);
        }
    }
}
