package com.cellsgame.game.module.quest.vo;

import java.util.Map;

import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.quest.csv.AQuestCfg;

public interface QuestHolder extends EvtHolder {

	PlayerVO getPlayer();

	Integer holderID();

	Map<Integer,QuestProcVO> getQuestProcMap();

	Map<Integer,QuestRecVO> getQuestRecMap();
	
	default void rmQuestProc(QuestProcVO qProc){
		getQuestProcMap().remove(qProc.getCid());
	}
	
	default QuestProcVO addQuestProc(AQuestCfg cfg){
		QuestProcVO questProcVO = new QuestProcVO(cfg, this);
		getQuestProcMap().put(cfg.getId(), questProcVO);
		return questProcVO;
	}

	
	default QuestProcVO getQuestProcByCid(int cid){
		return getQuestProcMap().get(cid);
	}
	
	/**
	 * 根据配置获取进行中的Quest过程对象
	 * @param cfg
	 * @return
	 */
	default QuestProcVO getQuestProcByCfg(AQuestCfg cfg){
		return getQuestProcByCid(cfg.getId());
	}
	
	default boolean questOnProcOrFinish(int type, int cid){
		if(getQuestProcByCid(cid)!=null)
			return true;
		return questFin(type, cid);
	}

	default boolean questFin(int type, int cid) {
		QuestRecVO rec = getRecByType(type);
		if(rec == null)
			return false;
		return rec.getQuestRecs().containsKey(cid);
	}
	
	
	default boolean questOnPorcOrFinish(AQuestCfg questCfg){
		return questOnProcOrFinish(questCfg.getType(), questCfg.getId());
	}

	/**
	 * @param type
	 * @return
	 */
	default QuestRecVO getRecByType(int type){
		return getQuestRecMap().get(type);
	}
	
	/**
	 * @param questRecVO
	 */
	default void addNewQuestRec(QuestRecVO questRecVO){
		if(questRecVO == null)
			return;
		Map<Integer, QuestRecVO> questRecMap = getQuestRecMap();
		if(!questRecMap.containsKey(questRecVO.getType())){
			questRecMap.put(questRecVO.getType(), questRecVO);
		}		
	}
	

}
