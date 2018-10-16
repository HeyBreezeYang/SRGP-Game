package com.cellsgame.game.module.log.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.guild.cons.EvtTypeGuild;
import com.cellsgame.game.module.guild.cons.RightLevel;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

public class SocietyLogPro extends LogProcess {

	private String TYPE = "type";
	private String WIN = "win";
	
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(TYPE, e.getType().toString());
		boolean win = e.getParam(EvtParamType.WIN, false);
		result.put(WIN, win);
		return result;
	}

}
