package com.cellsgame.game.module.log.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

public class PlayerOfflineLogPro extends LogProcess {

	private static String FIGHT_FORCE = "ff";
	private static String CURRENCY = "cur";
	private static String LEVEL = "lv";
	private static String VIP = "vip";
	private static String VIP_EXP = "vipExp";
	private static String LOGIN_DATA = "lgnDt";

	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		if(holder instanceof PlayerVO) {
			PlayerVO player = (PlayerVO) holder;
//			result.put(FIGHT_FORCE, player.getFightForce());
//			result.put(LEVEL, player.getLevel());
//			result.put(VIP, player.getVip());
//			result.put(VIP_EXP, player.getVipExp());
			result.put(CURRENCY, player.getDepotVO().getCurrencyMap());
			result.put(LOGIN_DATA, player.getLoginDate());
		}
		return result;
	}

}
