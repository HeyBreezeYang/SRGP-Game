package com.cellsgame.game.module.store.vo;

import com.cellsgame.game.core.cfg.base.CfgSaveVO;
import com.cellsgame.game.module.shop.csv.ShopItemConfig;
import com.cellsgame.orm.enhanced.annotation.Save;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 出售商品数据。
 *
 * @author Yang
 */
public class StoreItemVO extends CfgSaveVO<ShopItemConfig> {
    /**
     * 配置档ID
     */
    private int cid;
    // 所在商店下标
    @Save(ix = 1)
    private int index;
    // 出售数量
    @Save(ix = 2)
    private int sold;

    public StoreItemVO() {
        super(ShopItemConfig.class);
    }

    @Override
    protected Object initPrimaryKey() {
        return 1;
    }

    @Override
    protected Object getPrimaryKey() {
        return 1;
    }

    @Override
    protected void setPrimaryKey(Object pk) {

    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[0];
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {

    }

    @Override
    protected void init() {
        index = 0;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    @Override
    public Integer getCid() {
        return cid;
    }

    @Override
    public void setCid(Integer cid) {
        this.cid = cid;
    }
}
