package com.cellsgame.game.module.fight.cons;

public enum TargetType {
    SELF(1),
    RIVAL(2),
    BUILDING(3),    // 阻挡，据点
    ;

    private int value;

    TargetType(int v) {value = v;}

    public int getValue() {
        return value;
    }
}
