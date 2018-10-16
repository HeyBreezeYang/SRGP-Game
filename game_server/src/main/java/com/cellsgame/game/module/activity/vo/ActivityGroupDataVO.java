package com.cellsgame.game.module.activity.vo;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ActivityGroupDataVO extends DBVO {

    private int id;

    private String activityId;

    @Save(ix = 1)
    private int group;

    @Save(ix = 2)
    private Map<Integer, RankDataVO> rankDataVOMap;

    //-------- 逻辑数据-----------
    private List<RankDataVO> ranks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public Map<Integer, RankDataVO> getRankDataVOMap() {
        return rankDataVOMap;
    }

    public void setRankDataVOMap(Map<Integer, RankDataVO> rankDataVOMap) {
        this.rankDataVOMap = rankDataVOMap;
    }

    public List<RankDataVO> getRanks() {
        return ranks;
    }

    public void setRanks(List<RankDataVO> ranks) {
        this.ranks = ranks;
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
        return new Object[]{activityId};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys != null && relationKeys.length > 0)
            activityId = (String) relationKeys[0];
    }

    @Override
    protected void init() {
        rankDataVOMap = GameUtil.createSimpleMap();
        ranks = GameUtil.createList();
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {

    }

    private static Comparator<RankDataVO> comparable = new Comparator<RankDataVO>() {
        @Override
        public int compare(RankDataVO o1, RankDataVO o2) {
            if(o2.getValue() != o1.getValue()){
                if(o1.getValue() > o2.getValue()){
                    return -1;
                }else{
                    return 1;
                }
            }
            else
                return (int) (o1.getUpdateTime() - o2.getUpdateTime());
        }
    };

    public void initRanks() {
        for (RankDataVO rankDataVO : rankDataVOMap.values()) {
            ranks.add(rankDataVO);
        }
        ranks.sort(comparable);
    }

    public void sortRanks() {
        ranks.sort(comparable);
    }
}
