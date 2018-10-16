package com.cellsgame.game.module.rank.cons;

import com.cellsgame.game.cache.CacheDisData;
import com.cellsgame.game.cons.ModuleID;

/**
 * @author Aly on 2017-03-16.
 */
public enum CacheDisRank implements CacheDisData {
    ADD_LIKE_RANK_TYPE(1),       // 最大点赞次数
    MAX_RANK_NUM(2),            // 排行榜最大数量
    MAX_ADD_LIKE_PRIZE_TRE(3),  // 点赞奖励 宝箱
    ;
    private int id;
    private int[] data;

    CacheDisRank(int id) {
        this.id = createId(id);
    }

    @Override
    public int getModule() {
        return ModuleID.RANK;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int[] getData() {
        return data;
    }

    @Override
    public void setData(int[] data) {
        this.data = data;
    }
}
