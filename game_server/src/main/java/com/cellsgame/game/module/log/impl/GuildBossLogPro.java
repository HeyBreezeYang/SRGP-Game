package com.cellsgame.game.module.log.impl;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class GuildBossLogPro extends LogProcess {

	private String TYPE = "type";
	private String BOSS_CID = "bCid";
	private String GUILD_ID = "gId";
	private String GUILD_NAME = "gName";
	private String HURT = "hurt";
	private String LF = "lf";
	
	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		GuildVO guildVO = e.getParam(EvtParamType.GUILD);
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(TYPE, e.getType().toString());
		result.put(GUILD_ID, guildVO.getId());
		result.put(GUILD_NAME, guildVO.getName());
		result.put(BOSS_CID, e.getParam(EvtParamType.CID, 0));
		result.put(HURT, e.getParam(EvtParamType.LONG_VAL, 0L));
		result.put(LF, e.getParam(EvtParamType.LF, 0L));
		return result;
	}

}
