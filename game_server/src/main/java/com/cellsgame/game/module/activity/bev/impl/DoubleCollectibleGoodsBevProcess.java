package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.cons.SysType;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.msg.MsgFactoryActivity;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityGroupDataVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.PrizeConstant;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.csv.QuestCollectible;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;
import java.util.Map.Entry;

public class DoubleCollectibleGoodsBevProcess extends ABevProcess {

	private static String Type = "type";
	private static String SubType = "subType";
	private static String PrizeType = "pType";
	private static String PrizeParam = "pParam";
	
	private static final int PrizeType_GoodsID = 1;
	private static final int PrizeType_GoodsType = 2;
	
	
	@Override
	public int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act,
					   int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) {
		if(player == null) return 0;
		if(event == null) return 0;
		int type = bev.getIntParam(Type);
		int subType = bev.getIntParam(SubType);
		SysType sysType = event.getParam(EvtParamType.SYS_TYPE);
		Map prizeMap = event.getParam(EvtParamType.PRIZE);
		if(sysType == null || prizeMap == null || prizeMap.isEmpty()) return 0;
		if(sysType.check(event, type, subType)){
			int prizeType = bev.getIntParam(PrizeType);
			int prizeParam = bev.getIntParam(PrizeParam);
			if(prizeType == PrizeType_GoodsID){
				Map<Integer, Integer> result = PrizeConstant.getCollectibleGoodsByID(prizeMap, prizeParam);
				goodsPrizeExec(parent, cmd, result, player);
			}
			if(prizeType == PrizeType_GoodsType){
				Map<Integer, Integer> result = PrizeConstant.getCollectibleGoodsByType(prizeMap, prizeParam);
				goodsPrizeExec(parent, cmd, result, player);
			}
			return 1;
		}
        return 0;
	}

	private void goodsPrizeExec(Map<?, ?> parent, CMD cmd, Map<Integer, Integer> result, PlayerVO player) {
		FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
		for(Entry<Integer, Integer> entry : result.entrySet()){
			Integer goodsId = entry.getKey();
			Integer num = entry.getValue();
			QuestCollectible goodsConfig = CacheGoods.getQuestCollectibleMap().get(goodsId);
			exec.addSyncFunc(SyncFuncType.ChangeGoods, goodsId, num);
		}
		Map actPrize = GameUtil.createSimpleMap();
		exec.exec(parent, actPrize, player);
        MsgFactoryActivity.instance().createActivityPrizeMsg(parent, actPrize);
	}

	@Override
	protected void listener(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, ActivityBev bev, ActivityGroupDataVO groupDataVO, GameEvent event) {

	}
	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EvtTypePlayer.Prize};
	}

}
