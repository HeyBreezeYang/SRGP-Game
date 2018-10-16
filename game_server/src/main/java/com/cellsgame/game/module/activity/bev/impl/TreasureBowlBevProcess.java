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
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class TreasureBowlBevProcess extends ABevProcess {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(TreasureBowlBevProcess.class);

	private static String Cost = "costs";
	private static String MinPrize = "minPrzs";
	private static String MaxPrize = "maxPrzs";

	@Override
	public int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act,
					   int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) {
		ActivityBevRecVO recVO = bevRecVOs.get(ActivityScopeType.Personal);
		if(recVO == null) {
			log.error("TreasureBowlBevProcess execBev ActivityBevRecVO empty Personal scope");
			return 0;
		}
		String[] costs = bev.getStringParam(Cost, "").split("\\|");
		String[] minPrzs = bev.getStringParam(MinPrize, "").split("\\|");
		String[] maxPrzs = bev.getStringParam(MaxPrize, "").split("\\|");
		if(costs.length != minPrzs.length && costs.length != maxPrzs.length) {
			log.error("TreasureBowlBevProcess execBev params error , costs.length : {}, minPrzs.length : {}, maxPrzs.length : {}",
					costs.length, minPrzs.length, maxPrzs.length);
			return 0;
		}
		int idx = recVO.getExecNum();
		if(recVO.getExecNum() >= costs.length){
			idx = costs.length - 1;
			log.warn("TreasureBowlBevProcess execBev params , execNum >= costs.length");
		}
		int costTre = Integer.parseInt(costs[idx]);
		int minPrzTre = Integer.parseInt(minPrzs[idx]);
		int maxPrzTre = Integer.parseInt(maxPrzs[idx]);
		if(maxPrzTre <= minPrzTre){
			log.warn("TreasureBowlBevProcess execBev params , {}", bev.getParam());
			log.warn("TreasureBowlBevProcess execBev params , maxPrzTre <= minPrzTre, idx : {}, execNum : {}， minPrzTre : {}, maxPrzTre : {}", idx, recVO.getExecNum(), minPrzTre, maxPrzTre);
			return 0;
		}
		FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
		List<FuncConfig> funcs = GameUtil.createList();
		funcs.add(new FuncConfig(
				SyncFuncType.ChangeCur.getType(),
				CurrencyType.TRE.getType(),
				-costTre
		));
		executor.addSyncFunc(funcs);
		executor.exec(parent, player);
		Map prizeMap = GameUtil.createSimpleMap();
		executor = FuncsExecutorsType.Base.getExecutor(cmd);
		funcs = GameUtil.createList();
		int przTre = GameUtil.r.nextInt(maxPrzTre - minPrzTre) + minPrzTre;
		funcs.add(new FuncConfig(
				SyncFuncType.ChangeCur.getType(),
				CurrencyType.TRE.getType(),
				przTre
		));
		executor.addSyncFunc(funcs);
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
