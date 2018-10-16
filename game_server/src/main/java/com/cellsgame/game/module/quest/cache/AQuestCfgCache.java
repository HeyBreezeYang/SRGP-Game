package com.cellsgame.game.module.quest.cache;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.quest.bo.QuestItemProc;
import com.cellsgame.game.module.quest.csv.AQuestCfg;
import com.cellsgame.game.module.quest.csv.IQuestItemCfg;
import com.cellsgame.game.module.quest.vo.QuestHolder;

/**
 * 任务缓存抽象类
 * 需要根据具体的条件或者行为类型来实现缓存数据结构
 * 以达到存储空间以及搜索时间两方面的要求
 * @author peterveron
 *
 */
public abstract class AQuestCfgCache {
	
	private static Map<Integer,AQuestCfg> questConfigs = GameUtil.createMap();
	
	public static void addQuestCfg(AQuestCfg cfg){
		questConfigs.put(cfg.getId(), cfg);
	}
	
	public static AQuestCfg getQuestCfg(int cid){
		return questConfigs.get(cid);
	}
	
	public abstract void setProc(QuestItemProc<?,?> proc);
	
	public abstract void setComparator(Comparator ... c);
	
	public abstract Collection<AQuestCfg> getQuestCfgByEvt(QuestHolder holder, GameEvent evt);
	
	
	public abstract void add(IQuestItemCfg item, AQuestCfg cfg);
	
}
