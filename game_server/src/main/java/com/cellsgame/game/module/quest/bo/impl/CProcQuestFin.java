/**
 * 
 */
package com.cellsgame.game.module.quest.bo.impl;

import java.util.Map;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.quest.bo.CditProc;
import com.cellsgame.game.module.quest.cons.EventTypeQuest;
import com.cellsgame.game.module.quest.csv.CditCfg;
import com.cellsgame.game.module.quest.vo.IntRecProcItemVO;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.cellsgame.game.module.quest.vo.QuestProcVO;

/**
 * @author peterveron
 *
 */
public class CProcQuestFin extends CditProc<IntRecProcItemVO> {

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#getProcItemClass()
	 */
	@Override
	public Class<IntRecProcItemVO> getProcItemClass() {
		return IntRecProcItemVO.class;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#createProcItem(com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestHolder)
	 */
	@Override
	public IntRecProcItemVO createProcItem(CditCfg qCfg, QuestHolder qHolder) {
		boolean questOnProcOrFinish = qHolder.questFin(qCfg.getParam(),(int)qCfg.getVal());
		IntRecProcItemVO procItem = new IntRecProcItemVO();
		procItem.setRec(questOnProcOrFinish?1:0);
		return procItem;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#satisfied(com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO)
	 */
	@Override
	public boolean satisfied(CditCfg cfg, IntRecProcItemVO item) {
		return item.getRec()==1;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#listenEvt(java.util.Map, com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO, com.cellsgame.game.module.quest.vo.QuestProcVO, com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public boolean listenEvt(Map parent, CditCfg itemCfg, IntRecProcItemVO procItem, QuestProcVO qProcVO, QuestHolder qHolder, GameEvent event) {
		if(qHolder.questFin(itemCfg.getParam(), (int)itemCfg.getVal())){
			procItem.setRec(1);
			return true;
		}
		return false;
	}

	/**
	 * 当事件没有参数时将返回null搜索键, 缓存集将返回全部数据
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#getSearchKey(com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public <V> V getSearchKey(QuestHolder holder, GameEvent evt) {
		Integer cid = evt.getParam(EvtParamType.QUEST_CID);
		if(cid == null)
			return null;
		return (V) Long.valueOf(0L+cid);
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#getCacheKey(com.cellsgame.game.module.quest.csv.IQuestItemCfg)
	 */
	@Override
	public <V> V getCacheKey(CditCfg item) {
		return (V)Long.valueOf(item.getVal());
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.CditProc#satisfiedAsAcceptCdit(com.cellsgame.game.module.quest.csv.CditCfg, com.cellsgame.game.module.quest.vo.QuestHolder)
	 */
	@Override
	public boolean satisfiedAsAcceptCdit(CditCfg cdit, QuestHolder holder) throws LogicException {
		return holder.questFin(cdit.getParam(), (int)cdit.getVal());
	}

	/**
	 * @see com.cellsgame.game.core.event.EvtNoticer#getConcernType()
	 */
	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EventTypeQuest.QUEST_FIN,EvtTypePlayer.EnterGame};
	}

}
