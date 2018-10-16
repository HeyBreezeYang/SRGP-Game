package com.cellsgame.game.module.hero.csv;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;
import java.util.Map;

/**
 * 英雄转职配置表
 */
public class HeroCareerCfg extends BaseCfg {

    private int id;

    // 英雄配置ID
    private int hero_id;

    private int need_level;

    // 转职后满级时属性 ---BEGIN
    private int max_hp;

    private int max_speed;

    private int max_attack;

    private int max_defense;

    private int max_magic_defense;

    private int max_technique;

    private int max_luck;
    // 转职后满级时属性 ---END

    // 转职后开放技能 ---BEGIN
    private List<FuncConfig> weapon_skills;

    private List<FuncConfig> support_skills;

    private List<FuncConfig> a_skills;

    private List<FuncConfig> b_skills;

    private List<FuncConfig> c_skills;

    private List<FuncConfig> aoyi_skills;
    // 转职后开放技能 ---END

    // 转职所需道具
    private List<FuncConfig> cost_items;


    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getHero_id() {
        return hero_id;
    }

    public void setHero_id(int hero_id) {
        this.hero_id = hero_id;
    }

    public int getNeed_level() {
        return need_level;
    }

    public void setNeed_level(int need_level) {
        this.need_level = need_level;
    }

    public int getMax_hp() {
        return max_hp;
    }

    public void setMax_hp(int max_hp) {
        this.max_hp = max_hp;
    }

    public int getMax_attack() {
        return max_attack;
    }

    public void setMax_attack(int max_attack) {
        this.max_attack = max_attack;
    }

    public int getMax_defense() {
        return max_defense;
    }

    public void setMax_defense(int max_defense) {
        this.max_defense = max_defense;
    }

    public int getMax_magic_defense() {
        return max_magic_defense;
    }

    public void setMax_magic_defense(int max_magic_defense) {
        this.max_magic_defense = max_magic_defense;
    }

    public int getMax_technique() {
        return max_technique;
    }

    public void setMax_technique(int max_technique) {
        this.max_technique = max_technique;
    }

    public int getMax_luck() {
        return max_luck;
    }

    public void setMax_luck(int max_luck) {
        this.max_luck = max_luck;
    }

    public List<FuncConfig> getWeapon_skills() {
        return weapon_skills;
    }

    public void setWeapon_skills(List<FuncConfig> weapon_skills) {
        this.weapon_skills = weapon_skills;
    }

    public List<FuncConfig> getSupport_skills() {
        return support_skills;
    }

    public void setSupport_skills(List<FuncConfig> support_skills) {
        this.support_skills = support_skills;
    }

    public List<FuncConfig> getA_skills() {
        return a_skills;
    }

    public void setA_skills(List<FuncConfig> a_skills) {
        this.a_skills = a_skills;
    }

    public List<FuncConfig> getB_skills() {
        return b_skills;
    }

    public void setB_skills(List<FuncConfig> b_skills) {
        this.b_skills = b_skills;
    }

    public List<FuncConfig> getC_skills() {
        return c_skills;
    }

    public void setC_skills(List<FuncConfig> c_skills) {
        this.c_skills = c_skills;
    }

    public List<FuncConfig> getAoyi_skills() {
        return aoyi_skills;
    }

    public void setAoyi_skills(List<FuncConfig> aoyi_skills) {
        this.aoyi_skills = aoyi_skills;
    }

    public List<FuncConfig> getCost_items() {
        return cost_items;
    }

    public void setCost_items(List<FuncConfig> cost_items) {
        this.cost_items = cost_items;
    }

    public int getMax_speed() {
        return max_speed;
    }

    public void setMax_speed(int max_speed) {
        this.max_speed = max_speed;
    }
}
