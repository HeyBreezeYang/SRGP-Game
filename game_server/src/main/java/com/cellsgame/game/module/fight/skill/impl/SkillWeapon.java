package com.cellsgame.game.module.fight.skill.impl;

import com.cellsgame.game.module.skill.csv.SkillWeaponCfg;

import java.util.Set;

public class SkillWeapon extends SkillBase {
    private int power;
    private int range;
    private int weapon_type;
    private int[] weapon_weak_list;
    private int[] arms_weak_list;
    private int attack_times;

    public SkillWeapon(Integer skillId, Integer actorId, Set<Integer> to_actors) {
        super(skillId, actorId, to_actors);
    }

    // TODO 模拟lua构造函数
    public void ctor(SkillWeaponCfg cfg) {
        power = cfg.getPower();
        range = cfg.getRange();
        weapon_type = cfg.getWeapon_type();
        weapon_weak_list = cfg.getWeapon_counters();
        arms_weak_list = cfg.getArms_counters();
    }

    public void equip() {
        // TODO
    }

    public void unequip() {
        // TODO
    }
}
