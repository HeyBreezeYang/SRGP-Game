package com.cellsgame.game.module.quest.bo;

import java.util.Map;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.event.EvtNoticer;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.quest.cons.CditType;
import com.cellsgame.game.module.quest.cons.EventTypeQuest;
import com.cellsgame.game.module.quest.csv.CditCfg;
import com.cellsgame.game.module.quest.msg.MsgFactoryQuest;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.cellsgame.game.module.quest.vo.QuestProcVO;
import com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO;
import com.cellsgame.orm.BaseDAO;

public abstract class CditProc<T extends ProcItemVO> extends EvtNoticer implements QuestItemProc<T,CditCfg>{
	private CditType cditType;

	private BaseDAO<QuestProcVO> questProcDAO;
	

	public CditProc(){
		questProcDAO = SpringBeanFactory.getBean("questProcDAO");
	}
	
	public CditType getCditType() {
		return cditType;
	}

	public void setCditType(CditType cditType) {
		this.cditType = cditType;
	}

	public void listenEvtAsFinCdit(Map parent, CMD cmd, CditCfg cditCfg, QuestHolder qHolder, QuestProcVO qProcVO, GameEvent event) throws LogicException{
		ProcItemVO procItem = qProcVO.getFinCditProcs().get(cditCfg.getIx());
		if(procItem == null){
			qProcVO.addFinCditProcVO(cditCfg.getIx(), procItem = createProcItem(cditCfg, qHolder));
			parent = msgFactory.getQuestProcUpdateMsg(parent, qProcVO);
			questProcDAO.save(qProcVO);
		}
		if(listenEvt(parent, cditCfg, (T)procItem, qProcVO, qHolder, event)){//事件触发条件变更时
			parent = msgFactory.getQuestProcUpdateMsg(parent, qProcVO);
			//做全条件检查如果条件达成触发条件完成事件
			checkAllFinCditsAndTriggerEvt(parent, cmd, qHolder, qProcVO);
			questProcDAO.save(qProcVO);
		}
	}
	
	public static void checkAllFinCditsAndTriggerEvt(Map parent, CMD cmd, QuestHolder qHolder, QuestProcVO qProcVO){
		if(qProcVO.finCditsSatisfied()){
			//触发条件满足事件
			EventTypeQuest.FIN_CDITS_SATISFIED.happen(parent, cmd, qHolder, EvtParamType.QUEST_CID.val(qProcVO.getCid()));
		}
	}
	
	/**
	 * 该类型条件作为接受条件时是否满足
	 * @param cdit 条件配置对象
	 * @param holder 任务持有对象
	 * @return
	 * @throws LogicException
	 */
	public abstract boolean satisfiedAsAcceptCdit(CditCfg cdit, QuestHolder holder) throws LogicException;

	


	
}
