package com.cellsgame.game.module.hero.csv;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;
import java.util.Map;

/**
 * 英雄升星(觉醒)配置表
 */
public class HeroStarCfg extends BaseCfg {

    public final static Integer MAX_STAR = 5;

    // Map<CID, Map<LEVEL, HeroStarCfg>>
    public static Map<Integer, Map<Integer, HeroStarCfg>> configs = GameUtil.createSimpleMap();;

    // 英雄配置ID
    private int hero_id;

    // 星级等级
    private int level;

    // 对应星级等级属性累加值 ---BEGIN
    private int hp;

    private int speed;

    private int attack;

    private int defense;

    private int magic_defense;

    private int technique;

    private int luck;
    // 对应星级等级属性累加值 ---END

    private int equiped_weapon_skill;

    private int equiped_support_skill;

    // 升星所需道具
    private List<FuncConfig> cost_items;

    public int getHero_id() {
        return hero_id;
    }

    public void setHero_id(int hero_id) {
        this.hero_id = hero_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMagic_defense() {
        return magic_defense;
    }

    public void setMagic_defense(int magic_defense) {
        this.magic_defense = magic_defense;
    }

    public int getTechnique() {
        return technique;
    }

    public void setTechnique(int technique) {
        this.technique = technique;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public List<FuncConfig> getCost_items() {
        return cost_items;
    }

    public void setCost_items(List<FuncConfig> cost_items) {
        this.cost_items = cost_items;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getEquiped_weapon_skill() {
        return equiped_weapon_skill;
    }

    public void setEquiped_weapon_skill(int equiped_weapon_skill) {
        this.equiped_weapon_skill = equiped_weapon_skill;
    }

    public int getEquiped_support_skill() {
        return equiped_support_skill;
    }

    public void setEquiped_support_skill(int equiped_support_skill) {
        this.equiped_support_skill = equiped_support_skill;
    }
}
