/**
 * 
 */
package com.cellsgame.game.module.quest.bo.impl;

import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.quest.bo.CProcBiParamStats;
import com.cellsgame.game.module.stats.cons.EvtTypeStats;

/**
 * 指定物品收集条件
 * 
 * @author peterveron
 * 
 */
public class CProcStatsSpecIDGoodsCollc extends CProcBiParamStats {
	
	/**
	 * @see com.cellsgame.game.core.event.EvtNoticer#getConcernType()
	 */
	@Override
	public EvtType[] getConcernType() {
		return  new EvtType[]{EvtTypeStats.SPEC_ID_GOODS_COLLC,EvtTypePlayer.EnterGame};
	}
	

	/**
	 * @see com.cellsgame.game.module.quest.bo.CProcStats#getEvtTypeStats()
	 */
	@Override
	public EvtTypeStats getEvtTypeStats() {
		return EvtTypeStats.SPEC_ID_GOODS_COLLC;
	}

}
