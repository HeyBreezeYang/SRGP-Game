package com.cellsgame.game.module.goods.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

/**
 * @see ModuleID#Goods
 */
public enum CodeGoods implements ICode {
	    //物品相关
	    Goods_PrizeNumError(1),                 // 奖励数量错误
	    //物品类型错误
	    Goods_Type_Error(3),  	// 物品类型错误
	    Goods_Special_Type_Error(4),            // 特殊物品类型错误
	    Goods_NOT_EXIST(5),                     // 物品不存在
	    GOODS_FUNCS_ENDLESSLOOP(6),          //功能物品配置错误
	    Goods_ConfigError(7),              // 物品配置错误
	;
	
	
	private int code;
	
	CodeGoods(int code){
		this.code = code;
	}
	@Override
	public int getModule() {
		return ModuleID.Goods;
	}

	@Override
	public int getCode() {
		return code;
	}

}
