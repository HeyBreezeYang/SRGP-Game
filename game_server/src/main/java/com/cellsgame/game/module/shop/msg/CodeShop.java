package com.cellsgame.game.module.shop.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

/**
 * 商店
 *
 * @see ModuleID#Shop
 */
public enum CodeShop implements ICode {

    SHOP_NOT_FOUND(1),                       // 商品未找到
    SHOP_UNKNOWN_PLACE_HOLDER(2),            // 未知商店存储类型
    SHOP_DATA_EXCEPTION(3),                  // 数据异常，未找到商品
    SHOP_LIMIT(4),                           // 已到达限购条件
    SHOP_LIMIT_TIME(5),                      // 商品已下架
    SHOP_REFRESH_RULE_PARAM_ERROR(6),        // 商店刷新规则参数异常
    SHOP_GROUP_NOT_FOUND(7),                 // 商品组不存在
    SHOP_REQUIRE_LEVEL(8),                   // 等级不满足(可见等级或者购买等级)
    SHOP_REQUIRE_VIP(9),                     // VIP等级不满足
    SHOP_ITEM_NOT_FOUND(10),                 // 购买特殊商品时未找到目标
    SHOP_NOT_ALLOW_MANUAL_REFRESH(11),                 // 不能手动刷新商店
    SHOP_MAX_MANUAL_REFRESH(12),                 // 手动刷新商店次数超限
	SHOP_REQUIRE_GUILD_VIP(13),                     // 公会等级不满足

	;
	private int code;
	
	CodeShop(int code){
		this.code = code;
	}
	
	
	@Override
	public int getModule() {
		return ModuleID.Shop;
	}

	@Override
	public int getCode() {
		return code;
	}

}
