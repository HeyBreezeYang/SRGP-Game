package com.cellsgame.game.module.goods.msg;

import java.util.List;
import java.util.Map;

import javax.xml.soap.MessageFactory;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.module.MsgFactory;

/**
 * Created by yfzhang on 2016/8/22.
 */
public class MsgFactoryGoods extends MsgFactory {
	
	public static final String GOODS = "goods";
	
    private static final MsgFactoryGoods instance  = new MsgFactoryGoods();
    
    public static MsgFactoryGoods instance(){
    	return instance;
    }

	@Override
	public String getModulePrefix() {
		return GOODS;
	}

	public Map getGoodsPrizeMsg(Map<?, ?> parent, Map<?, ?> prizeInfo) {
		Map msdInfoOp = gocOpMap(parent);
		addPrizeMsg(msdInfoOp, prizeInfo);
		return parent;
	}
}
