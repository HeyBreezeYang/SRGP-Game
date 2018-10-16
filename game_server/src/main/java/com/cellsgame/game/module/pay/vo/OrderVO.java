package com.cellsgame.game.module.pay.vo;

import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;
import com.cellsgame.pay.IOrder;

public class OrderVO extends DBVO implements IOrder {

    private String orderId;

    private int playerId;

    @Save(ix = 1)
    private String itemId;

    @Save(ix = 2)
    private int mny;

    @Save(ix = 3)
    private boolean prize;


    @Override
    protected Object initPrimaryKey() {
        return "";
    }

    @Override
    public String getPrimaryKey() {
        return orderId;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        orderId = pk.toString();
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{playerId};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        this.playerId = (int) relationKeys[0];
    }

    @Override
    protected void init() {

    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getMny() {
        return mny;
    }

    public void setMny(int mny) {
        this.mny = mny;
    }

    public boolean isPrize() {
        return prize;
    }

    public void setPrize(boolean prize) {
        this.prize = prize;
    }
}
