package com.cellsgame.game.module.log.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;

import java.util.Map;

public class PlunderLogPro extends LogProcess {

	private String INDEX = "index";
	private String WIN = "win";
	
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		int index = e.getParam(EvtParamType.NUM);
		boolean win = e.getParam(EvtParamType.WIN);
		result.put(INDEX, index);
		result.put(WIN, win);
		return result;
	}

}
