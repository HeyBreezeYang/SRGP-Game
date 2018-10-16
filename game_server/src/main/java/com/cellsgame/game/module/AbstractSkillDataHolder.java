//package com.cellsgame.game.module;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//
//import com.cellsgame.common.util.GameUtil;
//import com.cellsgame.game.module.hero.vo.HeroVO;
//import com.cellsgame.game.module.skill.csv.SkillConfig;
//import com.cellsgame.game.module.skill.vo.SkillVO;
//import com.cellsgame.orm.DBVO;
//
///**
// * File Description.
// *
// * @author Yang
// */
//public abstract class AbstractSkillDataHolder extends DBVO {
//    private static final Comparator<SkillVO> SORTER = Comparator.comparingInt(SkillVO::getIndex);
//    // 所有技能
//    private Map<Integer, SkillVO> skills = GameUtil.createSimpleMap();
//    // 战斗技能
//    private Map<Integer, SkillVO> battleSkills = GameUtil.createSimpleMap();
//
//    public Map<Integer, SkillVO> getBattleSkills() {
//        return battleSkills;
//    }
//
//    public void setBattleSkills(Map<Integer, SkillVO> battleSkills) {
//        this.battleSkills = battleSkills;
//    }
//
//    public Map<Integer, SkillVO> getSkills() {
//        return skills;
//    }
//
//    public void setSkills(Map<Integer, SkillVO> skills) {
//        this.skills = skills;
//    }
//
//    public List<SkillVO> getSortedSkills() {
//        // 所有技能
//        List<SkillVO> skillVOs = new ArrayList<>(skills.values());
//        // 排序所有技能
//        skillVOs.sort(SORTER);
//        return skillVOs;
//    }
//
//    public abstract int getLevel();
//    
//    
//    public int calSkillFightForce(){
//    	int force = 0;
//    	//伙伴技能才会存在小技能和怒气技能
//    	for(SkillVO skill : skills.values()){
//    		if(skill.getCfg().getPos() == SkillConfig.SkillPos_Fur){
//    			force += skill.getLevel() * 30;
//    		}
//    		if(skill.getCfg().getPos() == SkillConfig.SkillPos_Min){
//    			force += skill.getLevel() * 15;
//    		}
//    	}
//    	//主角才能装备技能
//    	for(SkillVO skill : battleSkills.values()){
//			force += skill.getLevel() * 35;
//    	}
//    	return force;
//    }
//}
