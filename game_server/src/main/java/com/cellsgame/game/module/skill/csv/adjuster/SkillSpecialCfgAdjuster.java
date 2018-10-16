package com.cellsgame.game.module.skill.csv.adjuster;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.skill.cache.CacheSkill;
import com.cellsgame.game.module.skill.csv.SkillSpecialCfg;

import java.util.Map;

public class SkillSpecialCfgAdjuster implements CfgAdjuster<SkillSpecialCfg> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<SkillSpecialCfg> getProType() {
        return SkillSpecialCfg.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, SkillSpecialCfg> map) {
        for (SkillSpecialCfg cfg: map.values()) {
            CacheSkill.skillMap.put(cfg.getId(), cfg);
            CacheSkill.skillSpecialMap.put(cfg.getId(), cfg);

            CacheSkill.collcetSelfSkills(cfg);
        }
    }
}
