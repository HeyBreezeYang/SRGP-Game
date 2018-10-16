package com.cellsgame.game.module.fight.skill.impl;

import com.cellsgame.game.module.skill.csv.SkillSealCfg;
import com.cellsgame.game.module.skill.csv.SkillSpecialCfg;

import java.util.Set;

public class SkillSeal extends SkillBase {
    private int pre_id;
    private int post_id;

    public SkillSeal(Integer skillId, Integer actorId, Set<Integer> to_actors) {
        super(skillId, actorId, to_actors);
    }


    public void ctor(SkillSealCfg cfg) {
        pre_id = cfg.getPre_id();
        post_id = cfg.getRear_id();
    }
}
