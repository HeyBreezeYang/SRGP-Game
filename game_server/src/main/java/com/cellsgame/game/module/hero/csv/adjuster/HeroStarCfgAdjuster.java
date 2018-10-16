package com.cellsgame.game.module.hero.csv.adjuster;


import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.hero.csv.HeroLvCfg;
import com.cellsgame.game.module.hero.csv.HeroStarCfg;

import java.util.Map;

public class HeroStarCfgAdjuster implements CfgAdjuster<HeroStarCfg> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<HeroStarCfg> getProType() {
        return HeroStarCfg.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, HeroStarCfg> map) {
        for (Map.Entry<Integer, HeroStarCfg> entry : map.entrySet()) {
            HeroStarCfg config = entry.getValue();

            Integer cid = config.getHero_id();
            if (!HeroStarCfg.configs.containsKey(cid)) {
                HeroStarCfg.configs.put(cid, GameUtil.createSimpleMap());
            }

            Map<Integer, HeroStarCfg> levelCfg = HeroStarCfg.configs.get(cid);
            levelCfg.put(config.getLevel(), config);
        }
    }
}
