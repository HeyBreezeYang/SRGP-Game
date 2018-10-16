package com.cellsgame.game.module.shop.bo;

import java.util.Map;

import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.shop.vo.ShopItemRecordVO;
import com.cellsgame.game.module.shop.vo.ShopItemVO;
import com.cellsgame.game.module.shop.vo.ShopVO;

/**
 * File Description.
 *
 * @author Yang
 */
public interface ShopItemRecordBO extends IBuildData {


    /**
     * 购买成功之后处理
     *
     * @param playerVO   玩家数据
     * @param shopVO     商店数据
     * @param shopItemVO 商品数据
     * @param quantity   购买数量
     */
    void afterShopBuy(PlayerVO playerVO, ShopVO shopVO, ShopItemVO shopItemVO, int quantity);
}
