package com.cellsgame.game.module.card.csv;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;

public class CardConfig extends BaseCfg {

    private int expiryDate;

    private List<FuncConfig> dayPrize;

    private int buyApDiscount;

    private int expeditionNum;
    
    private int type;

    public int getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(int expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<FuncConfig> getDayPrize() {
        return dayPrize;
    }

    public void setDayPrize(List<FuncConfig> dayPrize) {
        this.dayPrize = dayPrize;
    }

    public int getBuyApDiscount() {
        return buyApDiscount;
    }

    public void setBuyApDiscount(int buyApDiscount) {
        this.buyApDiscount = buyApDiscount;
    }

    public int getExpeditionNum() {
        return expeditionNum;
    }

    public void setExpeditionNum(int expeditionNum) {
        this.expeditionNum = expeditionNum;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
