/**
 * 
 */
package com.cellsgame.game.module.quest.bo;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.quest.cache.CategorizedSortedMultimapCache;
import com.cellsgame.game.module.quest.csv.CditCfg;
import com.cellsgame.game.module.quest.vo.QuestHolder;
/**
 * 双参数统计条件处理器
 * 对应cache为CategorizedSortedKeyMultimapCache
 * @alsosee {@link CategorizedSortedMultimapCache}
 * @author peterveron
 */
public abstract class CProcBiParamStats extends CProcStats{
	/**
	 * 物品统计条件中类别为物品id，存储在CditCfg的param属性中 
	 */
	@Override
	public Object getCacheCategory(CditCfg item) {
		return item.getParam();
	}
	
	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#getSearchCategory(com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public Object getSearchCategory(QuestHolder holder, GameEvent evt) {
		return getEvtTypeStats().unwrapStatsType(evt.getParam(EvtParamType.TYPE));
	}
	
	
	/**
	 * @see com.cellsgame.game.module.quest.bo.CProcStats#getStatisticType(com.cellsgame.game.module.statistic.vo.StatisticVO)
	 */
	@Override
	public int getStatisticType(CditCfg cdit) {
		return getEvtTypeStats().getStatsType()+cdit.getParam();
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.CProcStats#match(com.cellsgame.game.module.quest.csv.CditCfg, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	protected boolean match(CditCfg cditCfg, GameEvent event) {

		Integer cid = getEvtTypeStats().unwrapStatsType(event.getParam(EvtParamType.TYPE));
		if(cid == null)
			return false;
		return cditCfg.getParam() == cid.intValue();
	}
}
