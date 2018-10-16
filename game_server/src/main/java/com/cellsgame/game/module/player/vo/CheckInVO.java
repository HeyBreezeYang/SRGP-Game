package com.cellsgame.game.module.player.vo;

import java.util.Set;
import com.cellsgame.game.cons.SYSCons;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;

/**
 * File Description.
 *
 * @author Yang
 */
public class CheckInVO extends DBVO {
    private int id;
    private int player;
    @Save(ix = 1)
    private int checkInDaysPerMonth; // 当月累计签到天数
    @Save(ix = 2)
    private long lastCheckInTime;// 上一次签到时间
    @Save(ix = 3)
    private Set<Integer> totalCheckInPrized; // 已领取的当前累计签到奖励

    public Set<Integer> getTotalCheckInPrized() {
        return totalCheckInPrized;
    }

    public void setTotalCheckInPrized(Set<Integer> totalCheckInPrized) {
        this.totalCheckInPrized = totalCheckInPrized;
    }

    public int getCheckInDaysPerMonth() {
        return checkInDaysPerMonth;
    }

    public void setCheckInDaysPerMonth(int checkInDaysPerMonth) {
        this.checkInDaysPerMonth = checkInDaysPerMonth;
    }

    public boolean isChecked() {
        return getLastCheckInTime() > 0 && !SYSCons.isNotSameDayWithNow(getLastCheckInTime());
    }

    public long getLastCheckInTime() {
        return lastCheckInTime;
    }

    public void setLastCheckInTime(long lastCheckInTime) {
        this.lastCheckInTime = lastCheckInTime;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = (int) pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{player};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        player = (int)relationKeys[0];
    }

    @Override
    protected void init() {
        totalCheckInPrized = Sets.newHashSet();
    }

    @Override
    public Integer getCid() {
        return 0;
    }

    @Override
    public void setCid(Integer cid) {

    }
}
