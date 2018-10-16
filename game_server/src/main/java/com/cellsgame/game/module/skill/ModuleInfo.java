package com.cellsgame.game.module.skill;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.skill.bo.SkillBO;
import com.cellsgame.game.module.skill.csv.*;
import com.cellsgame.game.module.skill.csv.adjuster.*;

public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(SkillBO.class)
                .jsonHead()
                .loadCSVFile()
                .load("SkillPassive.csv", SkillPassiveCfg.class)
                .load("SkillSeal.csv", SkillSealCfg.class)
                .load("SkillSpecial.csv", SkillSpecialCfg.class)
                .load("SkillSupport.csv", SkillSupportCfg.class)
                .load("SkillWeapon.csv", SkillWeaponCfg.class)
                .load("SkillCondition.csv", SkillConditionCfg.class)
                .load("SkillBuff.csv", SkillBuffCfg.class)

                .adjustConfig()
                .regCusAdjuster(new SkillPassiveCfgAdjuster())
                .regCusAdjuster(new SkillSealCfgAdjuster())
                .regCusAdjuster(new SkillSpecialCfgAdjuster())
                .regCusAdjuster(new SkillSupportCfgAdjuster())
                .regCusAdjuster(new SkillWeaponCfgAdjuster())
                .checker()
                .listener()
                .build();
    }
}
