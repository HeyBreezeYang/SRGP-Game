package com.cellsgame.game.module.skill.csv.adjuster;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.skill.cache.CacheSkill;
import com.cellsgame.game.module.skill.csv.SkillSealCfg;
import com.cellsgame.game.module.skill.csv.SkillSupportCfg;

import java.util.Map;

public class SkillSupportCfgAdjuster implements CfgAdjuster<SkillSupportCfg> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<SkillSupportCfg> getProType() {
        return SkillSupportCfg.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, SkillSupportCfg> map) {
        for (SkillSupportCfg cfg: map.values()) {
            CacheSkill.skillMap.put(cfg.getId(), cfg);
            CacheSkill.skillSupportMap.put(cfg.getId(), cfg);

            CacheSkill.collcetSelfSkills(cfg);
        }
    }
}
