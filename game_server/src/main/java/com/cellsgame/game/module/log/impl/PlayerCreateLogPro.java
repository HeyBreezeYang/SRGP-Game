package com.cellsgame.game.module.log.impl;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class PlayerCreateLogPro extends LogProcess {

	private static String ACCOUNT_ID = "actId";
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		if(holder instanceof PlayerVO) {
			PlayerVO player = (PlayerVO) holder;
			result.put(ACCOUNT_ID, player.getAccountId());
		}
		return result;
	}

}
