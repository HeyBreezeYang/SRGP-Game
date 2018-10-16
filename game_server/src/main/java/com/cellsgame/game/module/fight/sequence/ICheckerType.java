package com.cellsgame.game.module.fight.sequence;

import com.cellsgame.game.module.fight.cons.FightCons;
import com.cellsgame.game.module.fight.sequence.impl.checker.*;
import com.cellsgame.game.module.fight.sequence.impl.func.*;

import java.util.List;
import java.util.Map;

/**
 * 回合序列
 */
public class ICheckerType {
    public enum ACT_ENUM {
        MOVE(new MoveChecker()),
        ATK(new AttackChecker()),                         // 攻击
        MOVE_ATK(new MoveAttackChecker()),            // 移动攻击
        LEVEL_UP(new LevelupChecker()),                  // 升级
        OVER_TURN(new OverTurnChecker()),                                // 结束行动
        OVER_TIME(new OverTimeChecker()),                                // 超时
        REVIVAL(new RevivalChecker()),                   // 复活
        AWARD(new AwardChecker()),                         // 奖励
        ;
        private IChecker checker;

        ACT_ENUM(IChecker checker) {
            this.checker = checker;
        }

        public IChecker getChecker() {
            return checker;
        }
    }

    private static Map<String, Object> sequence;

    public static  List<Map<String, List<?>>> getTurnList() {
        if (sequence.containsKey("turns")) {
            return (List<Map<String, List<?>>>)sequence.get("turns");
        }
        return null;
    }

    public static  Map getAwardList() {
        if (sequence.containsKey("awards")) {
            return (Map)sequence.get("awards");
        }
        return null;
    }

    public static ACT_ENUM getAction(String type) {
        switch (type) {
            case FightCons.ATTACK:
                return ACT_ENUM.ATK;
            case FightCons.MOVE:
                return ACT_ENUM.MOVE;
            case FightCons.MOVE_ATTACK:
                return ACT_ENUM.MOVE_ATK;
            case FightCons.LEVEL_UP:
                return ACT_ENUM.LEVEL_UP;
            case FightCons.OVER_TURN:
                return ACT_ENUM.OVER_TURN;
            case FightCons.OVER_TIME:
                return ACT_ENUM.OVER_TIME;
            case FightCons.REVIVAL:
                return ACT_ENUM.REVIVAL;
            case FightCons.AWARD:
                return ACT_ENUM.AWARD;
        }
        return null;
    }

    public static IChecker getChecker(String type) {
        ACT_ENUM actEnum = getAction(type);
        if (null == actEnum) {
            return actEnum.getChecker();
        }
        return null;
    }
}
