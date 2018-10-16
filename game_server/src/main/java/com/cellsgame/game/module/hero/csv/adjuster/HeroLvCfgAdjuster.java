package com.cellsgame.game.module.hero.csv.adjuster;


import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.hero.cache.CacheHeroLv;
import com.cellsgame.game.module.hero.cache.HeroLv;
import com.cellsgame.game.module.hero.csv.HeroLvCfg;

import java.util.Map;

public class HeroLvCfgAdjuster implements CfgAdjuster<HeroLvCfg> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<HeroLvCfg> getProType() {
        return HeroLvCfg.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, HeroLvCfg> map) {
        for (Map.Entry<Integer, HeroLvCfg> entry : map.entrySet()) {
            HeroLvCfg config = entry.getValue();

            CacheHeroLv.addHeroLv(config);

            CacheHeroLv.addHeroCareerLv(config);
        }
    }
}
