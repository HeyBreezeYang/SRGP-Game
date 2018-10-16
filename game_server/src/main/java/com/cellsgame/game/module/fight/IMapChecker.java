package com.cellsgame.game.module.fight;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.fight.impl.HeroEntity;
import com.cellsgame.game.module.fight.impl.MapEntity;

public interface IMapChecker {
    void check(MapEntity map, HeroEntity hero, Integer i,Integer j) throws LogicException;
}
