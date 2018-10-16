package com.cellsgame.game.module.player.cons;

import com.cellsgame.game.cache.CacheDisData;
import com.cellsgame.game.cons.ModuleID;

public enum CacheDisDataPlayer implements CacheDisData {

    FirstPayPrize(1),//首次充值奖励
    ActivenessLimit(2),//活跃度上限
    NearChatFriendLimit(3),//最近联系人 最大数量
    ModifyPlayerNameCost(4),//玩家改名消耗
    ;
    private int id;
    private int[] data;

    CacheDisDataPlayer(int id) {
        this.id = createId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    @Override
    public int getModule() {
        return ModuleID.Player;
    }
}
