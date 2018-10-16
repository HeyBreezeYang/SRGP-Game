package com.cellsgame.game.module.chat.vo;

import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class ChatMsgVO extends DBVO {

    @Save(ix = 1)
    private int playerId;
    @Save(ix = 2)
    private String msg;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    protected Object initPrimaryKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Object getPrimaryKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        // TODO Auto-generated method stub

    }

    @Override
    protected Object[] getRelationKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void init() {
        playerId = 0;
    }

    @Override
    public Integer getCid() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCid(Integer cid) {
        // TODO Auto-generated method stub

    }


}
