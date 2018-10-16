package com.cellsgame.game.module.hero.csv;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;
import java.util.Map;

public class HeroCfg extends BaseCfg {
    private int id;
    // 颜色类型
    private int color_type;
    // 移动方式
    private int move_type;
    // 武器
    private int weapon_type;
    // 性别
    private int gender;
    // HP
    private int[] hps;
    // 攻击
    private int[] attacks;
    // 防守
    private int[] defenses;
    // 魔防
    private int[] magic_defenses;
    // 速度
    private int[] speeds;
    // 技巧
    private int[] techniques;
    // 幸运
    private int[] lucks;
    // 祝福类型
    private int bless_type;
    // 祝福类型加成
    private String[] bless_attribute_add;
    // 初始武器技能
    private List<FuncConfig> weapon_skills;
    // 初始辅助技能
    private List<FuncConfig> support_skills;
    // 初始被动技能A
    private List<FuncConfig> a_skills;
    // 初始被动技能B
    private List<FuncConfig> b_skills;
    // 初始被动技能C
    private List<FuncConfig> c_skills;
    // 初始奥义技能
    private List<FuncConfig> aoyi_skills;

    // 英雄类型，是否是系统英雄
    private int hero_type;
    // 祝福属性类型
    private int bless_type_1;
    // 祝福类型值
    private int bless_value_1;
    // 祝福属性类型
    private int bless_type_2;
    // 祝福类型值
    private int bless_value_2;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor_type() {
        return color_type;
    }

    public void setColor_type(int color_type) {
        this.color_type = color_type;
    }

    public int getMove_type() {
        return move_type;
    }

    public void setMove_type(int move_type) {
        this.move_type = move_type;
    }

    public int getWeapon_type() {
        return weapon_type;
    }

    public void setWeapon_type(int weapon_type) {
        this.weapon_type = weapon_type;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int[] getHps() {
        return hps;
    }

    public void setHps(int[] hps) {
        this.hps = hps;
    }

    public int[] getAttacks() {
        return attacks;
    }

    public void setAttacks(int[] attacks) {
        this.attacks = attacks;
    }

    public int[] getDefenses() {
        return defenses;
    }

    public void setDefenses(int[] defenses) {
        this.defenses = defenses;
    }

    public int[] getMagic_defenses() {
        return magic_defenses;
    }

    public void setMagic_defenses(int[] magic_defenses) {
        this.magic_defenses = magic_defenses;
    }

    public int[] getSpeeds() {
        return speeds;
    }

    public void setSpeeds(int[] speeds) {
        this.speeds = speeds;
    }

    public int[] getTechniques() {
        return techniques;
    }

    public void setTechniques(int[] techniques) {
        this.techniques = techniques;
    }

    public int[] getLucks() {
        return lucks;
    }

    public void setLucks(int[] lucks) {
        this.lucks = lucks;
    }

    public int getBless_type() {
        return bless_type;
    }

    public void setBless_type(int bless_type) {
        this.bless_type = bless_type;
    }

    public String[] getBless_attribute_add() {
        return bless_attribute_add;
    }

    public void setBless_attribute_add(String[] bless_attribute_add) {
        this.bless_attribute_add = bless_attribute_add;
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

    public int getHero_type() {
        return hero_type;
    }

    public void setHero_type(int hero_type) {
        this.hero_type = hero_type;
    }

    public int getBless_type_1() {
        return bless_type_1;
    }

    public void setBless_type_1(int bless_type_1) {
        this.bless_type_1 = bless_type_1;
    }

    public int getBless_value_1() {
        return bless_value_1;
    }

    public void setBless_value_1(int bless_value_1) {
        this.bless_value_1 = bless_value_1;
    }

    public int getBless_type_2() {
        return bless_type_2;
    }

    public void setBless_type_2(int bless_type_2) {
        this.bless_type_2 = bless_type_2;
    }

    public int getBless_value_2() {
        return bless_value_2;
    }

    public void setBless_value_2(int bless_value_2) {
        this.bless_value_2 = bless_value_2;
    }
}
