package com.cellsgame.game.module.skill.cache;

import java.util.Map;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.skill.csv.*;

public class CacheSkill {
    public static Map<Integer, SkillCfg> skillMap = GameUtil.createMap();
    public static Map<Integer, SkillSealCfg> skillSealMap = GameUtil.createMap();
    public static Map<Integer, SkillPassiveCfg> skillPassiveMap = GameUtil.createMap();
    public static Map<Integer, SkillSupportCfg> skillSupportMap = GameUtil.createMap();
    public static Map<Integer, SkillWeaponCfg> skillWeaponMap = GameUtil.createMap();
    public static Map<Integer, SkillSpecialCfg> skillSpecialMap = GameUtil.createMap();

    public static Set<Integer> selfSkills = GameUtil.createSet();


    public static void collcetSelfSkills(SkillCfg cfg) {
        int[] limits = cfg.getLimit_weapons();
        if (null == limits || limits.length == 0) {
            return;
        }
        for (int i = 0;i < limits.length;i ++) {
            if (999 == limits[i]) {
                selfSkills.add(limits[i]);
            }
        }
    }
}
