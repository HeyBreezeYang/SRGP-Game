package com.cellsgame.game.module.quest.cons;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EventTypeAll;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.quest.bo.CditProc;
import com.cellsgame.game.module.quest.bo.impl.*;
import com.cellsgame.game.module.quest.cache.AQuestCfgCache;
import com.cellsgame.game.module.quest.cache.CategorizedSortedMultimapCache;
import com.cellsgame.game.module.quest.cache.DualSortedMultimapCache;
import com.cellsgame.game.module.quest.cache.ListCache;
import com.cellsgame.game.module.quest.csv.AQuestCfg;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public enum CditType {
	NULL(0,new CProcNull(),ListCache.class),
	STATS_SPEC_ID_GOODS_COLLC(1,new CProcStatsSpecIDGoodsCollc(),CategorizedSortedMultimapCache.class,Comparators.DESC_LONG),
	STATS_SPEC_ID_GOODS_USE(2,new CProcStatsSpecIDGoodsUse(),CategorizedSortedMultimapCache.class,Comparators.DESC_LONG),
	STATS_SPEC_BIZ_COUNT(3,new CProcStatsSpecBizCount(),CategorizedSortedMultimapCache.class,Comparators.DESC_LONG),
	QUEST_FIN(6,new CProcQuestFin(),ListCache.class),

	;
	
	
	/**
	 * 提供根据事件类型反向映射关注其的条件类型 
	 */
	private static Multimap<EvtType,CditType> evtCditMapping = HashMultimap.create();
	
	private static Set<CditType> allEvtTypeCdit = GameUtil.createSet();
	
	static{
		CditType[] values = CditType.values();
		for (CditType cditType : values) {
			EvtType[] evtTypes = cditType.getProc().getConcernType();
			if(evtTypes == null)
				continue;
			for (EvtType evtType : evtTypes) {
				if(evtType == EventTypeAll.ALL)
					allEvtTypeCdit.add(cditType);
				else
					evtCditMapping.put(evtType, cditType);
			}
		}
	}
	
	public static Collection<CditType> getCditTypesByEvtType(EvtType etype){
		 Collection<CditType> cdits = evtCditMapping.get(etype);
		 Set<CditType> ret = GameUtil.createSet();
		 if(cdits!=null)
			 ret.addAll(cdits);
		 ret.addAll(allEvtTypeCdit);
		 return ret;
	}
	
	
	private CditType(int type, CditProc proc, Class<? extends AQuestCfgCache> cacheClass, Comparator ... c){
		this.type = type;
		this.proc = proc;
		this.proc.setCditType(this);
		try {
			this.acceptableQuestCfgs = cacheClass.newInstance();
			this.acceptableQuestCfgs.setProc(proc);
			if(c!=null){
				this.acceptableQuestCfgs.setComparator(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	/** 类型编号*/
	private int type;
	
	/** 该类型条件对应的条件处理器*/
	private CditProc proc;
	
	/** 开始条件中包含该类型条件的任务缓存, 需要在配置对象加载过程中进行填充*/
	private AQuestCfgCache acceptableQuestCfgs;
	
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public CditProc getProc() {
		return proc;
	}

	public void setProc(CditProc proc) {
		this.proc = proc;
	}
	
	public EvtType[] getConcernEvtType(){
		return proc.getConcernType();
	}

	public AQuestCfgCache getAcceptableQuestCfgCache() {
		return acceptableQuestCfgs;
	}

	public void setCfgCache(AQuestCfgCache cfgCache) {
		this.acceptableQuestCfgs = cfgCache;
	}

	
	public Collection<AQuestCfg> getAcceptableQuestCfgsByEvt(QuestHolder holder, GameEvent evt){
		return acceptableQuestCfgs.getQuestCfgByEvt(holder, evt);
	}

	
}

