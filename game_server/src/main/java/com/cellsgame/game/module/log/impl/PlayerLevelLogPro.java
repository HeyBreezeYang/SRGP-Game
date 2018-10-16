package com.cellsgame.game.module.log.impl;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class PlayerLevelLogPro extends LogProcess {

	private String LEVEL_TYPE = "lvType";
	private String EXP = "exp";
	private String LEVEL = "lv";
	private String OLD_LEVEL = "oldLv";
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(LEVEL_TYPE, e.getParam(EvtParamType.LEVEL_TYPE, -1));
		result.put(OLD_LEVEL, e.getParam(EvtParamType.OLD_LEVEL, -1));
		result.put(EXP, e.getParam(EvtParamType.EXP, -1));
		result.put(LEVEL, e.getParam(EvtParamType.LEVEL, -1));
		return result;
	}

}
