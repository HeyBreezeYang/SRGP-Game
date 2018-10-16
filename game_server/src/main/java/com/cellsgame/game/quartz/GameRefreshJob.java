package com.cellsgame.game.quartz;

/**
 * File Description.
 *
 * @author Yang
 */
public interface GameRefreshJob {
    /**
     * 系统任务
     *
     * @param briefNow      当前时间简要表示(hour * 100 + minute)
     * @param systemRefresh 是否是系统刷新时间
     */
    void doSystemJob(int briefNow, boolean systemRefresh);

    /**
     * 每小时任务
     *
     * @param briefNow      当前时间简要表示(hour * 100 + minute)
     * @param systemRefresh 是否是系统刷新时间
     */
    void doHourJob(int briefNow, boolean systemRefresh);

    /**
     * 每分钟任务
     *
     * @param briefNow      当前时间简要表示(hour * 100 + minute)
     * @param systemRefresh 是否是系统刷新时间
     */
    void doMinuteJob(int briefNow, boolean systemRefresh);
}
