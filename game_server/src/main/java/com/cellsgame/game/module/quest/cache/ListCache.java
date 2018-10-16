/**
 * 
 */
package com.cellsgame.game.module.quest.cache;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.quest.bo.QuestItemProc;
import com.cellsgame.game.module.quest.csv.AQuestCfg;
import com.cellsgame.game.module.quest.csv.IQuestItemCfg;
import com.cellsgame.game.module.quest.vo.QuestHolder;

/**
 * @author peterveron
 *
 */
public class ListCache extends AQuestCfgCache {
	
	private List<AQuestCfg> cache = GameUtil.createList();
	
	/**
	 * @see com.cellsgame.game.module.quest.cache.AQuestCfgCache#getQuestCfgByEvt(com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public Collection<AQuestCfg> getQuestCfgByEvt(QuestHolder holder, GameEvent evt) {
		return cache;
	}
	

	/**
	 * @see com.cellsgame.game.module.quest.cache.AQuestCfgCache#add(com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.csv.AQuestCfg)
	 */
	@Override
	public void add(IQuestItemCfg item, AQuestCfg cfg) {
		cache.add(cfg);
	}

	/**
	 * @see com.cellsgame.game.module.quest.cache.AQuestCfgCache#setProc(com.cellsgame.game.module.quest.bo.QuestItemProc)
	 */
	@Override
	public void setProc(QuestItemProc<?, ?> proc) {
	}

	/**
	 * @see com.cellsgame.game.module.quest.cache.AQuestCfgCache#setComparator(java.util.Comparator)
	 */
	@Override
	public void setComparator(Comparator ... c) {
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return cache.toString();
	}

	
}
