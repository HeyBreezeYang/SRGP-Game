/**
 * 
 */
package com.cellsgame.game.module.quest.bo.impl;

import java.util.Map;

import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.quest.bo.BehavProc;
import com.cellsgame.game.module.quest.csv.BehavCfg;
import com.cellsgame.game.module.quest.vo.BooleanRecProcItemVO;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.cellsgame.game.module.quest.vo.QuestProcVO;

/**
 * @author peterveron
 *
 */
public class BProcBasic extends BehavProc<BooleanRecProcItemVO> {

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#getProcItemClass()
	 */
	@Override
	public Class<BooleanRecProcItemVO> getProcItemClass() {
		
		return BooleanRecProcItemVO.class;
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#createProcItem(com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestHolder)
	 */
	@Override
	public BooleanRecProcItemVO createProcItem(BehavCfg qCfg, QuestHolder qHolder) {
		return new BooleanRecProcItemVO();
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#satisfied(com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO)
	 */
	@Override
	public boolean satisfied(BehavCfg cfg, BooleanRecProcItemVO item) {
		return item.isRec();
	}

	/**
	 * @see com.cellsgame.game.module.quest.bo.QuestItemProc#listenEvt(java.util.Map, com.cellsgame.game.module.quest.csv.IQuestItemCfg, com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO, com.cellsgame.game.module.quest.vo.QuestProcVO, com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public boolean listenEvt(Map parent, BehavCfg itemCfg, BooleanRecProcItemVO procItem, QuestProcVO qProcVO, QuestHolder qHolder, GameEvent event) {
		return false;
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
	public <V> V getCacheKey(BehavCfg item) {
		return null;
	}

	/**
	 * 
	 * @see com.cellsgame.game.module.quest.bo.BehavProc#execBehavRec(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.quest.vo.QuestHolder, com.cellsgame.game.module.quest.csv.BehavCfg, com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO)
	 */
	@Override
	public boolean execBehavRec(CMD cmd, QuestHolder qHolder, BehavCfg behavCfg, BooleanRecProcItemVO procItemVO) {
		if(procItemVO.isRec())
			return false;
		procItemVO.setRec(true);
		return true;
	}

	/**
	 * @see com.cellsgame.game.core.event.EvtNoticer#getConcernType()
	 */
	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{};
	}

}
