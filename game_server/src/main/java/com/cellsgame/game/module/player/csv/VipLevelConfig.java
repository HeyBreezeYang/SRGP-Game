package com.cellsgame.game.module.player.csv;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;
import java.util.Map;

/**
 * File Description.
 *
 * @author Yang
 */
public class VipLevelConfig extends BaseCfg {

    public static VipLevelConfig[] Configs;

    private int level;
    private int exp;
    private List<FuncConfig> prizes;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public List<FuncConfig> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<FuncConfig> prizes) {
        this.prizes = prizes;
    }
}
