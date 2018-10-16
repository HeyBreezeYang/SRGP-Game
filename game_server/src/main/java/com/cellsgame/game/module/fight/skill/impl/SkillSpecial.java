package com.cellsgame.game.module.fight.skill.impl;

import com.cellsgame.game.module.skill.csv.SkillCfg;
import com.cellsgame.game.module.skill.csv.SkillSpecialCfg;

import java.util.Set;

public class SkillSpecial extends SkillBase {
    public enum SpecialType {
        SingleAttack(1),
        GroupAttack(2),
        Heal(3),
        ReAction(4),
        Defense(5),
        ;
        private int value;
        SpecialType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    private int special_type;
    private int max_cd;
    private int cd;
    private int cd_attack_variable;
    private int cd_defense_variable;

    public SkillSpecial(Integer skillId, Integer actorId, Set<Integer> to_actors) {
        super(skillId, actorId, to_actors);
    }

    public void ctor(SkillSpecialCfg cfg) {
        special_type = cfg.getAoyi_type();
        max_cd = cfg.getMax_cd();

        cd = max_cd;
        cd_attack_variable = 1;
        cd_defense_variable = 1;
    }

    public int getCd() {
        return cd;
    }

    public void execute() {
        this.cd = this.max_cd;
    }

    /**
     * 攻击时减少奥义计量
     */
    public void reduce_cd_attack() {
        change_cd(-cd_attack_variable);
    }

    /**
     * 防御时减少奥义计量
     */
    public void reduce_cd_defense() {
        change_cd(-cd_defense_variable);
    }

    /**
     * 改变奥义计量上限
     */
    public int change_max_cd(int value) {
        max_cd += value;
        if (max_cd < 0) {
            value -= max_cd;
            max_cd = 0;
        }
        return value;
    }

    /**
     * 改变奥义计量
     */
    public int change_cd(int value) {
        cd += value;
        if (cd < 0) {
            value -= cd;
            cd = 0;
        }
        return value;
    }

    /**
     * TODO, java不能这样定义
     * 改变奥义计量变化量
     */
    public void change_cd_variable(int value_1, int value_2) {

    }

    /**
     * 改变攻击时奥义计量变化量
     */
    public int change_cd_variable_attack(int value) {
        cd_attack_variable += value;
        if (cd_attack_variable < 0) {
            value = value - cd_attack_variable;
            cd_attack_variable = 0;
        }
        return value;
    }

    /**
     * 改变防御时奥义计量变化量
     */
    public int change_cd_variable_defense(int value) {
        cd_defense_variable += value;
        if (cd_defense_variable < 0) {
            value = value - cd_defense_variable;
            cd_defense_variable = 0;
        }
        return value;
    }


    /**
     * 是否群体攻击
     */
    public boolean is_group_attack() {
        return special_type == SpecialType.GroupAttack.getValue();
    }
}
