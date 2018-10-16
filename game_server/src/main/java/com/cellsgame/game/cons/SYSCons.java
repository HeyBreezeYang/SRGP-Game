package com.cellsgame.game.cons;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.csv.Pair;

/**
 * @author Aly on 2017-02-21.
 *         系统全局设置
 */
public abstract class SYSCons {
    public static final int SYS_REF_TIME = 4;

    public static int getMonth() {
        return LocalDate.now().getMonthValue();
    }

    public static boolean isNotSameMonth(long d1) {
        // 当前时间
        LocalDateTime current = LocalDateTime.now(ZoneId.systemDefault());
        // 最后一次任务刷新时间
        LocalDateTime last = LocalDateTime.ofInstant(new Date(d1).toInstant(), ZoneId.systemDefault());
        //
        return current.getMonth() != last.getMonth();
    }

    public static boolean isFirstDayOfMonth() {
        // 当前时间
        LocalDateTime current = LocalDateTime.now(ZoneId.systemDefault());
        //
        return current.getDayOfMonth() == 1;
    }

    public static boolean isNotSameDayWithNow(long d1) {
        // 当前时间
        LocalDateTime current = LocalDateTime.now(ZoneId.systemDefault());
        // 最后一次任务刷新时间
        LocalDateTime last = LocalDateTime.ofInstant(new Date(d1).toInstant(), ZoneId.systemDefault());
        //
        return !last.toLocalDate().isEqual(current.toLocalDate());
    }

    public static boolean notSameDate(long d1, long d2) {
        return DateUtil.notSameDate(d1, d2, SYS_REF_TIME);
    }

    public static boolean isSystemJob(LocalTime time) {
        return time.getHour() == SYSCons.SYS_REF_TIME && time.getMinute() == 0;
    }

    public static boolean isHourJob(LocalTime time) {
        return time.getMinute() == 0;
    }

    /**
     * 检测当前时间相比上一次刷新时间, 是否已超过刷新时间
     *
     * @param time 上一次刷新时间
     * @return 当前时间是否已超过系统刷新时间
     */
    public static boolean isAfterSystemRefreshTime(long time) {
        //
        return notSameDate(System.currentTimeMillis(), time);
//        // 当前时间
//        LocalDateTime current = LocalDateTime.now(ZoneId.systemDefault());
//        // 最后一次任务刷新时间
//        LocalDateTime last = LocalDateTime.ofInstant(new Date(time).toInstant(), ZoneId.systemDefault());
//        // 是否超过一天
//        boolean moreThanOneDay = last.toLocalDate().plusDays(1).isBefore(current.toLocalDate());
//        // 是否是同一天
//        boolean isSameDay = current.toLocalDate().isEqual(last.toLocalDate());
//        // 不是同一天
//        // 不在同一天当前时间已超过系统刷新时间
//        // 在同一天，重置时间在系统之前，但当前时间超过系统刷新时间
//        return moreThanOneDay ||
//                (!isSameDay && current.getHour() >= SYS_REF_TIME) ||
//                (isSameDay && last.getHour() < SYS_REF_TIME && current.getHour() >= SYS_REF_TIME);
    }


    /**
     * 在某些时间刷新点，检测当前时间相比上一次刷新时间, 是否已达到下一个刷新时间点
     *
     * @param time              上一次刷新时间
     * @param refreshTimePoints 刷新时间点，hour*100+minutes
     * @param includeMinute     刷新时间是否包括分钟
     * @return 当前时间是否已达到下一个刷新时间点及时间点下标
     */
    public static Pair<Boolean, Integer> isReachRefreshTime(long time, int[] refreshTimePoints, boolean includeMinute) {
        //
        if (time <= 0) return Pair.valueOf(true, 0);
        // 如果没有刷新时间点
        if (refreshTimePoints == null || refreshTimePoints.length == 0) return Pair.valueOf(false, -1);
        // 当前时间
        LocalDateTime current = LocalDateTime.now(ZoneId.systemDefault());
        // 最后一次刷新时间
        LocalDateTime last = LocalDateTime.ofInstant(new Date(time).toInstant(), ZoneId.systemDefault());
        //
        int briefNow = includeMinute ? (current.getHour() * 100 + current.getMinute()) : current.getHour();
        //
        int briefLast = includeMinute ? (last.getHour() * 100 + last.getMinute()) : last.getHour();
        // 当前时间是否和上一次刷新时间在同一天
        if (current.toLocalDate().equals(last.toLocalDate())) {
            // 刷新时间数量
            int refreshCount = refreshTimePoints.length - 1;
            // 查找最后一次时间
            for (int i = refreshCount; i >= 0; i--) {
                // 如果满足
                if (briefLast >= refreshTimePoints[i]) {
                    // 当前时间是否已超过下一次刷新时间
                    return Pair.valueOf(i < refreshCount && briefNow >= refreshTimePoints[i + 1], i + 1);
                }
            }
        }
        // 如果刷新时间间隔越过1天
        if (current.minusDays(1).toLocalDate().isAfter(last.toLocalDate())) {
            // 直接刷新
            return Pair.valueOf(true, 0);
        }
        // 不在同一天，检查是否已超过第一个刷新时间点
        return Pair.valueOf(briefNow >= refreshTimePoints[0], 0);
    }

    /**
     * 检测指定时间与当前时间是否不在同一周
     *
     * @param time 指定日期
     * @return 是否不在同一周
     */
    public static boolean notInSameWeek(long time) {
        // 当前时间
        Calendar current = Calendar.getInstance(TimeZone.getDefault());
        // 刷新时间
        Calendar last = Calendar.getInstance();
        last.setTimeInMillis(time);
        // 不是同一周
        return current.get(Calendar.WEEK_OF_YEAR) != last.get(Calendar.WEEK_OF_YEAR);
    }
}
