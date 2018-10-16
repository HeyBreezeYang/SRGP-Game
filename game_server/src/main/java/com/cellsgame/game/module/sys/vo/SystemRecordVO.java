package com.cellsgame.game.module.sys.vo;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class SystemRecordVO extends DBVO {

    private int id;

    @Save(ix = 1)
    private Map<Integer, Long> recordDate;


    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = (int) pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
    }

    @Override
    protected void init() {
        recordDate = GameUtil.createSimpleMap();
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {
    }

    public Map<Integer, Long> getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Map<Integer, Long> recordDate) {
        this.recordDate = recordDate;
    }
}
