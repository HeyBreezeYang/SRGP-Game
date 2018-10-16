package com.cellsgame.game.module.fight.sequence.impl.func;

import com.cellsgame.game.module.fight.Battle;
import com.cellsgame.game.module.fight.CombatManager;
import com.cellsgame.game.module.fight.cons.TargetType;
import com.cellsgame.game.module.fight.impl.HeroEntity;
import com.cellsgame.game.module.fight.sequence.IChecker;
import com.cellsgame.game.module.fight.sequence.IFunc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

public class NullFunc implements IFunc {
    private static final Logger log = LoggerFactory.getLogger(NullFunc.class);

    @Override
    public List<IChecker> getCheckers() {
        return null;
    }

    @Override
    public void exec(Battle battle, Map action) throws LoginException {
    }
}
