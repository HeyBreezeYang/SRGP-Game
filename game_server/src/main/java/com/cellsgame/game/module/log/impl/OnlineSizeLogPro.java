package com.cellsgame.game.module.log.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;

import java.util.Map;

public class OnlineSizeLogPro extends LogProcess {

	private String SIZE = "size";
	
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		int cid = e.getParam(EvtParamType.NUM);
		result.put(SIZE, cid);
		return result;
	}

}
