package com.cellsgame.game.module.shop;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.shop.bo.ShopBO;
import com.cellsgame.game.module.shop.csv.ShopConfig;
import com.cellsgame.game.module.shop.csv.ShopItemConfig;
import com.cellsgame.game.module.shop.csv.SimpleShopItemConfig;
import com.cellsgame.game.module.shop.csv.adjust.ShopItemAdjuster;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(ShopBO.class)
                .jsonHead()
                .loadCSVFile()
                .load("Shop.csv", ShopConfig.class)
                .load("ShopItem.csv", ShopItemConfig.class)
//                .load("SpecialShopItem.csv", SimpleShopItemConfig.class)

                .adjustConfig()
                .regCusAdjuster(new ShopItemAdjuster())

                .build();
    }
}