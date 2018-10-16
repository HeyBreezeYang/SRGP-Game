package com.cellsgame.game.quartz;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collection;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cons.SYSCons;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.bo.ActivityBO;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerPI;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.sys.SystemBO;
/**
 * @author Aly on  2016-08-25.
 */
public class MinutesJob implements GameRefreshJob {
    private DepotBO depotBO;
    private SystemBO systemBO;
    private ActivityBO activityBO;


    public void execute() {
        //心跳超时处理
        CachePlayerPI.clearCache();
        // 冒险任务状态变更
        Dispatch.dispatchGameLogic(new Runnable() {
            @Override
            public void run() {
                LocalTime time = LocalTime.now(ZoneId.systemDefault());
                //
                int briefNow = time.getHour() * 100 + time.getMinute();
                //
                boolean system = SYSCons.isSystemJob(time);
                // 如果达到系统刷新时间
                if (system) doSystemJob(briefNow, true);
                // 每小时任务
                if (SYSCons.isHourJob(time)) doHourJob(briefNow, system);
                // 分钟任务
                doMinuteJob(briefNow, system);
            }
        });
    }

    /**
     * 系统任务
     *
     * @param briefNow      当前时间简要表示(hour * 100 + minute)
     * @param systemRefresh 是否是系统刷新时间
     */
    @Override
    public void doSystemJob(int briefNow, boolean systemRefresh) {

    }

    /**
     * 每小时任务
     *
     * @param briefNow      当前时间简要表示(hour * 100 + minute)
     * @param systemRefresh 是否是系统刷新时间
     */
    @Override
    public void doHourJob(int briefNow, boolean systemRefresh) {

    }

    /**
     * 每分钟任务
     *
     * @param briefNow      当前时间简要表示(hour * 100 + minute)
     * @param systemRefresh 是否是系统刷新时间
     */
    @Override
    public void doMinuteJob(int briefNow, boolean systemRefresh) {
        long sysTime = System.currentTimeMillis();
        activityBO.sysUpdateActivityStatus();
        LocalDateTime dateTime = LocalDateTime.now();
        int second = dateTime.getMinute();
        if(second % 5 == 0){
            EvtTypePlayer.OnlineSize.happen(GameUtil.createSimpleMap(), CMD.system, null, EvtParamType.NUM.val(CachePlayer.getOnlinePlayers().size()));
        }
    }

    public void setDepotBO(DepotBO depotBO) {
        this.depotBO = depotBO;
    }

    public void setSystemBO(SystemBO systemBO) {
        this.systemBO = systemBO;
    }

    public void setActivityBO(ActivityBO activityBO) {
        this.activityBO = activityBO;
    }

}
