package com.cellsgame.game.module.guild.vo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * @author Aly on  2016-08-13.
 *         家族成员
 */
public class GuildMemberVO extends DBVO {
    //DB
    private int id;
    private int pid;
    private int guildID;                    // 目前在哪个家族      <=0 表示不存在于任何家族
    @Save(ix = 1)
    private long nextEnterGuildTime;        // 下次进入家族的时间    --> 离开的时候才会设置这个值
    @Save(ix = 2)
    private long joinGuildTime;             // 加入家族的时间
    @Save(ix = 3)
    private long lastRestTime;              // 上次奖励重置时间
    @Save(ix = 4)
    private int donateCid;                  // 捐献配置   <=0 表示还没有捐献
    @Save(ix = 5)
    private Map<Integer, Integer> totalDonate; //总捐献记录
    @Save(ix = 6)
    private long historyGuildCoin;          // 历史贡献
    @Save(ix = 7)
    private Set<Integer> usedWorker;         //使用过的员工

    public GuildMemberVO() {
        super();
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getGuildID() {
        return guildID;
    }

    public void setGuildID(int guildID) {
        this.guildID = guildID;
    }

    public long getNextEnterGuildTime() {
        return nextEnterGuildTime;
    }

    public void setNextEnterGuildTime(long nextEnterGuildTime) {
        this.nextEnterGuildTime = nextEnterGuildTime;
    }

    public long getJoinGuildTime() {
        return joinGuildTime;
    }

    public void setJoinGuildTime(long joinGuildTime) {
        this.joinGuildTime = joinGuildTime;
    }


    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = ((int) pk);
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{pid, guildID};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        pid = (int) relationKeys[0];
        guildID = (int) relationKeys[1];
    }

    @Override
    protected void init() {
        pid = 0;
        totalDonate = GameUtil.createSimpleMap();
        usedWorker = new HashSet<>();
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {

    }

    public long getLastRestTime() {
        return lastRestTime;
    }

    public void setLastRestTime(long lastRestTime) {
        this.lastRestTime = lastRestTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDonateCid() {
        return donateCid;
    }

    public void setDonateCid(int donateCid) {
        this.donateCid = donateCid;
    }

    public Map<Integer, Integer> getTotalDonate() {
        return totalDonate;
    }

    public void setTotalDonate(Map<Integer, Integer> totalDonate) {
        this.totalDonate = totalDonate;
    }

    public long getHistoryGuildCoin() {
        return historyGuildCoin;
    }

    public void setHistoryGuildCoin(long historyGuildCoin) {
        this.historyGuildCoin = historyGuildCoin;
    }

    public Set<Integer> getUsedWorker() {
        return usedWorker;
    }

    public void setUsedWorker(Set<Integer> usedWorker) {
        this.usedWorker = usedWorker;
    }
}
