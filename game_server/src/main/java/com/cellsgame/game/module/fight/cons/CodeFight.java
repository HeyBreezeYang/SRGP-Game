package com.cellsgame.game.module.fight.cons;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

public enum CodeFight implements ICode {
    NO_ACT(1),                          // 不存在的行动
    ACT_PARAM_ERROR(2),                 // 行动参数错误
    ACT_NO_HERO(3),                     // 英雄不存在
    MOVE_RANGE_LIMIT(4),                // 移动范围限制
    ;
    private int code;

    CodeFight(int code) {
        this.code = code;
    }

    @Override
    public int getModule() {
        return ModuleID.Fight;
    }

    @Override
    public int getCode() {
        return code;
    }
}
