package com.cellsgame.game.module.activity.cond.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.cons.EvtTypeActivity;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.vo.ActivityCondRecVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;


public class PlayerLevelCondProcess extends ACondProcess {

	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EvtTypePlayer.LevelUp, EvtTypePlayer.EnterGame, EvtTypeActivity.AcceptNewActivity, EvtTypeActivity.RefActivity};
	}

	@Override
	public boolean recordCondData(Map<?, ?> parent, ActivityVO act, ActivityCond cond,
			ActivityCondRecVO condRecVO, PlayerVO player, GameEvent event) {
		long limit = cond.getValue();
		long currRec = condRecVO.getFinishData();
		if(currRec >= limit)
			return false;
//		int currPlayerLv = player.getLevel();
//		if(currPlayerLv > currRec){
//			condRecVO.setFinishData(Math.min(currPlayerLv, limit));
//			return true;
//		}
		return false;
	}

}
