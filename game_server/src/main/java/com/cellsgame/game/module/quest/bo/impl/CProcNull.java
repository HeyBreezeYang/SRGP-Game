/**
 * 
 */
package com.cellsgame.game.module.quest.bo.impl;

import java.util.Arrays;
import java.util.Map;

import com.cellsgame.game.core.event.EventTypeAll;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.event.GameEventManager;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.quest.bo.CditProc;
import com.cellsgame.game.module.quest.csv.CditCfg;
import com.cellsgame.game.module.quest.vo.IntRecProcItemVO;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.cellsgame.game.module.quest.vo.QuestProcVO;

/**
 * @author peterveron
 *
 */
public class CProcNull extends  CditProc<IntRecProcItemVO> {

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
		IntRecProcItemVO itemvo = new IntRecProcItemVO();
		itemvo.setRec(1);
		return itemvo;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#satisfied(com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO)
	 */
	@Override
	public boolean satisfied(CditCfg cfg, IntRecProcItemVO item) {
		return true;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#listenEvt(java.util.Map, com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO, com.cellsgame.game.module.quest.vo.QuestProcVO, com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public boolean listenEvt(Map parent, CditCfg itemCfg, IntRecProcItemVO procItem, QuestProcVO qProcVO, QuestHolder qHolder, GameEvent event) {
		return true;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#getSearchKey(com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public <V> V getSearchKey(QuestHolder holder, GameEvent evt) {
		return null;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#getCacheKey(com.cellsgame.game.module.quest.csv.IQuestItemCfg)
	 */
	@Override
	public <V> V getCacheKey(CditCfg item) {
		return null;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.CditProc#satisfiedAsAcceptCdit(com.cellsgame.game.module.quest.csv.CditCfg, com.cellsgame.game.module.quest.vo.QuestHolder)
	 */
	@Override
	public boolean satisfiedAsAcceptCdit(CditCfg cdit, QuestHolder holder) throws LogicException {
		return true;
	}

	/**
	 * @see com.cellsgame.game.core.event.EvtNoticer#getConcernType()
	 */
	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EventTypeAll.ALL};
	}

}
