package com.cellsgame.game.module.hero.cons;

public enum HeroSkillType {
    A(1),
    B(2),
    C(3),
    WEAPON(4),
    SUPPORT(5),
    AOYI(6),
    SHENGYIN(7)
    ;


    private final int value;

    HeroSkillType(int i) {
        this.value = i;
    }
}
