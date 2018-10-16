package com.cellsgame.game.module.store.vo;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.shop.vo.ShopItemVO;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

import java.util.List;

public class StoreVO extends DBVO {

    // 唯一ID
    private int id;
    // 商城类型
    @Save(ix = 0)
    private int type;
    @Save(ix = 1)
    // 商品{index, StoreItemVO}
    private List<StoreItemVO> itemVOs;
    @Save(ix = 3)
    // 商店刷新时间
    private int[] refreshHours;
    // 开始时间
    @Save(ix = 4)
    private long startTime;
    // 结束时间
    @Save(ix = 5)
    private long endTime;
    // 商店上一次刷新时间
    @Save(ix = 6)
    private long refreshTime;
    // 系统刷新版本号
    @Save(ix = 7)
    private int version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<StoreItemVO> getItemVOs() {
        return itemVOs;
    }

    public void setItemVOs(List<StoreItemVO> itemVOs) {
        this.itemVOs = itemVOs;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int[] getRefreshHours() {
        return refreshHours;
    }

    public void setRefreshHours(int[] refreshHours) {
        this.refreshHours = refreshHours;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

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
        return new Object[0];
    }

    @Override
    protected void setRelationKeys(Object[] objects) {

    }

    @Override
    protected void init() {
        itemVOs = GameUtil.createList();
        refreshHours = new int[]{};
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer integer) {

    }


}
