package com.cellsgame.game.module.hero.csv.adjuster;


import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.hero.csv.HeroStageCfg;

import java.util.Map;

public class HeroStageCfgAdjuster implements CfgAdjuster<HeroStageCfg> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<HeroStageCfg> getProType() {
        return HeroStageCfg.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, HeroStageCfg> map) {
        for (Map.Entry<Integer, HeroStageCfg> entry : map.entrySet()) {
            HeroStageCfg config = entry.getValue();

            Integer cid = config.getHero_id();
            if (!HeroStageCfg.configs.containsKey(cid)) {
                HeroStageCfg.configs.put(cid, GameUtil.createSimpleMap());
            }

            Map<Integer, HeroStageCfg> levelCfg = HeroStageCfg.configs.get(cid);
            levelCfg.put(config.getLevel(), config);
        }
    }
}
