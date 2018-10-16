package com.cellsgame.game.module.guild.csv;

import java.util.List;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

/**
 * @author Aly on  2016-08-22.
 *         捐献配置
 */
public class DonateConfig extends BaseCfg {

    private FuncConfig cost;

    private List<FuncConfig> prize;  // 奖励

    private int guildExpPrize;

    private int guildMoneyPrize;

    public FuncConfig getCost() {
        return cost;
    }

    public void setCost(FuncConfig cost) {
        this.cost = cost;
    }

    public List<FuncConfig> getPrize() {
        return prize;
    }

    public void setPrize(List<FuncConfig> prize) {
        this.prize = prize;
    }

    public int getGuildExpPrize() {
        return guildExpPrize;
    }

    public void setGuildExpPrize(int guildExpPrize) {
        this.guildExpPrize = guildExpPrize;
    }

    public int getGuildMoneyPrize() {
        return guildMoneyPrize;
    }

    public void setGuildMoneyPrize(int guildMoneyPrize) {
        this.guildMoneyPrize = guildMoneyPrize;
    }
}