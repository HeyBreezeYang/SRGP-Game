package com.cellsgame.game.module.store.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

/**
 * 商城
 * @see ModuleID#Store
 */
public enum CodeStore implements ICode {


	NotFindStore(1),
	ItemIndexError(2),//商品错误
	ReqLevel(3),//等级不足
	ReqVip(4),//VIP不足
	Limit(5),//已经达到上限
	CreateStoreParamsError(6),
	CreateStoreNotFindItemConfig(7),
	NotOpen(8),//没有开启
	;
	private int code;

	CodeStore(int code){
		this.code = code;
	}
	
	
	@Override
	public int getModule() {
		return ModuleID.Store;
	}

	@Override
	public int getCode() {
		return code;
	}

}
