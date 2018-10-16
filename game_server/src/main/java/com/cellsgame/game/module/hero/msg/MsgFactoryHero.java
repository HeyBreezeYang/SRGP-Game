package com.cellsgame.game.module.hero.msg;

import com.cellsgame.game.module.MsgFactory;

public class MsgFactoryHero extends MsgFactory {
    public static final String HERO = "h";
    public static final String HERO_ID = "hid";
    public static final String HERO_GROUP = "hg";


    private static final MsgFactoryHero instance = new MsgFactoryHero();


    public static MsgFactoryHero instance() {
        return instance;
    }


    @Override
    public String getModulePrefix() {
        return HERO;
    }
}
