package com.cellsgame.game.module.log.impl;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class GoodsLogPro extends LogProcess {

	private static String GOODS_ID = "gCid";
	private static String CHANGE = "change";
	private static String AFTER = "after";
	
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(GOODS_ID, e.getParam(EvtParamType.GOODS_CID, 0));
		result.put(CHANGE, e.getParam(EvtParamType.CHANGE, 0L));
		result.put(AFTER, e.getParam(EvtParamType.AFTER, 0L));
		return result;
	}

}
