package com.cellsgame.game.module.hero.cons;

public enum HeroMoveType {
    INFANTRY(1),    // 步兵
    HEAVY_ARMOR(2), // 重甲
    CAVALRY(3),     // 骑兵
    FLYING_ARMS(4); // 飞行兵


    private final int value;

    HeroMoveType(int i) {
        this.value = i;
    }
}
