package com.cellsgame.game.module.activity.cond.impl;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.vo.ActivityCondRecVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.guild.cons.EvtTypeGuild;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;


public class GuildBossHurtCondProcess extends ACondProcess {

	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EvtTypeGuild.FightBoss};
	}

	@Override
	public boolean recordCondData(Map<?, ?> parent, ActivityVO act, ActivityCond cond,
			ActivityCondRecVO condRecVO, PlayerVO player, GameEvent event) {
		long addVal = event.getParam(EvtParamType.LONG_VAL, 0L);
		if(addVal <= 0) return false;
		condRecVO.setFinishData(Math.min(condRecVO.getFinishData() + addVal, cond.getValue()));
		return true;
	}

}
