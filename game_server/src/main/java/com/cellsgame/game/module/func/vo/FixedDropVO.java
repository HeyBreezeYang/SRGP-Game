package com.cellsgame.game.module.func.vo;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;

/**
 * Created with game002
 * User: 阚庆忠
 * Date: 2016/8/24 17:37
 */
public class FixedDropVO extends DBVO {

    /**
     * DBID
     */
    private int dbid;
    /**
     * 玩家ID
     */
    private int pid;

    private Map<Integer, Integer> triggerCountMap;

    private Map<Integer, Integer> dropCountMap;

    @Override
    protected Object getPrimaryKey() {
        return dbid;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        dbid = (int) pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{pid};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys != null && relationKeys.length > 0)
            pid = (int) relationKeys[0];
    }

    @Override
    protected void init() {
        triggerCountMap = GameUtil.createSimpleMap();
        dropCountMap = GameUtil.createSimpleMap();
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {
    }

    public int getDbid() {
        return dbid;
    }

    public void setDbid(int dbid) {
        this.dbid = dbid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public Map<Integer, Integer> getTriggerCountMap() {
        return triggerCountMap;
    }

    public void setTriggerCountMap(Map<Integer, Integer> triggerCountMap) {
        this.triggerCountMap = triggerCountMap;
    }

    public Map<Integer, Integer> getDropCountMap() {
        return dropCountMap;
    }

    public void setDropCountMap(Map<Integer, Integer> dropCountMap) {
        this.dropCountMap = dropCountMap;
    }
}
