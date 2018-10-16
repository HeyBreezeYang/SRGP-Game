package com.cellsgame.game.module.log.impl;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class GuildDonateLogPro extends LogProcess {

	private String TRE_DONATE = "treDnt";
	private String GUILD_ID = "gId";
	private String GUILD_NAME = "gName";
	private String NUM = "num";
	private String TOTAL_NUM = "tNum";
	
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(GUILD_ID, e.getParam(EvtParamType.GUILD_ID, 0));
		result.put(GUILD_NAME, e.getParam(EvtParamType.GUILD_NAME, ""));
		result.put(NUM, e.getParam(EvtParamType.NUM, 0));
		result.put(TOTAL_NUM, e.getParam(EvtParamType.TOTAL_NUM, 0L));
		return result;
	}

}
