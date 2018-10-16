package com.cellsgame.game.module.activity.cond.impl;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.vo.ActivityCondRecVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.pay.cache.CacheOrderItem;
import com.cellsgame.game.module.pay.cons.EvtPay;
import com.cellsgame.game.module.pay.csv.OrderItemConfig;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;


public class PayMoneyCondProcess extends ACondProcess {

	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EvtPay.Pay};
	}

	@Override
	public boolean recordCondData(Map<?, ?> parent, ActivityVO act, ActivityCond cond,
			ActivityCondRecVO condRecVO, PlayerVO player, GameEvent event) {
		String orderItemId = event.getParam(EvtParamType.ORDER_ITEM_ID);
		OrderItemConfig config = CacheOrderItem.getConfig(orderItemId);
		if(config == null) return false;
		condRecVO.setFinishData(Math.min(condRecVO.getFinishData() + config.getOrderMoney(), cond.getValue()));
		long oldValue = condRecVO.getRecordDetails().getOrDefault(player.getId(), 0L);
		condRecVO.getRecordDetails().put(player.getId(), oldValue + config.getOrderMoney());
		return true;
	}

}
