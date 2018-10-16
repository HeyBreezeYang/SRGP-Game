package com.cellsgame.game.module.activity.cond.impl;

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

import javax.annotation.Resource;
import java.util.Map;


public class ChangeGoodsCondProcess extends ACondProcess {


	private static String Op = "op";
	private static String GoodsCID = "gcid";
	
	
	private static final int OP_Get = 1;
	private static final int OP_Use = 2;
	
	@Resource
	private DepotBO depotBO;
	
	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EventTypeDepot.Goods};
	}

	@Override
	public boolean recordCondData(Map<?, ?> parent, ActivityVO act, ActivityCond cond,
			ActivityCondRecVO condRecVO, PlayerVO player, GameEvent event) {
		int tGoodsCID = cond.getIntParam(GoodsCID);
		int op = cond.getIntParam(Op);
		int gcid = event.getParam(EvtParamType.GOODS_CID, 0);
		long before = event.getParam(EvtParamType.BEFORE, 0L);
		long after = event.getParam(EvtParamType.AFTER, 0L);
		int change = (int)(after - before);
		if(op != OP_Get && op != OP_Use) return false;
		if(op == OP_Get && change <= 0) return false;
		if(op == OP_Use && change >= 0) return false;
		if(tGoodsCID > 0 && tGoodsCID != gcid) return false;
		condRecVO.setFinishData(Math.min(condRecVO.getFinishData() + Math.abs(change), cond.getValue()));
		return false;
	}


}
