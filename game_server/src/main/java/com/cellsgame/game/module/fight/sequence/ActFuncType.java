package com.cellsgame.game.module.fight.sequence;

import com.cellsgame.game.module.fight.sequence.impl.func.*;

public enum ActFuncType {
    MOVE(1, new MoveFunc()),
    ATK(2,  new AttackFunc()),                          // 攻击
    AID(3, new AidFunc()),                              // 辅助
    MOVE_ATK(4, new MoveAttackFunc()),                  // 移动攻击
    MOVE_AID(5, new MoveAttackFunc()),                  // 移动攻击
    LEVEL_UP(6,  new LevelUpFunc()),                    // 升级
    OVER_TURN(7, new NullFunc()),                                 // 结束行动
    OVER_TIME(8,  new NullFunc()),                                // 超时
    REVIVAL(9,  new RevivalFunc()),                     // 复活
    AWARD(10,  new AwardFunc()),                        // 奖励
    ;
    private Integer code;
    private IFunc func;

    ActFuncType(Integer code, IFunc handler) {
        this.code = code;
        this.func = handler;
    }
    public Integer getCode() {
        return  code;
    }

    public IFunc getFunc() { return func; }
}
