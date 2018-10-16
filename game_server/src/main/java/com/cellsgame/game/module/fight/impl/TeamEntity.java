package com.cellsgame.game.module.fight.impl;

import com.cellsgame.common.util.GameUtil;

import java.util.Set;

/**
 * 队伍
 */
public class TeamEntity {
    private Set<HeroEntity> heros = GameUtil.createSet();

    public Integer getHeroNum() {
        return heros.size();
    }

    public Set<HeroEntity> getHeros() {
        return heros;
    }

    public HeroEntity getHeroById(Integer id) {
        return null;
    }
}
