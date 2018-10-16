package com.cellsgame.game.module.fight.sequence.impl.func;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.fight.Battle;
import com.cellsgame.game.module.fight.sequence.IChecker;
import com.cellsgame.game.module.fight.sequence.IFunc;

import java.util.List;
import java.util.Map;

public class RevivalFunc implements IFunc {
    @Override
    public List<IChecker> getCheckers() {
        return null;
    }

    @Override
    public void exec(Battle battle, Map action) throws LogicException {
    }
}
