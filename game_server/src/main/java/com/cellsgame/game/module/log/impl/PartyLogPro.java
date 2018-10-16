package com.cellsgame.game.module.log.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;

import java.util.Map;

public class PartyLogPro extends LogProcess {

	private String TYPE = "type";
	private String CID = "cid";
	
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(TYPE, e.getType().toString());
		int cid = e.getParam(EvtParamType.CID);
		result.put(CID, cid);
		return result;
	}

}
