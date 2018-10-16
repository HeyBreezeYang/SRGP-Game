package com.cellsgame.game.module.fight.skill.impl;

import com.cellsgame.game.module.skill.csv.SkillSpecialCfg;
import com.cellsgame.game.module.skill.csv.SkillSupportCfg;

import java.util.Set;

public class SkillSupport extends SkillBase {
    public enum SupportType {
        Heal(1),
        Reposition(2),
        Shove(3),
        Swap(4),
        Pivot(5),
        Smite(6),
        DrawBack(7),
        Reaction(8),
        Rally(9),
        ReciprocalAid(10),
        HarshCommand(11),
        ;
        private int value;
        SupportType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    private int support_type;
    private int range;

    public SkillSupport(Integer skillId, Integer actorId, Set<Integer> to_actors) {
        super(skillId, actorId, to_actors);
    }

    // TODO 模拟lua构造函数
    public void ctor(SkillSupportCfg cfg) {
        support_type = cfg.getAid_type();
        range = cfg.getRange();
    }

   public boolean is_movement() {
        if (support_type == SupportType.Reposition.getValue() ||
        support_type == SupportType.Shove.getValue() ||
        support_type == SupportType.Swap.getValue() ||
        support_type == SupportType.Pivot.getValue() ||
        support_type == SupportType.Smite.getValue() ||
        support_type == SupportType.DrawBack.getValue()) {
            return true;
        }
        return false;
   }
}
