package com.cellsgame.game.module.activity.vo;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RankDataVO extends DBVO {

    @Save(ix = 1)
    private int rankItemKey;

    @Save(ix = 2)
    private String rankItemName;

    @Save(ix = 3)
    private long value;

    @Save(ix = 4)
    private long updateTime;

    @Save(ix = 5)
    private Map<Integer, Long> detailValues;

    @Save(ix = 6)
    private Set<Integer> recordPlayer;

    public int getRankItemKey() {
        return rankItemKey;
    }

    public void setRankItemKey(int rankItemKey) {
        this.rankItemKey = rankItemKey;
    }

    public String getRankItemName() {
        return rankItemName;
    }

    public void setRankItemName(String rankItemName) {
        this.rankItemName = rankItemName;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Map<Integer, Long> getDetailValues() {
        return detailValues;
    }

    public void setDetailValues(Map<Integer, Long> detailValues) {
        this.detailValues = detailValues;
    }

    public Set<Integer> getRecordPlayer() {
        return recordPlayer;
    }

    public void setRecordPlayer(Set<Integer> recordPlayer) {
        this.recordPlayer = recordPlayer;
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
        detailValues = GameUtil.createSimpleMap();
        recordPlayer = new HashSet<>();
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer integer) {

    }
}
