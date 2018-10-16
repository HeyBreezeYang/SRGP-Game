package com.cellsgame.game.module.player.cons;

import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;

public enum EvtTypePlayer implements EvtType {
    CacheTimeOut,
    PI_Offline,
    /**
     * 玩家进入游戏
     */
    EnterGame,
    PlayerLevelUp,
    BattleLevelUp,
    VipLevelUp,
    BuyMoney,// 购买金币
    /**
     * 玩家离线
     */
    Offline,
    /**
     * 玩家加载成功
     */
    LoadSuc,
    /**
     * 玩家创建玩家成功
     */
    CreatePlayer,
    /**
     * 离开公会
     */
    LeaveGuild,
    /**
     * 重置
     *
     * @see EvtParamType#SYS_TYPE 系统类型
     */
    Reset,
    /**
     * 奖励
     *
     * @see EvtParamType#SYS_TYPE 系统类型
     * @see EvtParamType#PRIZE 奖励
     */
    Prize,
    /**
     * 折扣事件
     *
     * @see EvtParamType#SYS_TYPE 系统类型
     * @see EvtParamType#PRIZE 价格
     */
    Discount,


    /**
     * 等级升级
     */
    LevelUp,

    /**
     * 技能突破
     */
    SkillBreak,

    /**
     * 新完成剧情
     */
    FinishPlot,

    Worship,
    ACTIVENESS,
    PARTY_SCORE,

    /**
     * 实力变化
     *
     * @see EvtParamType#BEFORE
     * @see EvtParamType#AFTER
     */
    FightForce,
    CheckIn,
    OnlineSize
    ;
    private static final AtomicInteger incr = new AtomicInteger(ModuleID.Player);

    static {
        EvtType[] values = values();
        for (EvtType eType : values) {
            eType.setEvtCode(incr.incrementAndGet());
        }
    }

    private int evtCode;

    @Override
    public int getEvtCode() {
        return evtCode;
    }
    @Override
    public void setEvtCode(int evtCode) {
        this.evtCode = evtCode;
    }

}
