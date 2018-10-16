package com.cellsgame.game.module.hero.cons;

public enum HeroWeaponType {
    SWORD(1),   //剑
    AXE(2),     //斧头
    GUN(3),     //枪
    BOW(4),     //弓
    HIDDEN_WEAPON(5),   //暗器
    THE_MAGIC_BOOK(6),  //魔法书
    LONG_YAN(7),
    ROD(8);      //杖



    private final int value;

    HeroWeaponType(int i) {
        this.value = i;
    }
}
