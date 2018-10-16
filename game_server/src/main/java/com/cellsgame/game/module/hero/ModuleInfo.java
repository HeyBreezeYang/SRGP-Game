package com.cellsgame.game.module.hero;


import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.hero.bo.HeroBO;
import com.cellsgame.game.module.hero.csv.*;
import com.cellsgame.game.module.hero.csv.adjuster.*;
//import com.cellsgame.game.module.hero.csv.ActivityRankPrizeConfig;
//import com.cellsgame.game.module.hero.csv.adjuster.ActRankPrizeAdjuster;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(HeroBO.class)
                .jsonHead()
                .loadCSVFile()
                .load("Hero.csv", HeroCfg.class)
                .load("HeroLv.csv", HeroLvCfg.class)
                .load("HeroStar.csv", HeroStarCfg.class)
                .load("HeroStage.csv", HeroStageCfg.class)
                .load("HeroCareer.csv", HeroCareerCfg.class)
                .adjustConfig()
                .regCusAdjuster(new HeroCfgAdjuster())
                .regCusAdjuster(new HeroLvCfgAdjuster())
                .regCusAdjuster(new HeroStarCfgAdjuster())
                .regCusAdjuster(new HeroStageCfgAdjuster())
                .regCusAdjuster(new HeroCareerCfgAdjuster())

                .checker()
                .listener()

                .onStartup(HeroBO::init)

                .build();
    }
}