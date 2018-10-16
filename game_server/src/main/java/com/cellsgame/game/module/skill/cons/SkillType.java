package com.cellsgame.game.module.skill.cons;

/**
 * 技能目标枚举
 */
public enum SkillType {
    WEAPON(1),
    SUPPORT(2),
    SPECIAL(3),
    A(4),
    B(5),
    C(6),
    SEAL(7);

    private int value;

    SkillType(Integer v) {
        value = v;
    }

    public int getValue() {
        return value;
    }
}
