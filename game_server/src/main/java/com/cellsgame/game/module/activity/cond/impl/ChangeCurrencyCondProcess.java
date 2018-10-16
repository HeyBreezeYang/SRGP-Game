package com.cellsgame.game.module.activity.cond.impl;

import java.util.Map;


import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.vo.ActivityCondRecVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.depot.cons.EventTypeDepot;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;
import org.springframework.stereotype.Component;

public class ChangeCurrencyCondProcess extends ACondProcess {

	private static String Op = "op";
	private static String CurrencyType = "cType";
	private static String Pay = "pay";
	
	private static final int OP_Get = 1;
	private static final int OP_Use = 2;

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
		condRecVO.setFinishData(Math.min(condRecVO.getFinishData() + Math.abs(change), cond.getValue()));
		return true;
	}

	public void setFriendListDAO(BaseDAO friendListDAO) {
	}
}
