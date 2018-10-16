package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.msg.MsgFactoryActivity;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityGroupDataVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ExchangeBevProcess extends ABevProcess {

	private static String InputAtt_NUM = "num";

	private static org.slf4j.Logger log = LoggerFactory.getLogger(ExchangeBevProcess.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act,
					   int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) {
		if(player == null) return 0;
		if(bev.getFuncs() == null || bev.getFuncs().isEmpty()){
			log.error("Activity execBev ExchangeBevProcess funcs is empty ...");
			return 0;
		}
		if(bev.getCost() == null || bev.getCost().isEmpty()){
			log.error("Activity execBev ExchangeBevProcess cost is empty ...");
			return 0;
		}
		Integer num = (Integer)inputAtts.get(InputAtt_NUM);
		num = num == null ? 1 : num;
		Map prizeMap = GameUtil.createSimpleMap();
        // 奖励
		FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
		for(int i = 0; i < num; i ++){
			executor.addSyncFunc(bev.getCost());
			executor.addSyncFunc(bev.getFuncs());
		}
		executor.exec(parent, prizeMap, player);
        MsgFactoryActivity.instance().createActivityPrizeMsg(parent, prizeMap);
        return num;
	}

	@Override
	protected void listener(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, ActivityBev bev, ActivityGroupDataVO groupDataVO, GameEvent event) {

	}

	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{};
	}

}
