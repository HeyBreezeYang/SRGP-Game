//package com.cellsgame.game.module.quest.cache;
//
//import java.util.Collection;
//import java.util.Comparator;
//import java.util.List;
//import java.util.SortedMap;
//import java.util.TreeMap;
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
//public class SortedMultimapCache<T> extends AQuestCfgCache{
//	
//	private Multimap<T, AQuestCfg> cache;
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
//		SortedMap<T, Collection<AQuestCfg>> map = (SortedMap) cache.asMap();
//		SortedMap<T, Collection<AQuestCfg>> tgtMap = map.tailMap(searchKey);
//		Collection<Collection<AQuestCfg>> tgtCs = tgtMap.values();
//		List<AQuestCfg> result = GameUtil.createList();
//		for (Collection<AQuestCfg> c : tgtCs) {
//			result.addAll(c);
//		}
//		return result;
//	}
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
//		cache = Multimaps.newMultimap(new TreeMap<T,Collection<AQuestCfg>>(c[0]), ()-> GameUtil.createList());
//	}
//	
//	/**
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		return cache.toString();
//	}
//
//}
