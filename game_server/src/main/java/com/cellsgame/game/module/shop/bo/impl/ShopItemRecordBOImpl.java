package com.cellsgame.game.module.shop.bo.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.shop.bo.ShopBO;
import com.cellsgame.game.module.shop.bo.ShopItemRecordBO;
import com.cellsgame.game.module.shop.csv.ShopItemConfig;
import com.cellsgame.game.module.shop.vo.ShopItemRecordVO;
import com.cellsgame.game.module.shop.vo.ShopItemVO;
import com.cellsgame.game.module.shop.vo.ShopVO;
import com.cellsgame.game.module.sys.vo.SystemVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;

/**
 * File Description.
 *
 * @author Yang
 */
public class ShopItemRecordBOImpl implements ShopItemRecordBO {
    @Resource
    private BaseDAO<ShopItemRecordVO> shopItemRecordDAO;


    /**
     * 购买成功之后处理
     *
     * @param playerVO   玩家数据
     * @param shopVO     商店数据
     * @param shopItemVO 商品数据
     * @param quantity   购买数量
     */
    @Override
    public void afterShopBuy(PlayerVO playerVO, ShopVO shopVO, ShopItemVO shopItemVO, int quantity) {
        // 商品配置数据
        ShopItemConfig shopItemConfig = shopItemVO.getCfg();
        // 限购数据
        ShopItemRecordVO recordVO;
        // 如果是系统商店且为个人限购商品
//        if (shopVO.isSystem()) {
//            // 查看是否已存在记录
//            recordVO = playerVO.getShopItemRecordVOMap().get(shopItemConfig.getId());
//            // 如果不存在记录
//            if (recordVO == null)
//                playerVO.getShopItemRecordVOMap().putIfAbsent(shopItemConfig.getId(), recordVO = new ShopItemRecordVO());
//            //配置档ID
//            recordVO.setCid(shopItemConfig.getId());
//            // 商店所属玩家ID(当记录不属于任何玩家时,表示此记录为全服限购)
//            recordVO.setPlayer(playerVO.getId());
//            // 已售数量
//            ShopItemRecordVO.SoldUpdater.addAndGet(recordVO, quantity);
//            //
//            shopItemRecordDAO.save(recordVO);
//        }
        // 记录商品本身出售数量
        if (!shopVO.isSystem()) ShopItemVO.SoldUpdater.addAndGet(shopItemVO, quantity);
    }

    @Override
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
        // 玩家所有商店数据
        List<DBObj> shopDB = (List<DBObj>) data.get(DATA_SIGN_SHOP_ITEM_RECORD);
        if (shopDB == null)
            return;
        //
        for (DBObj dbObj : shopDB) {
            // 限购数据
            ShopItemRecordVO recordVO = new ShopItemRecordVO();
            // DB TO OBJECT
            recordVO.readFromDBObj(dbObj);
            // 限购数据所属性玩家
            recordVO.setPlayer(player.getId());
            // 加入玩家数据
//            player.getShopItemRecordVOMap().put(recordVO.getCid(), recordVO);
        }
    }

	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
	}
}
