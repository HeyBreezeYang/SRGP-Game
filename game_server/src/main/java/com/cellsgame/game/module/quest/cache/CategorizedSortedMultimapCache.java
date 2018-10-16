/**
 * 
 */
package com.cellsgame.game.module.quest.cache;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.quest.bo.QuestItemProc;
import com.cellsgame.game.module.quest.csv.AQuestCfg;
import com.cellsgame.game.module.quest.csv.IQuestItemCfg;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

/**
 * @author peterveron
 *
 */
public class CategorizedSortedMultimapCache<T> extends AQuestCfgCache {

	private Comparator<T> c;
	
	private Map<Object,Multimap<T, AQuestCfg>> cache = GameUtil.createSimpleMap();
	
	private QuestItemProc<?,?> proc;
	
	
	public void setProc(QuestItemProc<?,?> dealer){
		this.proc = dealer;
	}
	
	public void setComparator(Comparator ... c){
		this.c = c[0];
	}
	
	
	
	private Multimap<T,AQuestCfg> getOrCreMultimap(Object category){
		Multimap<T, AQuestCfg> multimap = cache.get(category);
		if(multimap == null)
			cache.put(category, multimap =  Multimaps.newMultimap(new TreeMap<T,Collection<AQuestCfg>>(c), ()-> GameUtil.createList()));
		return multimap;
	}
	
	/**
	 * @see com.cellsgame.game.module.quest.cache.AQuestCfgCache#getQuestCfgByEvt(com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public Collection<AQuestCfg> getQuestCfgByEvt(QuestHolder holder, GameEvent evt) {
		Object category = proc.getSearchCategory(holder,evt);
		if(category == null){
			Collection<AQuestCfg> ret = GameUtil.createList();
			Collection<Multimap<T, AQuestCfg>> maps = cache.values();
			for (Multimap<T, AQuestCfg> map : maps) {
				ret.addAll(map.values());
			}
			return ret;
		}
		Multimap<T, AQuestCfg> multimap = getOrCreMultimap(category);
		T searchKey = proc.getSearchKey(holder,evt);
		if(searchKey == null){
			return multimap.values();
		}
		SortedMap<T, Collection<AQuestCfg>> map = (SortedMap) multimap.asMap();
		SortedMap<T, Collection<AQuestCfg>> tgtMap = map.tailMap(searchKey);
		Collection<Collection<AQuestCfg>> tgtCs = tgtMap.values();
		List<AQuestCfg> result = GameUtil.createList();
		for (Collection<AQuestCfg> c : tgtCs) {
			result.addAll(c);
		}
		return result;
	}
	

	/**
	 * @see com.cellsgame.game.module.quest.cache.AQuestCfgCache#add(com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.csv.AQuestCfg)
	 */
	@Override
	public void add(IQuestItemCfg item, AQuestCfg cfg) {
		Multimap<T, AQuestCfg> multimap = getOrCreMultimap(proc.extractCacheCategory(item));
		multimap.put(proc.extractCacheKey(item), cfg);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return cache.toString();
	}



}
