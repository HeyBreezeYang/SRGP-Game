package com.cellsgame.game.module.fight.cons;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtType;

public enum EvtTypeFight implements EvtType {

    EnterBattle,                        // 开始战役
    EnterTurn,                          // 开始回合
    EnterCombat,                        // 开始战斗

    ExitBattle,                         // 结束战役
    ExitTurn,                           // 结束回合
    ExitBCombat,                        // 结束战斗

    ReleaseSkill,                       // 释放技能
    LevelUp,                            // 升级
    Dead,                               // 死亡（英雄)
    Destroy,                            // 破坏（障碍物，据点之类）
    FightOver,                          // 战争结束
    NextBattle,                         // 下一场战役(连续战役时)
    BattleWin,                          // 胜利
    BattleLost,                         // 失败
    ;

    private int evtCode;

    @Override
    public int getEvtCode() {
        return evtCode;
    }

    @Override
    public void setEvtCode(int code) {
        this.evtCode = code;
    }
}
