package com.cellsgame.game.module.hero.csv.adjuster;


import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.hero.cache.CacheHeroOpenedSkill;
import com.cellsgame.game.module.hero.cache.CacheHeroProp;
import com.cellsgame.game.module.hero.cons.HeroType;
import com.cellsgame.game.module.hero.csv.HeroCfg;

import java.util.Map;

public class HeroCfgAdjuster implements CfgAdjuster<HeroCfg> {
    @Override
    public boolean needAdd2Cache() {
        return true;
    }

    @Override
    public Class<HeroCfg> getProType() {
        return HeroCfg.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, HeroCfg> map) {
        for (Map.Entry<Integer, HeroCfg> entry : map.entrySet()) {
            HeroCfg config = entry.getValue();
            // 过滤掉怪物
            if (config.getHero_type() == HeroType.MONSTER.getValue()) {
                continue;
            }

            CacheHeroProp.addOneLvProps(config);

            CacheHeroProp.addFullLvProps(config);

            CacheHeroOpenedSkill.addHeroSkills(config);
        }
    }
}
