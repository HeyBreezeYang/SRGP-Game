package com.cellsgame.game.module.hero.cons;

/**
 * 性别
 */
public enum HeroGenderType {
    WOMEN(0),
    MAN(1),
    ;

    private final int value;

    HeroGenderType(int i) {
        this.value = i;
    }
}
