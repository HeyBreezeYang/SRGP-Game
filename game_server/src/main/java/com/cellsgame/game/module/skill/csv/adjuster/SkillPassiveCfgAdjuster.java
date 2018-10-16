package com.cellsgame.game.module.skill.csv.adjuster;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.skill.cache.CacheSkill;
import com.cellsgame.game.module.skill.csv.SkillPassiveCfg;

import java.util.Map;

public class SkillPassiveCfgAdjuster implements CfgAdjuster<SkillPassiveCfg> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<SkillPassiveCfg> getProType() {
        return SkillPassiveCfg.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, SkillPassiveCfg> map) {
        for (SkillPassiveCfg cfg: map.values()) {
            CacheSkill.skillMap.put(cfg.getId(), cfg);
            CacheSkill.skillPassiveMap.put(cfg.getId(), cfg);

            CacheSkill.collcetSelfSkills(cfg);
        }
    }
}
