package com.cellsgame.game.module.log.impl;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class PlayerEnterGameAndOfflineLogPro extends LogProcess {

	private static String TYPE = "type";
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		Enum<?> en = (Enum<?>)e.getType();
		result.put(TYPE, ((EvtTypePlayer)en).toString());
		return result;
	}

}
