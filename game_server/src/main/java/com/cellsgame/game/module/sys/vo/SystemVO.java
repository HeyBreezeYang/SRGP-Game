package com.cellsgame.game.module.sys.vo;

import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.context.GameConfig;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.module.shop.vo.ShopItemRecordVO;
import com.cellsgame.game.module.shop.vo.ShopVO;
import com.cellsgame.orm.DBObj;

/**
 * 系统数据.
 *
 * @author Yang
 */
public final class SystemVO implements EvtHolder {
    public static final int GAME_DB_UNIQUE_ID = GameConfig.getConfig().getGameServerId();
    private static final SystemVO Instance = new SystemVO();

    /**
     * 全服商品限购数据
     */
    private Map<Integer, ShopItemRecordVO> shopItemRecordVOMap = GameUtil.createMap();
    /**
     * 系统商店
     */
    private Map<Integer, ShopVO> shopVOMap = GameUtil.createMap();


    private SystemVO() {

    }

    public static SystemVO getInstance() {
        return Instance;
    }

    /**
     * 加载系统商店数据。
     *
     * @param data 系统商店DB数据
     */
    public void loadSystemShop(List<DBObj> data) {
        for (DBObj dbObj : data) {
            ShopVO shopVO = new ShopVO();
            shopVO.readFromDBObj(dbObj);
            shopVOMap.put(shopVO.getCid(), shopVO);
        }
    }

    /**
     * 加载全服限购商品数据
     *
     * @param data 全服限购商品DB数据
     */
    public void loadShopItemLimitRecord(List<DBObj> data) {
        data.forEach(dbObj -> {
            ShopItemRecordVO itemRecordVO = new ShopItemRecordVO();
            itemRecordVO.readFromDBObj(dbObj);
            shopItemRecordVOMap.put(itemRecordVO.getCid(), itemRecordVO);
        });
    }


    public Map<Integer, ShopItemRecordVO> getShopItemRecordVOMap() {
        return shopItemRecordVOMap;
    }

    public Map<Integer, ShopVO> getShopVOMap() {
        return shopVOMap;
    }

}
