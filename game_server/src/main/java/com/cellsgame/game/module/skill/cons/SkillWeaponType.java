package com.cellsgame.game.module.skill.cons;

public enum SkillWeaponType {
    SWORD("剑",1),
    GUN("枪",2),
    AXE("斧",3),
    BOW("弓",4),
    HIDDEN("暗器",5),
    BOOK("书",6),
    DRAGON_STONE("龙石",7),
    STAFF("杖",8);

    private String name;
    private Integer value;
    SkillWeaponType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}
