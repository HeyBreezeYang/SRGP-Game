package com.cellsgame.game.module.fight.sequence.impl.checker;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.fight.Battle;
import com.cellsgame.game.module.fight.sequence.IChecker;

import java.util.Map;

/**
 * 辅助检查
 */
public class AidChecker implements IChecker {
    @Override
    public void check(Battle battle, Map action) throws LogicException {
        // 攻击距离检查
        // 检查阻挡
    }
}
