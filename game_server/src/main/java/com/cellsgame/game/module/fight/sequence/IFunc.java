package com.cellsgame.game.module.fight.sequence;

import com.cellsgame.game.module.fight.Battle;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

public interface IFunc {
    List<IChecker> getCheckers();
    void exec(Battle battle, Map action) throws LoginException;
}
