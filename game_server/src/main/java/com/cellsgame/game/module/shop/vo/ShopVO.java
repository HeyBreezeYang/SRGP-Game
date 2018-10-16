package com.cellsgame.game.module.shop.vo;

import java.util.List;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.cfg.base.CfgSaveVO;
import com.cellsgame.game.module.shop.csv.ShopConfig;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * File Description.
 *
 * @author Yang
 */
public class ShopVO extends CfgSaveVO<ShopConfig> {

    /**
     * 配置档ID
     */
    private int cid;
    // 唯一ID
    private int id;
    // 商店所属玩家ID
    private int pid;
    // 商店上一次刷新时间
    @Save(ix = 1)
    private long refreshTime;
    // 商品{index, ShopItemVO}
    @Save(ix = 2)
    private List<ShopItemVO> itemVOs;
    // 商店额外数据
    @Save(ix = 3)
    private ShopExtraVO extraData;
    // 手动刷新次数
    @Save(ix = 4)
    private int manualRefreshTimes;
    // 系统刷新版本号
    @Save(ix = 5)
    private int version;

    public ShopVO() {
        super(ShopConfig.class);
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
        return new Object[]{pid};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys != null && relationKeys.length > 0)
            pid = (int) relationKeys[0];
    }

    @Override
    protected void init() {
        id = 0;
        pid = 0;
        itemVOs = GameUtil.createList();
        extraData = new ShopExtraVO();
        manualRefreshTimes = 0;
    }

    @Override
    public Integer getCid() {
        return cid;
    }

    @Override
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public boolean isSystem() {
        return getCfg().isSystem();
    }

    public boolean isGuild() {
        return getCfg().isGuild();
    }

    public int getPlayer() {
        return this.pid;
    }

    public void setPlayer(int player) {
        this.pid = player;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getManualRefreshTimes() {
        return manualRefreshTimes;
    }

    public void setManualRefreshTimes(int manualRefreshTimes) {
        this.manualRefreshTimes = manualRefreshTimes;
    }

    public List<ShopItemVO> getItemVOs() {
        return itemVOs;
    }

    public void setItemVOs(List<ShopItemVO> itemVOs) {
        this.itemVOs = itemVOs;
    }

    public ShopExtraVO getExtraData() {
        return extraData;
    }

    public void setExtraData(ShopExtraVO extraData) {
        this.extraData = extraData;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
