package com.cellsgame.game.module.guild.vo;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * @author Aly on  2016-08-15.
 *         请求信息
 */
public class GuildReqVO extends DBVO {
    // static
    public static final AtomicIntegerFieldUpdater<GuildReqVO> statusUpdater = AtomicIntegerFieldUpdater.newUpdater(GuildReqVO.class, "status");
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_ACCEPT = 2;
    public static final int STATUS_REFUSE = 3;
    public static final int STATUS_DONE = 4;

    //id
    private int id;
    private int pid;

    @Save(ix = 1)
    private int guildID;

    @Save(ix = 2)
    private volatile int status;

    @Save(ix = 3)
    private long time;

    public int getGuildID() {
        return guildID;
    }

    public void setGuildID(int guildID) {
        this.guildID = guildID;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
        return new Object[]{pid, guildID};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        pid = (int) relationKeys[0];
        guildID = (int) relationKeys[1];
    }

    @Override
    protected void init() {
        status = STATUS_NORMAL;
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {

    }
}
