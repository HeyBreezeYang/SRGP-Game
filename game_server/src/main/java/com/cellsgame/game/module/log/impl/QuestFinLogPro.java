package com.cellsgame.game.module.log.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;

import java.util.Map;

public class QuestFinLogPro extends LogProcess {

	private static String QUEST_CID = "cid";
	private static String TYPE = "type";
	
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(QUEST_CID, e.getParam(EvtParamType.QUEST_CID, 0));
		result.put(TYPE, e.getParam(EvtParamType.TYPE, 0));
		return result;
	}

}
