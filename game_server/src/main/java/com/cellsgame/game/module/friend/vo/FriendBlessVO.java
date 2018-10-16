package com.cellsgame.game.module.friend.vo;

import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class FriendBlessVO extends DBVO {
    private int id;
    private int pid;
    @Save(ix = 1)
    private int blessNum;           // 祝福次数
    @Save(ix = 2)
    private int blessedNum;         // 被祝福次数
    @Save(ix = 3)
    private long lastRestTime;      // 上次重置时间
    @Save(ix = 4)
    private int prizeNum;           // 领取奖励的次数
    @Save(ix = 5)
    private long lastPrizeTime;     // 上次领奖时间


    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        this.id = (int) pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{pid};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        pid = (int) relationKeys[0];
    }

    @Override
    protected void init() {
        pid = 0;
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getBlessNum() {
        return blessNum;
    }

    public void setBlessNum(int blessNum) {
        this.blessNum = blessNum;
    }

    public int getBlessedNum() {
        return blessedNum;
    }

    public void setBlessedNum(int blessedNum) {
        this.blessedNum = blessedNum;
    }

    public long getLastRestTime() {
        return lastRestTime;
    }

    public void setLastRestTime(long lastRestTime) {
        this.lastRestTime = lastRestTime;
    }

    public int getPrizeNum() {
        return prizeNum;
    }

    public void setPrizeNum(int prizeNum) {
        this.prizeNum = prizeNum;
    }

    public long getLastPrizeTime() {
        return lastPrizeTime;
    }

    public void setLastPrizeTime(long lastPrizeTime) {
        this.lastPrizeTime = lastPrizeTime;
    }
}
