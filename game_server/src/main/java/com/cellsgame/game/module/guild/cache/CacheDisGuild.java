package com.cellsgame.game.module.guild.cache;

import com.cellsgame.game.cache.CacheDisData;
import com.cellsgame.game.cons.ModuleID;

/**
 * @author Aly on 2017-03-02.
 */
public enum CacheDisGuild implements CacheDisData {

    /**
     * 公会总人数
     */
    MaxMemberSize(1),
    /**
     * 精英人数 数量
     */
    RightLevel2MemberSize(2),
    /**
     * 副会长 人数数量
     */
    RightLevel1MemberSize(3),
    /**
     * 创建消耗钻石
     */
    CreateGuildCostTre(4),
    /**
     * 工会操作日志条数
     */
    GuildLogLen(5),
    /**
     * 军团长离线时长，被替换
     */
    LeaderOfflineReplace(6),
    /**
     * 修改公会名消耗
     * */
    ChangeGuildNameCost(7),
    ;

    private int id;
    private int[] data;

    CacheDisGuild(int id) {
        this.id = createId(id);
    }

    @Override
    public int getModule() {
        return ModuleID.Guild;
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
