package com.cellsgame.game.module.activity.cond.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.cache.CacheActivityGlobalData;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.vo.ActivityCondRecVO;
import com.cellsgame.game.module.activity.vo.ActivityGlobalVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.cons.EventTypeDepot;
import com.cellsgame.game.module.player.vo.PlayerVO;


public class CountCurrencyCondProcess extends ACondProcess {


	private static String Op = "op";
	private static String CurrencyType = "cType";
	private static String Pay = "pay";
	
	
	private static final int OP_Get = 1;
	private static final int OP_Use = 2;
	
	@Resource
	private DepotBO depotBO;
	
	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EventTypeDepot.Currency};
	}

	@Override
	public boolean recordCondData(Map<?, ?> parent, ActivityVO act, ActivityCond cond,
			ActivityCondRecVO condRecVO, PlayerVO player, GameEvent event) {
		int tCurrencyType = cond.getIntParam(CurrencyType);
		int op = cond.getIntParam(Op);
		boolean tPay = cond.getBooleanParam(Pay);
		if(tCurrencyType <= 0) return false;
		int cType = event.getParam(EvtParamType.CURRENCY_TYPE, 0);
		long before = event.getParam(EvtParamType.BEFORE, 0L);
		long after = event.getParam(EvtParamType.AFTER, 0L);
		boolean pay = event.getParam(EvtParamType.PAY, false);
		long change = after - before;
		if(op != OP_Get && op != OP_Use) return false;
		if(op == OP_Get && change <= 0) return false;
		if(op == OP_Use && change >= 0) return false;
		if(cType <= 0 || cType != tCurrencyType) return false;
		if(tPay && !pay) return false;
		ActivityGlobalVO globalVO = CacheActivityGlobalData.get().getGlobalData(act, player);
		Long currNum = globalVO.getCurrencyData().get(tCurrencyType);
		if(currNum == null) currNum = 0L;
		condRecVO.setFinishData(condRecVO.getFinishData() + Math.abs(change));
		globalVO.getCurrencyData().put(tCurrencyType, currNum + Math.abs(change));
		getActivityGlobalDAO().save(globalVO);
		return false;
	}


}
