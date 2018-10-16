package com.cellsgame.game.module.quest.bo;

import java.util.Map;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.quest.csv.CditCfg;
import com.cellsgame.game.module.quest.vo.LongRecProcItemVO;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.cellsgame.game.module.quest.vo.QuestProcVO;
import com.cellsgame.game.module.stats.cons.EvtTypeStats;
import com.cellsgame.game.module.stats.vo.StatsVO;

public abstract class CProcStats extends CditProc<LongRecProcItemVO> {
	

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#getProcItemClass()
	 */
	@Override
	public Class<LongRecProcItemVO> getProcItemClass() {
		return LongRecProcItemVO.class;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#createProcItem(com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestHolder)
	 */
	@Override
	public LongRecProcItemVO createProcItem(CditCfg cditCfg, QuestHolder qHolder) {
		Long now = 0L;
		switch(cditCfg.getRecMode()){
		case CditCfg.REC_MODE_EARLY_REC:
			StatsVO svo = qHolder.getPlayer().getStatsVO();
			now = svo.getData(getStatisticType(cditCfg));
			break;
		case CditCfg.REC_MODE_LATER_REC:
			break;
		}
		LongRecProcItemVO item = new LongRecProcItemVO();
		item.setRec(now);
		return item;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#satisfied(com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO)
	 */
	@Override
	public boolean satisfied(CditCfg cfg, LongRecProcItemVO item) {
		return item.getRec()>=cfg.getVal();
	}
	
	/**
	 * @see com.cellsgame.game.module.quest.bo.CditProc#satisfiedAsAcceptCdit(com.cellsgame.game.module.quest.csv.CditCfg, com.cellsgame.game.module.quest.vo.QuestHolder)
	 */
	@Override
	public boolean satisfiedAsAcceptCdit(CditCfg cdit, QuestHolder holder) throws LogicException {
		PlayerVO pvo = holder.getPlayer();
		Long now = pvo.getStatsVO().getData(getStatisticType(cdit));
		return now>=cdit.getVal();
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#listenEvt(java.util.Map, com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO, com.cellsgame.game.module.quest.vo.QuestProcVO, com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public boolean listenEvt(Map parent, CditCfg cditCfg, LongRecProcItemVO procItem, QuestProcVO qProcVO, QuestHolder qHolder, GameEvent event) {
		if(!match(cditCfg,event))
			return false;
		Long after = event.getParam(EvtParamType.AFTER);
		Long before = event.getParam(EvtParamType.BEFORE);
		if(after == null || before == null)
			return false;
		Long delta = after - before;
		if(delta == 0)
			return false;
		switch(cditCfg.getRecMode()){
		case CditCfg.REC_MODE_EARLY_REC:
			procItem.setRec(after);
			break;
		case CditCfg.REC_MODE_LATER_REC:
			procItem.setRec(procItem.getRec()+ delta);
			break;
		}
		return true;
	}


	
	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#getCacheKey(com.cellsgame.game.module.quest.csv.IQuestItemCfg)
	 */
	@Override
	public <V> V getCacheKey(CditCfg item) {
		return (V)(Long.valueOf(item.getVal()));
	}
	
	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#getSearchKey(com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public <V> V getSearchKey(QuestHolder holder, GameEvent evt) {
		return (V) evt.getParam(EvtParamType.AFTER);
	}


	/**
	 * 根据配置对象获取统计类型, 允许不参考该配置对象直接返回统计类型
	 * @param cdit
	 * @return
	 */
	public int getStatisticType(CditCfg cdit){
		return getEvtTypeStats().getStatsType();
	}

	/**
	 * 发生的事件是否匹配该配置
	 * @param cditCfg
	 * @param event
	 * @return
	 */
	protected boolean match(CditCfg cditCfg, GameEvent event){
		return true;
	}
	
	public abstract EvtTypeStats getEvtTypeStats();
	
}

