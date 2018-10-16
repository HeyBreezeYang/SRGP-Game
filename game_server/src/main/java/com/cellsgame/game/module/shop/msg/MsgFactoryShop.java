package com.cellsgame.game.module.shop.msg;

import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.shop.vo.ShopItemRecordVO;
import com.cellsgame.game.module.shop.vo.ShopItemVO;
import com.cellsgame.game.module.shop.vo.ShopVO;
import com.cellsgame.game.module.sys.vo.SystemVO;

/**
 * File Description.
 *
 * @author Yang
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class MsgFactoryShop extends MsgFactory {

    public static final String SHOP = "shop";
    public static final String SHOP_SOLD = "sold";
    public static final String SHOP_ITEM_UPDATE = "itmUpd";
    public static final String SHOP_PRIZE_AND_COST = "przCst";

    private static final MsgFactoryShop instance = new MsgFactoryShop();

    public static MsgFactoryShop instance() {
        return instance;
    }

    @Override
    public String getModulePrefix() {
        return SHOP;
    }

    public Map<String, Object> getShopInfoMsg(Map parent, PlayerVO playerVO, ShopVO shopVO) {
        parent = creIfNull(parent);
        Map shopInfo = gocInfoMap(parent);
        shopInfo.put(shopVO.getCid(), getShopVOMsg(playerVO, shopVO));
        return parent;
    }

    private Map getShopVOMsg(PlayerVO playerVO, ShopVO shopVO) {
        Map shopData = GameUtil.createSimpleMap();
        shopData.put(CID, shopVO.getCid());
        shopData.put(RESET, shopVO.getManualRefreshTimes());
        List<Map<String, Object>> shopItemData = gocLstIn(shopData, LIST);
        for (ShopItemVO itemVO : shopVO.getItemVOs()) shopItemData.add(getItemVOMsg(playerVO, shopVO, itemVO));
        return shopData;
    }

    private Map<String, Object> getItemVOMsg(PlayerVO playerVO, ShopVO shopVO, ShopItemVO shopItemVO) {
        Map<String, Object> itemData = GameUtil.createSimpleMap();
        itemData.put(INDEX, shopItemVO.getIndex());
        itemData.put(CID, shopItemVO.getCid());
        itemData.put(SHOP_SOLD, shopItemVO.getSold());
        if (shopVO.isSystem()) {// 系统商店个人限购
            // 查看个人限购数据
//            ShopItemRecordVO recordVO = playerVO.getShopItemRecordVOMap().get(shopItemVO.getCid());
            //
//            itemData.put(SHOP_SOLD, recordVO == null ? 0 : recordVO.getSold());
        }
        return itemData;
    }

    public Map getShopDeleteMessage(Map parent, ShopVO shopVO) {
        parent = creIfNull(parent);
        gocLstIn(gocOpMap(parent), DELETE).add(shopVO.getCid());
        return parent;
    }

    public Map<String, Object> getShopBuyMessage(Map parent, PlayerVO playerVO, ShopVO shopVO, ShopItemVO shopItemVO, Map<String, Object> prizeAndCost) {
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        Map<Integer, Map> itemUpdate = gocMapIn(op, SHOP_ITEM_UPDATE);
        Map<Integer, Map> shopsItems = gocMapIn(itemUpdate, shopVO.getCid());
        Map<String, Object> itemVOMsg = getItemVOMsg(playerVO, shopVO, shopItemVO);
        shopsItems.put(shopItemVO.getIndex(), itemVOMsg);
        addPrizCostMessage(op, prizeAndCost);
        return parent;
    }

    public void addPrizCostMessage(Map<String, Object> parent, Map<String, Object> prizeAndCost) {
        List<Map> przCst = gocLstIn(parent, SHOP_PRIZE_AND_COST);
        przCst.add(prizeAndCost);
    }


}