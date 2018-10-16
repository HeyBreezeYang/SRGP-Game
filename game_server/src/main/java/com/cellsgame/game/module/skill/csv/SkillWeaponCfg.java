package com.cellsgame.game.module.skill.csv;

import com.cellsgame.common.util.GameUtil;

import java.util.List;
import java.util.Set;

/**
 * 技能(武器)配置
 */
public class SkillWeaponCfg extends SkillCfg {
    // 威力
    private int power;
    // 射程
    private int range;
    // 武器类型
    private int weapon_type;
    // 武器克制组
    private int[] weapon_counters;
    // 兵种克制组
    private int[] arms_counters;

    // 攻击次数
    private int attack_times;

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getWeapon_type() {
        return weapon_type;
    }

    public void setWeapon_type(int weapon_type) {
        this.weapon_type = weapon_type;
    }

    public int[] getWeapon_counters() {
        return weapon_counters;
    }

    public void setWeapon_counters(int[] weapon_counters) {
        this.weapon_counters = weapon_counters;
    }

    public int[] getArms_counters() {
        return arms_counters;
    }

    public void setArms_counters(int[] arms_counters) {
        this.arms_counters = arms_counters;
    }

    public int getAttack_times() {
        return attack_times;
    }

    public void setAttack_times(int attack_times) {
        this.attack_times = attack_times;
    }

}
