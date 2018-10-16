package com.cellsgame.game.module.hero.cache;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.hero.csv.HeroCareerCfg;
import com.cellsgame.game.module.hero.csv.HeroCfg;
import com.cellsgame.game.module.hero.csv.HeroLvCfg;
import com.cellsgame.game.module.skill.cache.CacheSkill;

import java.util.List;
import java.util.Map;

public class CacheHeroOpenedSkill {
    // Map<HERO_CID, Map<SKILL_CID, OPEN_STAR>>
    private final static  Map<Integer, Map<Integer, Integer>> heroSkills = GameUtil.createSimpleMap();

    public static boolean hasSkill(Integer heroCId, Integer skillCId) {
        if (heroSkills.containsKey(heroCId)) {
            return heroSkills.get(heroCId).containsKey(skillCId);
        }
        return false;
    }

    public static Integer getSkillOpenStar(Integer heroCId, Integer skillCId) {
        Map<Integer, Integer> skills = heroSkills.get(heroCId);
        if (null != skills && skills.containsKey(skillCId)) {
            return skills.get(skillCId);
        }
        return 0;
    }

    public static boolean isOnlySelf(Integer heroCId, Integer skillCId) {
        if (heroSkills.containsKey(heroCId) && heroSkills.get(heroCId).containsKey(skillCId)) {
            return CacheSkill.selfSkills.contains(skillCId);
        }
        return false;
    }

    public static void addHeroSkills(HeroCfg cfg) {
        if (!heroSkills.containsKey(cfg.getId())) {
            heroSkills.put(cfg.getId(), GameUtil.createSimpleMap());
        }

        Map<Integer, Integer> skills = heroSkills.get(cfg.getId());

        List<FuncConfig> skillList = GameUtil.createList();
        if(null != cfg.getA_skills()) skillList.addAll(cfg.getA_skills());
        if(null != cfg.getB_skills()) skillList.addAll(cfg.getB_skills());
        if(null != cfg.getC_skills()) skillList.addAll(cfg.getC_skills());
        if(null != cfg.getAoyi_skills()) skillList.addAll(cfg.getAoyi_skills());
        if(null != cfg.getWeapon_skills()) skillList.addAll(cfg.getWeapon_skills());
        if(null != cfg.getSupport_skills()) skillList.addAll(cfg.getSupport_skills());

        for (FuncConfig config : skillList) {
            skills.put(config.getParam(), (int) config.getValue());
        }
    }

    public static void addHeroCareerSkills(HeroCareerCfg cfg) {
        if (!heroSkills.containsKey(cfg.getId())) {
            heroSkills.put(cfg.getId(), GameUtil.createSimpleMap());
        }

        Map<Integer, Integer> skills = heroSkills.get(cfg.getId());

        List<FuncConfig> skillList = GameUtil.createList();
        if(null != cfg.getA_skills()) skillList.addAll(cfg.getA_skills());
        if(null != cfg.getB_skills()) skillList.addAll(cfg.getB_skills());
        if(null != cfg.getC_skills()) skillList.addAll(cfg.getC_skills());
        if(null != cfg.getAoyi_skills()) skillList.addAll(cfg.getAoyi_skills());
        if(null != cfg.getWeapon_skills()) skillList.addAll(cfg.getWeapon_skills());
        if(null != cfg.getSupport_skills()) skillList.addAll(cfg.getSupport_skills());

        for (FuncConfig config : skillList) {
            skills.put(config.getParam() + HeroLvCfg.FULL_LEVEL, (int) config.getValue());
        }
    }
}
