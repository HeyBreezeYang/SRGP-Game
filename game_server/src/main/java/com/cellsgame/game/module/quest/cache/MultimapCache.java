///**
// * 
// */
//package com.cellsgame.game.module.quest.cache;
//
//import java.util.Collection;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Set;
//
//import com.cellsgame.common.util.GameUtil;
//import com.cellsgame.game.core.event.GameEvent;
//import com.cellsgame.game.module.quest.bo.QuestItemProc;
//import com.cellsgame.game.module.quest.csv.AQuestCfg;
//import com.cellsgame.game.module.quest.csv.IQuestItemCfg;
//import com.cellsgame.game.module.quest.vo.QuestHolder;
//import com.google.common.collect.Multimap;
//import com.google.common.collect.Multimaps;
//
///**
// * @author peterveron
// *
// */
//public class MultimapCache<T> extends AQuestCfgCache {
//	
//	private Multimap<T, AQuestCfg> cache = Multimaps.newMultimap(new HashMap<T,Collection<AQuestCfg>>(), ()-> GameUtil.createList());
//	
//	private QuestItemProc<?,?> proc;
//	
//	
//	public void add(IQuestItemCfg item, AQuestCfg cfg){
//		cache.put(proc.extractCacheKey(item), cfg);
//	}
//	
//	@Override
//	public Collection<AQuestCfg> getQuestCfgByEvt(QuestHolder holder, GameEvent evt) {
//		T searchKey = proc.getSearchKey(holder,evt);
//		if(searchKey == null)
//			return cache.values();
//		Collection<AQuestCfg> result = cache.get(searchKey);
//		return result;
//	}
//	
//	
//
//	/**
//	 * @see com.cellsgame.game.module.quest.cache.AQuestCfgCache#setProc(com.cellsgame.game.module.quest.bo.QuestItemProc)
//	 */
//	@Override
//	public void setProc(QuestItemProc<?, ?> proc) {
//		this.proc = proc;
//	}
//
//	/**
//	 * @see com.cellsgame.game.module.quest.cache.AQuestCfgCache#setComparator(java.util.Comparator)
//	 */
//	@Override
//	public void setComparator(Comparator ... c) {
//	}
//
//	
//	/**
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		return cache.toString();
//	}
//}
