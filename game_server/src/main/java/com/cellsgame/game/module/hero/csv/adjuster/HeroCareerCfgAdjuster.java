package com.cellsgame.game.module.hero.csv.adjuster;


import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.hero.cache.CacheHeroOpenedSkill;
import com.cellsgame.game.module.hero.cache.CacheHeroProp;
import com.cellsgame.game.module.hero.csv.HeroCareerCfg;

import java.util.Map;

public class HeroCareerCfgAdjuster implements CfgAdjuster<HeroCareerCfg> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<HeroCareerCfg> getProType() {
        return HeroCareerCfg.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, HeroCareerCfg> map) {
        for (Map.Entry<Integer, HeroCareerCfg> entry : map.entrySet()) {
            HeroCareerCfg config = entry.getValue();

            CacheHeroProp.addCareerFullLvProps(config);

            CacheHeroOpenedSkill.addHeroCareerSkills(config);
        }
    }
}
