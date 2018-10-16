package com.cellsgame.game.module.activity.cond.impl;

import java.util.Date;
import java.util.Map;

import com.cellsgame.common.util.NDateUtil;
import org.springframework.stereotype.Component;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.vo.ActivityCondRecVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;


public class LoginCondProcess extends ACondProcess {
	
	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EvtTypePlayer.EnterGame};
	}

	@Override
	public boolean recordCondData(Map<?, ?> parent, ActivityVO act, ActivityCond cond,
			ActivityCondRecVO condRecVO, PlayerVO player, GameEvent event) {
		long lastRecordTime = condRecVO.getLastRecordTime();
		if(lastRecordTime > 0) {
			int days = NDateUtil.compareTwoDate(new Date(lastRecordTime), new Date());
			if(days <= 0) return false;
		}
		condRecVO.setFinishData(Math.min(condRecVO.getFinishData() + 1, cond.getValue()));
		condRecVO.setLastRecordTime(System.currentTimeMillis());
		return true;
	}
}
