package com.cellsgame.game.module.card.vo;

import com.cellsgame.game.core.cfg.base.CfgSaveVO;
import com.cellsgame.game.module.card.csv.CardConfig;
import com.cellsgame.orm.enhanced.annotation.Save;

public class CardVO  extends CfgSaveVO<CardConfig> {

    private int id;
    private int cid;
    private int playerId;

    @Save(ix = 1)
    private int endDate;

    @Save(ix = 2)
    private int prizeDate;


    public CardVO() {
        super(CardConfig.class);
    }

    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = Integer.parseInt(pk.toString());
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
        return cid;
    }

    @Override
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPrizeDate() {
        return prizeDate;
    }

    public void setPrizeDate(int prizeDate) {
        this.prizeDate = prizeDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }
}
