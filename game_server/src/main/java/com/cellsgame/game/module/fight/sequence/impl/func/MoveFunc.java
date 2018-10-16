package com.cellsgame.game.module.fight.sequence.impl.func;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.fight.*;
import com.cellsgame.game.module.fight.impl.HeroEntity;
import com.cellsgame.game.module.fight.sequence.IChecker;
import com.cellsgame.game.module.fight.sequence.IFunc;

import java.util.List;
import java.util.Map;

public class MoveFunc implements IFunc {
    @Override
    public List<IChecker> getCheckers() {
        return null;
    }

    @Override
    public void exec(Battle battle, Map action) throws LogicException {
        List<IChecker> checkers = getCheckers();
        for (IChecker checker: checkers) {
            checker.check(battle, action);
        }

        Integer src = (Integer)action.get("src");
        HeroEntity actor = battle.getHeroById(src);

        List<Integer> pos = (List)action.get("pos");

        actor.setY(pos.get(0));
        actor.setX(pos.get(1));
    }
}
