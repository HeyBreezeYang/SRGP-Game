package com.cellsgame.game.module.guild.csv;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;

public class GuildBossConfig extends BaseCfg {

    // 需求公会等级
    private int reqGuildLevel;

    // 开启消耗公会资金
    private int costGuildMoney;

    // 开启消耗元宝
    private int costTre;

    // boss血量
    private long bossLf;

    // 单次攻击贡献奖励基数
    private int baseFightPrize;

    // 击杀奖励
    private int killPrize;

    // 击杀奖励-联盟资金奖励{killGuildMoneyPrize,int}
    private int killGuildMoneyPrize;


    public int getReqGuildLevel() {
        return reqGuildLevel;
    }

    public void setReqGuildLevel(int reqGuildLevel) {
        this.reqGuildLevel = reqGuildLevel;
    }

    public int getCostGuildMoney() {
        return costGuildMoney;
    }

    public void setCostGuildMoney(int costGuildMoney) {
        this.costGuildMoney = costGuildMoney;
    }

    public long getBossLf() {
        return bossLf;
    }

    public void setBossLf(long bossLf) {
        this.bossLf = bossLf;
    }

    public int getBaseFightPrize() {
        return baseFightPrize;
    }

    public void setBaseFightPrize(int baseFightPrize) {
        this.baseFightPrize = baseFightPrize;
    }

    public int getKillPrize() {
        return killPrize;
    }

    public void setKillPrize(int killPrize) {
        this.killPrize = killPrize;
    }

    public int getCostTre() {
        return costTre;
    }

    public void setCostTre(int costTre) {
        this.costTre = costTre;
    }

    public int getKillGuildMoneyPrize() {

        return killGuildMoneyPrize;
    }

    public void setKillGuildMoneyPrize(int killGuildMoneyPrize) {
        this.killGuildMoneyPrize = killGuildMoneyPrize;
    }
}
