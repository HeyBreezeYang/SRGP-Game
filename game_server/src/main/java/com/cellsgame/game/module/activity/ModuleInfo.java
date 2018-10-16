package com.cellsgame.game.module.activity;


import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.activity.bo.ActivityBO;
import com.cellsgame.game.module.activity.csv.ActivityConfig;
import com.cellsgame.game.module.activity.csv.ActivityRankPrizeConfig;
import com.cellsgame.game.module.activity.csv.adjuster.ActRankPrizeAdjuster;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(ActivityBO.class)
                .jsonHead()
                .loadCSVFile()
                .load("ActivityRankPrize.csv", ActivityRankPrizeConfig.class)
                .adjustConfig()
                .regCusAdjuster(new ActRankPrizeAdjuster())
                .checker()
                .listener()
                .onStartup(ActivityBO::init)
                .build();
    }
}