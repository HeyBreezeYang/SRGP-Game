package com.cellsgame.game.quartz;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import com.cellsgame.game.cons.SYSCons;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.module.guild.bo.GuildBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.store.bo.StoreBO;

/**
 * @author Aly on  2016-08-16.
 */
public class HalfHourJob implements GameRefreshJob {
    private GuildBO guildBO;
    private StoreBO storeBO;
//    private BossBO bossBO;

    public void execute() {
        Calendar calendar = Calendar.getInstance();
        Dispatch.dispatchGameLogic(new Runnable() {
            @Override
            public void run() {

                LocalTime time = LocalTime.now(ZoneId.systemDefault());
                int briefNow = time.getHour() * 100 + time.getMinute();
                guildBO.clearGuildReq();
                guildBO.checkInDissolutionGuild();
                guildBO.updateGuildFightForce();
//                bossBO.checkBossStart();
                // 半小时更新一次 公会战力
                // 放在排行榜中 被动更新
//                guildBO.updateGuildFightForce();
                //
                boolean system = SYSCons.isSystemJob(time);
                // 每小时任务
                if (SYSCons.isHourJob(time)) doHourJob(briefNow, system);
            }
        });
    }


    /**
     * 每小时任务
     *
     * @param briefNow      当前时间简要表示(hour * 100 + minute)
     * @param systemRefresh 是否是系统刷新时间
     */
    @Override
    public void doHourJob(int briefNow, boolean systemRefresh) {
        CachePlayer.clearCache();
        storeBO.systemRef();
    }

    /**
     * 每分钟任务
     *
     * @param briefNow      当前时间简要表示(hour * 100 + minute)
     * @param systemRefresh 是否是系统刷新时间
     */
    @Override
    public void doMinuteJob(int briefNow, boolean systemRefresh) {
        // 每半小时定时任务调度器不支持执行每 分钟任务
    }

    public void setGuildBO(GuildBO guildBO) {
        this.guildBO = guildBO;
    }


    public void setStoreBO(StoreBO storeBO) {
        this.storeBO = storeBO;
    }

//    public void setBossBO(BossBO bossBO) {
//        this.bossBO = bossBO;
//    }

    @Override
    public void doSystemJob(int briefNow, boolean systemRefresh) {
        // TODO Auto-generated method stub

    }
}
