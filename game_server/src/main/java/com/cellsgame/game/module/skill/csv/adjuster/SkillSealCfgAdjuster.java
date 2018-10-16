package com.cellsgame.game.module.skill.csv.adjuster;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.skill.cache.CacheSkill;
import com.cellsgame.game.module.skill.csv.SkillPassiveCfg;
import com.cellsgame.game.module.skill.csv.SkillSealCfg;

import java.util.Map;

public class SkillSealCfgAdjuster implements CfgAdjuster<SkillSealCfg> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<SkillSealCfg> getProType() {
        return SkillSealCfg.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, SkillSealCfg> map) {
        for (SkillSealCfg cfg: map.values()) {
            CacheSkill.skillMap.put(cfg.getId(), cfg);
            CacheSkill.skillSealMap.put(cfg.getId(), cfg);
        }
    }
}
