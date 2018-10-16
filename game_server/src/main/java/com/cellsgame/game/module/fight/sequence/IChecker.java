package com.cellsgame.game.module.fight.sequence;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.fight.Battle;

import java.util.Map;

public interface IChecker {
    void check(Battle battle, Map action) throws LogicException;
}
