package com.cellsgame.game.module.activity.bev.impl;

import java.util.Map;

import com.cellsgame.game.module.activity.vo.ActivityGroupDataVO;
import com.cellsgame.game.module.func.FuncsExecutor;
import org.slf4j.LoggerFactory;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.msg.MsgFactoryActivity;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class FuncBevProcess extends ABevProcess {


	private static org.slf4j.Logger log = LoggerFactory.getLogger(FuncBevProcess.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act,
					   int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) {
		if(player == null) return 0;
		if(bev.getFuncs() == null || bev.getFuncs().isEmpty()){
			log.error("Activity execBev FuncBevProcess funcs is empty ...");
			return 0;
		}
		Map prizeMap = GameUtil.createSimpleMap();
        // 奖励
		FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd).addSyncFunc(bev.getFuncs());
		executor.exec(parent, prizeMap, player);
        MsgFactoryActivity.instance().createActivityPrizeMsg(parent, prizeMap);
        return 1;
	}

	@Override
	protected void listener(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, ActivityBev bev, ActivityGroupDataVO groupDataVO, GameEvent event) {

	}

	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{};
	}

}
