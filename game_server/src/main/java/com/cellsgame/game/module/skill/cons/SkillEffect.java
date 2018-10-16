package com.cellsgame.game.module.skill.cons;

/**
 * 技能效果类型枚举
 */
public enum SkillEffect {
    A(1),
    B(2),
    C(3),
    WEAPON(4),
    SUPPORT(5),
    AOYI(6),
    SHENGYIN(7)
    ;


    private final int value;

    SkillEffect(int i) {
        this.value = i;
    }
}
