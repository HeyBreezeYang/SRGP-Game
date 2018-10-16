package com.cellsgame.game.module.activity.csv;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;
import java.util.Map;

public class ActivityRankPrizeConfig extends BaseCfg {

    public static Map<Integer, List<ActivityRankPrizeConfig>> configs = GameUtil.createSimpleMap();

    private int groupId;

    private int rank;

    private List<FuncConfig> prizes;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<FuncConfig> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<FuncConfig> prizes) {
        this.prizes = prizes;
    }
}
