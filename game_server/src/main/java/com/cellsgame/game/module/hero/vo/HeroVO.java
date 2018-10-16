package com.cellsgame.game.module.hero.vo;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.skill.cons.SkillType;
import com.cellsgame.game.module.skill.vo.SkillVO;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

import java.util.Map;

public class HeroVO extends DBVO {
    private Integer id;
    // 玩家ID
    private Integer playerId;

    @Save(ix=1)
    // 配置档ID
    private Integer cid;

    // 当前等级
    @Save(ix=2)
    private Integer level;

    // 当前星级
    @Save(ix=3)
    private Integer star;

    // 当前突破等级
    @Save(ix=4)
    private Integer stage;

    // 是否转职
    @Save(ix=5)
    private Boolean careerExchanged;

    @Save(ix=6)
    private Integer hp;

    @Save(ix=7)
    private Integer speed;

    @Save(ix=8)
    private Integer attack;

    @Save(ix=9)
    private Integer defense;

    @Save(ix=10)
    private Integer magicDefense;

    @Save(ix=11)
    private Integer technique;

    @Save(ix=12)
    private Integer luck;

    // 当前经验
    @Save(ix=13)
    private Integer sp;

    // 当前经验
    @Save(ix=14)
    private Integer exp;

    // 当前装备的技能列表KEY=SKILL_TYPE
    private Map<Integer, SkillVO> equippedSkills = GameUtil.createSimpleMap();

    // 当前解锁的技能，转职后会新解锁一批技能。KEY=SKILL_ID
    private Map<Integer, SkillVO> openedSkills = GameUtil.createSimpleMap();

    public Boolean getCareerExchanged() {
        return careerExchanged;
    }

    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = (Integer)pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{playerId};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys != null && relationKeys.length > 0)
            playerId = (int) relationKeys[0];
    }

    @Override
    protected void init() {
        id = 0;
        playerId = 0;
        level = 1;
        star = 0;
        stage = 0;
        sp=0;
        exp=0;
        careerExchanged = false;
    }

    @Override
    public Integer getCid() {
        return this.cid;
    }

    @Override
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getHp() {
        return hp;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getSpeed() {
        return this.speed;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getAttack() {
        return this.attack;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getDefense() {
        return this.defense;
    }

    public void setMagicDefense(Integer magicDefense) {
        this.magicDefense = magicDefense;
    }

    public Integer getMagicDefense() {
        return this.magicDefense;
    }

    public Integer getSp() {
        return sp;
    }

    public void setSp(Integer sp) {
        this.sp = sp;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Boolean isCareerExchanged() {
        return careerExchanged;
    }

    public void setCareerExchanged(Boolean careerExchanged) {
        this.careerExchanged = careerExchanged;
    }

    public Integer getTechnique() {
        return technique;
    }

    public void setTechnique(Integer technique) {
        this.technique = technique;
    }

    public Integer getLuck() {
        return luck;
    }

    public void setLuck(Integer luck) {
        this.luck = luck;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Map<Integer, SkillVO> getEquippedSkills() {
        return equippedSkills;
    }

    public void setEquippedSkills(Map<Integer, SkillVO> skills) {
        this.equippedSkills = equippedSkills;
    }

    public Map<Integer, SkillVO> getOpenedSkills() {
        return openedSkills;
    }

    public void setOpenedSkills(Map<Integer, SkillVO> allSkills) {
        this.openedSkills = allSkills;
    }
}
