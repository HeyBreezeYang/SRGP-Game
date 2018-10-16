package com.cellsgame.game.module.player.csv;

import java.util.List;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

/**
 * File Description.
 *
 * @author Yang
 */
public class CheckInPrizeConfig extends BaseCfg {
    private int days;
    private List<FuncConfig> prizes;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public List<FuncConfig> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<FuncConfig> prizes) {
        this.prizes = prizes;
    }
}
