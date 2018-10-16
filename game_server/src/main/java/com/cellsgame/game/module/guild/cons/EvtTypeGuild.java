package com.cellsgame.game.module.guild.cons;

import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;

/**
 * aly @ 16-11-8.
 */
public enum EvtTypeGuild implements EvtType {
    /**
     * 捐献
     * @see EvtParamType#GUILD_ID
     * @see EvtParamType#GUILD_NAME
     * @see EvtParamType#CID  捐献配置
     */
    Donate,
    /**
     * 加入公会
     *
     * @see EvtParamType#Right_Level
     * @see EvtParamType#PlayerInfoVO
     */
    Join,
    /**
     * 资金变动
     * */
    MoneyChange,
    ExpChange,
    OpenBoss,
    KillBoss,
    /**
     * 攻击BOSS
     *
     * @see EvtParamType#LONG_VAL  伤害
     */
    FightBoss, 
    LV_UP,
    /**
     * 创建
     * @see EvtParamType#GUILD
     */
    Create,
    /**
     * 解散
     * @see EvtParamType#GUILD
     */
    Dissolution,
    /**
     * 增加经验
     * @see EvtParamType#NUM
     */
    AddExp,
    ;
    private static final AtomicInteger inr = new AtomicInteger(ModuleID.Guild);

    static {
        EvtType[] values = values();
        for (EvtType eType : values) {
            eType.setEvtCode(inr.incrementAndGet());
        }
    }

    private int code;


    @Override
    public int getEvtCode() {
        return code;
    }

    @Override
    public void setEvtCode(int code) {
        this.code = code;
    }
}
