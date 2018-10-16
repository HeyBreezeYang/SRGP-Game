package com.cellsgame.game.module.fight.sequence.impl.checker;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.fight.Battle;
import com.cellsgame.game.module.fight.cons.CodeFight;
import com.cellsgame.game.module.fight.impl.HeroEntity;
import com.cellsgame.game.module.fight.sequence.IChecker;

import java.util.List;
import java.util.Map;

/**
 * 英雄移动检查
 */
public class MoveChecker implements IChecker {
    @Override
    public void check(Battle battle, Map action) throws LogicException {
        Integer src = (Integer)action.get("src");
        List<Integer> pos = (List)action.get("pos");

        HeroEntity actor = battle.getHeroById(src);
        CodeFight.ACT_NO_HERO.throwIfTrue(null == actor);
        CodeFight.MOVE_RANGE_LIMIT.throwIfTrue(true);

        CodeFight.NO_ACT.throwIfTrue(!battle.getaStar().checkPath(actor.getX(),actor.getY(), pos.get(0), pos.get(1)));
    }
}
