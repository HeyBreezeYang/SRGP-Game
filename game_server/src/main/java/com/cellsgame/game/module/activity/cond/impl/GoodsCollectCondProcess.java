package com.cellsgame.game.module.activity.cond.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import org.springframework.stereotype.Component;

import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.cons.EvtTypeActivity;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.vo.ActivityCondRecVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.cons.EventTypeDepot;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.goods.csv.QuestCollectible;
import com.cellsgame.game.module.player.vo.PlayerVO;


public class GoodsCollectCondProcess extends ACondProcess {


	private static String GoodsType = "gType";
	private static String GoodsCid = "gCid";
	
	@Resource
	private DepotBO depotBO;
	
	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EventTypeDepot.Goods, EvtTypePlayer.EnterGame, EvtTypeActivity.AcceptNewActivity, EvtTypeActivity.RefActivity};
	}

	@Override
	public boolean recordCondData(Map<?, ?> parent, ActivityVO act, ActivityCond cond,
			ActivityCondRecVO condRecVO, PlayerVO player, GameEvent event) {
    	int tGoodsCid = cond.getIntParam(GoodsCid);
		int tGoodsType = cond.getIntParam(GoodsType);
		if(tGoodsCid <= 0 && tGoodsType <= 0) return false;
		Enum<?> o = event.getType();
        if (o instanceof EvtTypeActivity) {
			if(tGoodsCid > 0){
				int goodsNum = depotBO.getGoodsQuantity(player, tGoodsCid);
				condRecVO.setFinishData(Math.min(goodsNum, cond.getValue()));
				return true;
			}
			if(tGoodsType > 0){
				int goodsNum = depotBO.getGoodsQuantity(player, tGoodsType);
				condRecVO.setFinishData(Math.min(goodsNum, cond.getValue()));
				return true;
			}
			return false;
        }else{
			int goodsCid = event.getParam(EvtParamType.GOODS_CID, 0);
			long before = event.getParam(EvtParamType.BEFORE, 0L);
			long after = event.getParam(EvtParamType.AFTER, 0L);
			int change = (int)(before - after);
			if(change <= 0) return false;
			if(tGoodsCid > 0 && tGoodsCid != goodsCid)
				return false;
			if(tGoodsType > 0){
				BaseCfg config = event.getParam(EvtParamType.CONFIG, null);
				if(config == null) return false;
				if(!(config instanceof QuestCollectible)) return false;
				if(((QuestCollectible)config).getCollectType() != tGoodsType) return false;
			}
			condRecVO.setFinishData(Math.min(condRecVO.getFinishData() + change, cond.getValue()));
			return true;
        }
	}


}
