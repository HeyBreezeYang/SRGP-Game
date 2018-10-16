package com.cellsgame.game.module.hero.cons;

public enum HeroType {
    HERO(1),
    MONSTER(2),
    ;

    private int value;
    HeroType(int v) { value = v; }

    public int getValue() {
        return value;
    }
}
