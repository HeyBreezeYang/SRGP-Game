package com.cellsgame.game.module.goods.csv.adjust;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.csv.FuncGoodsConfig;
import com.cellsgame.game.module.goods.csv.ItemGoodsConfig;

import java.util.Map;

/**
 * Date: 2016/9/18 19:44
 * Desc:
 */
public class FuncGoodsConfigAdjuster implements CfgAdjuster<FuncGoodsConfig> {

    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<FuncGoodsConfig> getProType() {
        return FuncGoodsConfig.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, FuncGoodsConfig> map) {
        for (FuncGoodsConfig cfg : map.values()) {
            CacheGoods.getGoodsMap().put(cfg.getId(), cfg);
            CacheGoods.getQuestCollectibleMap().put(cfg.getId(), cfg);
        }
    }
}
