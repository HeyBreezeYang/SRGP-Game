package com.cellsgame.game.module.fight.sequence.impl.func;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.fight.Battle;
import com.cellsgame.game.module.fight.sequence.IChecker;
import com.cellsgame.game.module.fight.sequence.ICheckerType;
import com.cellsgame.game.module.fight.sequence.IFunc;

import java.util.List;
import java.util.Map;

public class MoveAttackFunc implements IFunc {
    List<IChecker> checkers;
    @Override
    public List<IChecker> getCheckers() {
        if (null == checkers) {
            checkers = GameUtil.createList();
            checkers.add(ICheckerType.ACT_ENUM.MOVE.getChecker());
            checkers.add(ICheckerType.ACT_ENUM.ATK.getChecker());
        }
        return checkers;
    }

    @Override
    public void exec(Battle battle, Map action) throws LogicException {

    }
}
