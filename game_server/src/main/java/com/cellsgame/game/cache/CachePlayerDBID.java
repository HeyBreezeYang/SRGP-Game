package com.cellsgame.game.cache;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.Bootstrap;
import com.cellsgame.game.context.GameConfig;
import com.cellsgame.game.util.DebugTool;
import com.cellsgame.orm.BaseDAO;

import java.util.concurrent.atomic.AtomicInteger;

public enum CachePlayerDBID {
    PLAYER_DBID("playerDAO"),
    GUILD_DBID("guildDAO"),
    //
    ;
    private AtomicInteger ID;
    private String dao;

    CachePlayerDBID(String dao) {
        this.dao = dao;
    }

    public static void init() {
        for (CachePlayerDBID cacheDBID : CachePlayerDBID.values()) {
            if (cacheDBID.dao == null || cacheDBID.dao.length() <= 0)
                continue;
            try {
                int maxValue = SpringBeanFactory.getBean(cacheDBID.dao, BaseDAO.class).getMaxKey().intValue();
                if(maxValue <= 0) maxValue = GameConfig.getConfig().getGameServerId() * 1000000;
                cacheDBID.ID = new AtomicInteger(maxValue);
            } catch (Exception e) {
                DebugTool.throwException(" DAO:[" + cacheDBID.dao + "]  -> init error:", e);
            }
        }
    }

    public int incrementAndGet() {
        return ID.incrementAndGet();
    }

    public int get() {
        return ID.get();
    }
}
