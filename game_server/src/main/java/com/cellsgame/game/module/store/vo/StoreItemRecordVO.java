package com.cellsgame.game.module.store.vo;

import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class StoreItemRecordVO extends DBVO {
    // 唯一ID
    private int id;
    // 玩家ID
    private int pid;
    // 配置档ID
    private int cid;
    // 商城ID
    @Save(ix = 1)
    private int storeId;
    // 已售数量
    @Save(ix = 2)
    private int sold;
    // 版本号
    @Save(ix = 3)
    private int version;

    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        this.id = (int) pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{pid};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys != null && relationKeys.length > 0)
            pid = (int) relationKeys[0];
    }

    @Override
    protected void init() {
        sold = 0;
    }

    public void setPlayer(int player) {
        this.pid = player;
    }

    public int getPlayer() {
        return this.pid;
    }

    @Override
    public Integer getCid() {
        return cid;
    }

    @Override
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
