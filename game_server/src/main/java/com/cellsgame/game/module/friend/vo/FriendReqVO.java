package com.cellsgame.game.module.friend.vo;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * @author Aly @2017-02-07.
 *         好友请求/好友关系VO
 */
public class FriendReqVO extends DBVO {
    public static final int STATUS_CREATE = 1;          // 创建
    public static final int STATUS_REQUEST_SEND = 2;    // 请求发送
    public static final int STATUS_REJECT = 3;          // 拒绝
    public static final int STATUS_ACCEPT = 4;          // 同意
    public static final int STATUS_DELETE = 5;          // 删除

    private static final AtomicIntegerFieldUpdater<FriendReqVO> statusUpdater = AtomicIntegerFieldUpdater.newUpdater(FriendReqVO.class, "status");
    private int id;
    @Save(ix = 1)
    private int src;
    @Save(ix = 2)
    private int tgt;
    @Save(ix = 3)
    private volatile int status;
    @Save(ix = 4)
    private long modifyTime;                            // 最近状态修改时间

    public boolean casStatus(int expect, int update) {
        boolean suc = statusUpdater.compareAndSet(this, expect, update);
        if (suc) {
            modifyTime = System.currentTimeMillis();
        }
        return suc;
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
        return null;
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
    }

    @Override
    protected void init() {
        src = 0;
        tgt = 0;
        status = STATUS_CREATE;
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getTgt() {
        return tgt;
    }

    public void setTgt(int tgt) {
        this.tgt = tgt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }
}
