package com.cellsgame.game.module.quest.cons;

import java.util.Collection;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.module.quest.bo.BehavProc;
import com.cellsgame.game.module.quest.bo.impl.BProcBasic;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public enum BehavType {
	BASIC(1,new BProcBasic()),
	;
	
	/**
	 * 提供根据事件类型反向映射关注其的条件类型 
	 */
	private static Multimap<EvtType,BehavType> evtBehavMapping = HashMultimap.create();
	static{
		BehavType[] values = BehavType.values();
		for (BehavType behavType : values) {
			EvtType[] evtTypes = behavType.getProc().getConcernType();
			if(evtTypes == null)
				continue;
			for (EvtType evtType : evtTypes) {
				evtBehavMapping.put(evtType, behavType);
			}
		}
	}
	
	
	
	public static Collection<BehavType> getBehavTypesByEvtType(EvtType etype){
		return evtBehavMapping.get(etype);
	}
	
	
	private BehavType(int type, BehavProc proc){
		this.type = type;
		this.proc = proc;
		this.proc.setBehavType(this);
	
	}
	
	/** 类型编号*/
	private int type;
	
	/** 该类型条件对应的条件处理器*/
	private BehavProc proc;
	
//	/** 自动行为中包含该类型行为的任务缓存, 需要在配置对象加载过程中进行填充*/
//	private AQuestCfgCache autoBehavQuestCfgs;
//	
//	/** 手动行为中包含该类型行为的任务缓存, 需要在配置对象加载过程中进行填充*/
//	private AQuestCfgCache behavQuestCfgs;
	
	
	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}

	public BehavProc getProc() {
		return proc;
	}


	public void setProc(BehavProc proc) {
		this.proc = proc;
	}


//	public AQuestCfgCache getAutoBehavQuestCfgs() {
//		return autoBehavQuestCfgs;
//	}
//
//
//	public void setAutoBehavQuestCfgs(AQuestCfgCache autoBehavQuestCfgs) {
//		this.autoBehavQuestCfgs = autoBehavQuestCfgs;
//	}
//
//
//	public AQuestCfgCache getBehavQuestCfgs() {
//		return behavQuestCfgs;
//	}
//
//
//	public void setBehavQuestCfgs(AQuestCfgCache behavQuestCfgs) {
//		this.behavQuestCfgs = behavQuestCfgs;
//	}

	
	
}
