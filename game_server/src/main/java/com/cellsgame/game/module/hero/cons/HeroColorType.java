package com.cellsgame.game.module.hero.cons;

public enum HeroColorType {
    RED(1),
    GREEN(2),
    BLUE(3),
    NONE(4),
    ;


    private final int value;

    HeroColorType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
