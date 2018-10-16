package com.cellsgame.game.module.log.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.guild.cons.EvtTypeGuild;
import com.cellsgame.game.module.guild.cons.RightLevel;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

public class GuildLogPro extends LogProcess {

	private String TYPE = "type";
	private String LEVEL = "level";
	private String FIGHT_FORCE = "fightForce";
	private String RIGHT_LEVEL = "right";
	
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(TYPE, e.getType().toString());
		Enum<?> o = e.getType();
		if (o instanceof EvtTypeGuild) {
			switch ((EvtTypeGuild) o) {
				case Create:
					PlayerVO create = e.getParam(EvtParamType.PLAYER);
					result.put(PLAYER_ID, create.getId());
					result.put(PLAYER_NAME, create.getName());
					break;
				case Join:
					PlayerInfoVO infoVO = e.getParam(EvtParamType.PlayerInfoVO);
					RightLevel right = e.getParam(EvtParamType.Right_Level, null);
					result.put(PLAYER_ID, infoVO.getPid());
					result.put(PLAYER_NAME, infoVO.getName());
					result.put(LEVEL, infoVO.getPlv());
					result.put(FIGHT_FORCE, infoVO.getFightForce());
					result.put(RIGHT_LEVEL, right.getId());
					break;
			}
		}
		return result;
	}

}
