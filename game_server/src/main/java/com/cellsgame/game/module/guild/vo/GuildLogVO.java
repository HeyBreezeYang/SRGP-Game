package com.cellsgame.game.module.guild.vo;

import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * @author Aly on 2017-03-09.
 */
public class GuildLogVO extends DBVO {
    @Save(ix = 1)
    private int logID;
    @Save(ix = 2)
    private String[] param;

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    @Override
    protected Object initPrimaryKey() {
        return 0;
    }

    @Override
    protected Object getPrimaryKey() {
        return 0;
    }

    @Override
    protected void setPrimaryKey(Object o) {

    }

    @Override
    protected Object[] getRelationKeys() {
        return null;
    }

    @Override
    protected void setRelationKeys(Object[] objects) {
    }

    @Override
    protected void init() {
        param = new String[0];

    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer integer) {

    }
}
