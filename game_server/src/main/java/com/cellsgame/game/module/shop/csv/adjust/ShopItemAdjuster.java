package com.cellsgame.game.module.shop.csv.adjust;

import java.util.Map;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.shop.GroupShopItem;
import com.cellsgame.game.module.shop.bo.ShopBO;
import com.cellsgame.game.module.shop.csv.ShopItemConfig;

/**
 * 所有商品数据重新整理为另一种存储格式.
 *
 * @author Yang
 */
public class ShopItemAdjuster implements CfgAdjuster<ShopItemConfig> {

    @Override
    public boolean needAdd2Cache() {
        return true;
    }

    @Override
    public Class<ShopItemConfig> getProType() {
        return ShopItemConfig.class;
    }

    /**
     * @param map 所有商品数据
     */
    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, ShopItemConfig> map) {
        // 查看所有商品
        map.forEach((integer, shopItemConfig) -> {
            // 查看当前商品所属商品组是否存在
            GroupShopItem group = ShopBO.ShopItemGroup.get(shopItemConfig.getGroup());
            // 如果不存在
            if (group == null)
                ShopBO.ShopItemGroup.put(shopItemConfig.getGroup(), group = new GroupShopItem(shopItemConfig.getGroup()));
            // 添加商品
            group.addShopItem(shopItemConfig);
        });
    }
}
