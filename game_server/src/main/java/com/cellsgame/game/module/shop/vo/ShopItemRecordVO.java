package com.cellsgame.game.module.shop.vo;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * 限购商品记录数据.
 * <p>
 * 如果记录的全局变量, 则数据表示为全服限购。
 * <p>
 * 如果记录的是玩家亦是, 则数据表示为玩家的限购(系统商品限购或全服商品个人限购)
 * <p>
 * 商品的上架时间, 下架时间可以设置, 但不能重置、覆盖、取消。
 * <p>
 * 如:
 * <p>
 * 当商品存在上架、下架时间时, 不能修改其时间。反之, 可以修改。
 *
 * @author Yang
 */
public class ShopItemRecordVO extends DBVO {
    /**
     * 配置档ID
     */
    private int cid;
    // 唯一ID
    private int id;
    // 商店所属玩家ID(当记录不属于任何玩家时,表示此记录为全服限购)
    private int pid;
    // 已售数量
    @Save(ix = 1)
    private volatile int sold;

    public static final AtomicIntegerFieldUpdater<ShopItemRecordVO> SoldUpdater = AtomicIntegerFieldUpdater.newUpdater(ShopItemRecordVO.class, "sold");

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
}
