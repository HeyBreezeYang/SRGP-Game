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

public class AidFunc implements IFunc {
    private static final Logger log = LoggerFactory.getLogger(AidFunc.class);

    @Override
    public List<IChecker> getCheckers() {
        return null;
    }

    @Override
    public void exec(Battle battle, Map action) throws LoginException {

        List<IChecker> checkers = getCheckers();
        for (IChecker checker: checkers) {
            checker.check(battle, action);
        }


        Integer src = (Integer)action.get("src");
        Integer target = (Integer)action.get("target");
        Integer target_type = (Integer)action.get("target_type");

        if (TargetType.BUILDING.getValue() == target_type) {
            log.info("还没有实现打建筑物");
        } else {
            HeroEntity attacker = battle.getHeroById(src);
            HeroEntity defender = battle.getHeroById(target);
            CombatManager.doAtkAction(battle, attacker, defender);
        }
    }
}
