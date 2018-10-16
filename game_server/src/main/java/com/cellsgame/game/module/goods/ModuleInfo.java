package com.cellsgame.game.module.goods;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.goods.bo.GoodsBO;
import com.cellsgame.game.module.goods.csv.FuncGoodsConfig;
import com.cellsgame.game.module.goods.csv.ItemGoodsConfig;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.goods.csv.adjust.FuncGoodsConfigAdjuster;
import com.cellsgame.game.module.goods.csv.adjust.ItemConfigAdjuster;
import com.cellsgame.game.module.goods.csv.adjust.GoodsAdjuster;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(GoodsBO.class)
                .jsonHead()
                .loadCSVFile()
                .load("Goods.csv", GoodsConfig.class)
                .load("Item.csv", ItemGoodsConfig.class)
//                .load("FuncGoods.csv", FuncGoodsConfig.class)
                .adjustConfig()
                .regCusAdjuster(new ItemConfigAdjuster())
                .regCusAdjuster(new GoodsAdjuster())
//                .regCusAdjuster(new FuncGoodsConfigAdjuster())
                .build();
    }
}