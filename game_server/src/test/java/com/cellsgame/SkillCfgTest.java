package com.cellsgame;

import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.module.load.ModuleLoader;
import com.cellsgame.game.module.skill.cache.CacheSkill;
import com.cellsgame.game.module.skill.csv.SkillCfg;
import com.cellsgame.game.module.skill.csv.SkillWeaponCfg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkillCfgTest {
    private static final Logger log = LoggerFactory.getLogger(SkillCfgTest.class);
    public static void main(String[] args) throws Exception {
        ModuleLoader.loadAllModule("com.cellsgame.game.module");

        ModuleLoader.loadConfigs(ModuleID.System);

        SkillCfg cfg = CacheSkill.skillMap.get(21110001);
        System.out.println(">>>cfg :" + cfg.getId());
    }
}
