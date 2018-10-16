package com.cellsgame.game.module.pay.csv;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;

public class OrderItemConfig  extends BaseCfg {

    private String itemId;

    private int orderMoney;

    private List<FuncConfig> prize;

    private List<FuncConfig> fristPrize;

    private List<FuncConfig> extraPrize;

    public List<FuncConfig> getPrize() {
        return prize;
    }

    public void setPrize(List<FuncConfig> prize) {
        this.prize = prize;
    }

    public List<FuncConfig> getFristPrize() {
        return fristPrize;
    }

    public void setFristPrize(List<FuncConfig> fristPrize) {
        this.fristPrize = fristPrize;
    }

    public List<FuncConfig> getExtraPrize() {
        return extraPrize;
    }

    public void setExtraPrize(List<FuncConfig> extraPrize) {
        this.extraPrize = extraPrize;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(int orderMoney) {
        this.orderMoney = orderMoney;
    }
}
