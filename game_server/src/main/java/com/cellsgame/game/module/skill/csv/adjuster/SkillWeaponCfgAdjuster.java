package com.cellsgame.game.module.skill.csv.adjuster;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.skill.cache.CacheSkill;
import com.cellsgame.game.module.skill.csv.SkillWeaponCfg;

import java.util.Map;

public class SkillWeaponCfgAdjuster implements CfgAdjuster<SkillWeaponCfg> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<SkillWeaponCfg> getProType() {
        return SkillWeaponCfg.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, SkillWeaponCfg> map) {
        for (SkillWeaponCfg cfg: map.values()) {
            CacheSkill.skillMap.put(cfg.getId(), cfg);
            CacheSkill.skillWeaponMap.put(cfg.getId(), cfg);

            CacheSkill.collcetSelfSkills(cfg);
        }
    }
}
