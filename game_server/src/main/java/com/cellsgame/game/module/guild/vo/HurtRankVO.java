package com.cellsgame.game.module.guild.vo;

import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class HurtRankVO extends DBVO {

    @Save(ix = 1)
    private int playerId;

    @Save(ix = 2)
    private long hurt;

    @Save(ix = 3)
    private long score;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public long getHurt() {
        return hurt;
    }

    public void setHurt(long hurt) {
        this.hurt = hurt;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    @Override
    protected Object getPrimaryKey() {
        return null;
    }

    @Override
    protected void setPrimaryKey(Object o) {

    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[0];
    }

    @Override
    protected void setRelationKeys(Object[] objects) {

    }

    @Override
    protected void init() {

    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer integer) {

    }
}
