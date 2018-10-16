package com.cellsgame.game.module.fight.sequence;

import com.cellsgame.game.module.fight.cons.FightCons;
import com.cellsgame.game.module.fight.sequence.impl.func.*;

import java.util.List;
import java.util.Map;

/**
 * 战斗序列
 */
public class ActSequence {
    private Map<String, Object> sequence;

    public List<Map<String, List<?>>> getTurnList() {
        if (sequence.containsKey("turns")) {
            return (List<Map<String, List<?>>>)sequence.get("turns");
        }
        return null;
    }

    public Map getAwardList() {
        if (sequence.containsKey("awards")) {
            return (Map)sequence.get("awards");
        }
        return null;
    }

    public static ActFuncType getActFunc(String type) {
        switch (type) {
            case FightCons.ATTACK:
                return ActFuncType.ATK;
            case FightCons.AID:
                return ActFuncType.AID;
            case FightCons.MOVE:
                return ActFuncType.MOVE;
            case FightCons.MOVE_ATTACK:
                return ActFuncType.MOVE_ATK;
            case FightCons.MOVE_AID:
                return ActFuncType.MOVE_AID;
            case FightCons.LEVEL_UP:
                return ActFuncType.LEVEL_UP;
            case FightCons.OVER_TURN:
                return ActFuncType.OVER_TURN;
            case FightCons.OVER_TIME:
                return ActFuncType.OVER_TIME;
            case FightCons.REVIVAL:
                return ActFuncType.REVIVAL;
            case FightCons.AWARD:
                return ActFuncType.AWARD;
        }
        return null;
    }
}
