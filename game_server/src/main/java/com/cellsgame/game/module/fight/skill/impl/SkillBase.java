package com.cellsgame.game.module.fight.skill.impl;

import com.cellsgame.game.module.fight.impl.HeroEntity;

import java.util.List;
import java.util.Set;

public class SkillBase {
    private int skillId;

    private int cid;

    private HeroEntity actor;
    private Set<HeroEntity> to_actors;

    public SkillBase(Integer skillId, Integer actorId, Set<Integer> to_actors) {
        //
    }

    public void release() {
        actor = null;
        to_actors.clear();
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
