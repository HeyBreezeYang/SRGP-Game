package com.cellsgame.game.module.friend.cons;

import com.cellsgame.game.cache.CacheDisData;
import com.cellsgame.game.cons.ModuleID;

/**
 * @author Aly @2017-02-08.
 */
public enum CacheDisDataFriend implements CacheDisData {
    // 好友名单 容量大小
    FRIEND_LIST_SIZE(1),
    // 黑名单 容量大小
    BLACK_LIST_SIZE(2),
    // 推荐好友 等級上下限
    RECOMMENDED_RANGE(3),
    // 推荐好友数量
    RECOMMENDED_SIZE(4),

    BLESSING_NUM(5),                // 每日祝福次数
    BLESSED_NUM(6),                 // 每日被祝福次数
    BLESS_PRIZE_NUM(7),             // 每日祝福领奖次数
    BLESS_GET_PRIZE_TIME(8),        // 每日多少点以后可以领奖
    BLESS_GET_PRIZE_INTERVAL(9),    // 奖励领取时间间隔
    // 10 客户端显示被祝福比例
    ;

    private int id;
    private int[] data;

    CacheDisDataFriend(int id) {
        this.id = createId(id);
    }

    @Override
    public int getModule() {
        return ModuleID.Friends;
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
