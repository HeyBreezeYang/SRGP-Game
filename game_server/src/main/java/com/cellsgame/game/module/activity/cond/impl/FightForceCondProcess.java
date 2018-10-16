package com.cellsgame.game.module.activity.cond.impl;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.vo.ActivityCondRecVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;


public class FightForceCondProcess extends ACondProcess {

	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EvtTypePlayer.FightForce};
	}

	@Override
	public boolean recordCondData(Map<?, ?> parent, ActivityVO act, ActivityCond cond,
			ActivityCondRecVO condRecVO, PlayerVO player, GameEvent event) {
		long before = event.getParam(EvtParamType.BEFORE, 0L);
		long after = event.getParam(EvtParamType.AFTER, 0L);
		long change = after - before;
		if(change < 0) return false;
		condRecVO.setFinishData(Math.min(condRecVO.getFinishData() + change, cond.getValue()));
		return true;
	}

}
