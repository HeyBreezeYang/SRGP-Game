package com.cellsgame.game.module.quest.bo.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import javax.annotation.Resource;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.event.EventTypeAll;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.quest.bo.BehavProc;
import com.cellsgame.game.module.quest.bo.CditProc;
import com.cellsgame.game.module.quest.bo.QuestBO;
import com.cellsgame.game.module.quest.cache.QuestCidCache;
import com.cellsgame.game.module.quest.cons.BehavType;
import com.cellsgame.game.module.quest.cons.CditType;
import com.cellsgame.game.module.quest.cons.EventTypeQuest;
import com.cellsgame.game.module.quest.cons.LoopType;
import com.cellsgame.game.module.quest.csv.AQuestCfg;
import com.cellsgame.game.module.quest.csv.BehavCfg;
import com.cellsgame.game.module.quest.csv.CditCfg;
import com.cellsgame.game.module.quest.msg.CodeQuest;
import com.cellsgame.game.module.quest.msg.MsgFactoryQuest;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.cellsgame.game.module.quest.vo.QuestProcVO;
import com.cellsgame.game.module.quest.vo.QuestRecItemVO;
import com.cellsgame.game.module.quest.vo.QuestRecVO;
import com.cellsgame.game.util.TriConsumer;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


@SuppressWarnings("rawtypes")
public class QuestBOImpl implements QuestBO{

	
	private PlayerBO playerBO;
	
	@Resource
	private BaseDAO<QuestProcVO> questProcDAO;
	
	@Resource
	private BaseDAO<QuestRecVO> questRecDAO;
	
	private MsgFactoryQuest msgFactory = MsgFactoryQuest.instance();
	
	private static EvtType[] evtType = new EvtType[]{EventTypeAll.ALL};
	

	
	@Override
	public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
		if(!(holder instanceof QuestHolder))
			return parent;
		
		QuestHolder qHolder = (QuestHolder)holder;
		/*
		 * 1.获取对此事件类型关注的所有条件类型
		 */
		Collection<CditType> concernedCditTypes = CditType.getCditTypesByEvtType(event.getType());
		/*
		 * 2.通过映射找到  接收条件组中拥有上述条件的所有任务 
		 * 如果其没有处于完成或是进行中状态, 结合事件检测
		 * 其所有接受条件, 在全部接受条件满足的情况下接受
		 */
		checkAndAccpet(parent, cmd, qHolder, event, qHolder, concernedCditTypes);
	
		/*
		 * 3.处理完成条件对事件的监听
		 */
		multiDeal(parent, event, qHolder, 
			getQuestProcVOsBySomeTypes(qHolder, concernedCditTypes, event, (questCfg,cditType)->questCfg.getFinCditBy(cditType)!=null), 
			(cditType,qProcVO,questCfg)->{
				CditProc processor = cditType.getProc();
				Collection<CditCfg> finCdits = questCfg.getFinCditBy(cditType);
				for (CditCfg cditCfg : finCdits) {
					processor.listenEvtAsFinCdit(parent, cmd, cditCfg, qHolder, qProcVO, event);
				}
			}
		);
		
		/*
		 * 处理自动行为对事件的监听
		 */
		Collection<BehavType> concernedBehavTypes = BehavType.getBehavTypesByEvtType(event.getType());
		multiDeal(parent, event, qHolder,
			getQuestProcVOsBySomeTypes(qHolder, concernedBehavTypes, event, (questCfg,behavType)->questCfg.getAutoBehavBy(behavType)!=null), 
			(behavType,qProcVO,questCfg)->{
				BehavProc processor = behavType.getProc();
				Collection<BehavCfg> autoBehavs = questCfg.getAutoBehavBy(behavType);
				for (BehavCfg cditCfg : autoBehavs) {
					processor.listenEvtAsAutoBehav(parent, cditCfg, qHolder, qProcVO, event);
				}
			}
		);
		
		/*
		 *处理手动行为对事件的监听 
		 */
		multiDeal(parent, event, qHolder, 
			getQuestProcVOsBySomeTypes(qHolder, concernedBehavTypes, event, (questCfg,behavType)->questCfg.getManualBehavBy(behavType)!=null), 
			(behavType,qProcVO,questCfg)->{
				BehavProc processor = behavType.getProc();
				Collection<BehavCfg> autoBehavs = questCfg.getManualBehavBy(behavType);
				for (BehavCfg cditCfg : autoBehavs) {
					processor.listenEvtAsManualBehav(parent, cditCfg, qHolder, qProcVO, event);
				}
			}
		);
		
		
		questFinCditsSatisfied(parent, cmd, event, qHolder);
		
		return parent;
	}
	
	
	public void update(CMD cmd, Map parent, LoopType l, QuestHolder holder){
		Collection<Integer> cids = QuestCidCache.loopTypeCidMapping.get(l);
		List<QuestProcVO> deletes = GameUtil.createList();
		List<Integer> rmCids = GameUtil.createList();
		for (Integer cid : cids) {
			QuestProcVO remove = holder.getQuestProcMap().remove(cid);
			if(remove!=null){
				deletes.add(remove);
				rmCids.add(cid);
			}
				
		}
		msgFactory.getQuestProcRmMsg(parent, rmCids);
		questProcDAO.delete(deletes);
		
		List<QuestRecVO> saves = GameUtil.createList();
		Collection<QuestRecVO> values = holder.getQuestRecMap().values();
		for (QuestRecVO recvo : values) {
			int type = recvo.getType();
			List<Integer> rm = GameUtil.createList();
			for (Integer cid : cids) {
				if(recvo.getQuestRecs().remove(cid)!= null)
					rm.add(cid);
			}
			msgFactory.getQuestRecRmMsg(parent, type, rm);
			saves.add(recvo);
		}
	
		questRecDAO.save(saves);
		EventTypeQuest.UPDATE.happen(parent, cmd, holder, EvtParamType.QUEST_LOOP_TYPE.val(l.name()));
	}

	public void questFinCditsSatisfied(Map parent, CMD cmd, GameEvent event, QuestHolder qHolder) {
		Enum<?> eType = event.getType();
		if(eType instanceof EventTypeQuest){
			switch((EventTypeQuest) eType){
				case FIN_CDITS_SATISFIED://执行自动功能
					QuestProcVO qProcVO = qHolder.getQuestProcByCid(event.getParam(EvtParamType.QUEST_CID));
					Map prizeMap = GameUtil.createSimpleMap();
					if(BehavProc.tryDoAutoBehav(cmd, parent, prizeMap, qHolder, qProcVO)){
						msgFactory.getQuestProcUpdateMsg(parent, qProcVO);
						msgFactory.getQuestPrizeMsg(parent, prizeMap);
						questProcDAO.save(qProcVO);
					}
					if(qProcVO.allBehavFin()){//
						finishQuest( cmd, parent, qHolder, qProcVO);
					}
					break;
			}
		}
	}
	
	public Map commit( CMD cmd, PlayerVO pvo, int qCid) throws LogicException{
		Map ret = GameUtil.createSimpleMap();
		QuestProcVO qProcVO = pvo.getQuestProcByCid(qCid);
		CodeQuest.NOT_IN_QUEST.throwIfTrue(qProcVO == null);
		CodeQuest.MANUAL_BEHAV_FIN.throwIfTrue(qProcVO.isManualBehavFin());//检测是否已经完成
		CodeQuest.FIN_CDIT_NOT_SATISFIED.throwIfTrue(!qProcVO.finCditsSatisfied());//检测完成条件是否达成
		Map prizeMap = GameUtil.createSimpleMap();
		if(BehavProc.tryDoManualBehav(cmd, ret, prizeMap, pvo, qProcVO)){
			
			questProcDAO.save(qProcVO);
			msgFactory.getQuestProcUpdateMsg(ret, qProcVO);
			msgFactory.getQuestPrizeMsg(ret, prizeMap);
		}
		
		if(qProcVO.allBehavFin())
			finishQuest( cmd, ret, pvo, qProcVO);
		return MsgUtil.brmAll(ret, cmd);
	}

	private void finishQuest(CMD cmd, Map parent, QuestHolder qHolder, QuestProcVO qProcVO) {
		qHolder.rmQuestProc(qProcVO);
		questProcDAO.delete(qProcVO);
		msgFactory.getQuestProcRmMsg(parent, qProcVO.getCid());
		int qType = qProcVO.getCfg().getType();
		QuestRecVO qRecVO = qHolder.getRecByType(qType);
		
		if(qRecVO == null){
			qHolder.addNewQuestRec(qRecVO = new QuestRecVO(qHolder.holderID(), qType));
		}
		QuestRecItemVO questRecItemVO = new QuestRecItemVO(qProcVO.getCid());
		//如有需要需要对QuestRecItemVO的数据进行定制
		qRecVO.getQuestRecs().put(qProcVO.getCid(),questRecItemVO);
		msgFactory.getQuestRecAddMsg(parent, qType, questRecItemVO);
		questRecDAO.save(qRecVO);
		EventTypeQuest.QUEST_FIN.happen(parent, cmd, qHolder, EvtParamType.QUEST_CID.val(qProcVO.getCid()), EvtParamType.TYPE.val(qType));
	}

	/**
	 * 批量处理
	 * @param parent
	 * @param event
	 * @param qHolder
	 * @param processing
	 * @param consumer
	 */
	private <T> void multiDeal(Map parent, GameEvent event, QuestHolder qHolder, Multimap<T, QuestProcVO> processing, TriConsumer<T, QuestProcVO, AQuestCfg> consumer) {
		Collection<Entry<T, QuestProcVO>> es = processing.entries();
		for (Entry<T, QuestProcVO> e : es) {
			T type = e.getKey();
			QuestProcVO qProcVO = e.getValue();
			consumer.accept(type, qProcVO, qProcVO.getCfg());
		}
	}

	public <T> Multimap<T, QuestProcVO>  getQuestProcVOsBySomeTypes(QuestHolder qHolder, Collection<T> types, GameEvent event, BiFunction<AQuestCfg,T,Boolean> cfgHaveType) {
		Multimap<T,QuestProcVO> processing = HashMultimap.create();
		/*
		 * 检索出完成条件对此事件关注的正在进行的任务
		 */
		//通过对 条件类型-任务集数据结构的优化,尽可能准确提供最小任务配置集合, 减小尝试检测的基数
		//不同的条件类型可能指向同一个任务对象, 多次指向就意味着监听多次
		Collection<QuestProcVO> procVOs = qHolder.getQuestProcMap().values();
		for (QuestProcVO questProcVO : procVOs) {
			AQuestCfg cfg = questProcVO.getCfg();
			for (T type : types) {
				boolean cfgHaveCdit = cfgHaveType.apply(cfg, type);
				if(cfgHaveCdit)
					processing.put(type, questProcVO);
			}
		}
		return processing;
	}
	
	


	/**
	 *通过映射找到  接收条件组中拥有上述条件的所有任务
	 *如果其没有处于完成或是进行中状态, 结合事件检测其所有接受条件, 在全部接受条件满足的情况下接受
	 * @param parent
	 * @param cmd
	 * @param holder
	 * @param event
	 * @param qHolder
	 * @param concernedCditTypes
	 */
	private void checkAndAccpet(Map parent, CMD cmd, QuestHolder holder, GameEvent event, QuestHolder qHolder, Collection<CditType> concernedCditTypes) {
		Set<AQuestCfg> mayDeal = GameUtil.createSet();
		/*通过对 条件类型-任务集数据结构的优化,尽可能准确提供最小任务配置集合, 减小尝试检测的基数*/
		for (CditType cditType : concernedCditTypes) {
			Collection<AQuestCfg> questCfgs = cditType.getAcceptableQuestCfgsByEvt(holder, event);
			/*合并不同条件指向的同一任务*/
			mayDeal.addAll(questCfgs);
		}
		for (AQuestCfg questCfg : mayDeal) {
			try{
				tryAcceptQuest(parent, cmd, qHolder, questCfg);
			}catch(LogicException e){}
			 catch (Exception e) {e.printStackTrace();}
		}
	}
	
	
	
	private void tryAcceptQuest(Map parent, CMD cmd, QuestHolder holder, AQuestCfg questCfg) throws LogicException{
		/*如果任务已经接受或已经完成,直接忽略*/
		if(holder.questOnPorcOrFinish(questCfg))
			return;
		Collection<CditCfg> acceptCdits = questCfg.getAcceptCdits().values();
		if(acceptCdits!=null&&acceptCdits.size()>0){//接受任务条件非空时作条件检测
			FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
			for (CditCfg cdit : acceptCdits) {//每个条件都为独立检查, 不支持合并检查
				CditProc cditProc = cdit.getProc();
				if(!cditProc.satisfiedAsAcceptCdit(cdit, holder))//不满足接受条件立即终止
					return;
				exec.addSyncFunc(cdit.getExFuncs());
			}
			Map prizeMap = GameUtil.createSimpleMap();
			if(holder.getPlayer()!=null)
				exec.exec(parent, prizeMap, holder.getPlayer());
			parent = msgFactory.getQuestPrizeMsg(parent, prizeMap);
		}
		QuestProcVO procvo = holder.addQuestProc(questCfg);
		questProcDAO.save(procvo);//保存任务数据
		parent = msgFactory.getQuestProcAddMsg(parent, procvo);
		
		//如果完成条件已经全部满足立即触发完成条件达成事件
		CditProc.checkAllFinCditsAndTriggerEvt(parent, cmd, holder, procvo/*添加任务数据对象*/);
	}





	@Override
	public EvtType[] getListenTypes() {
		return evtType;
	}

	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsLoad(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO, java.util.Map)
	 */
	@Override
	public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
		List<DBObj> procDatas = (List<DBObj>) data.get(DATA_SIGN_QUEST_PROC);
		if(procDatas != null && !procDatas.isEmpty()){
			for (DBObj dbObj : procDatas) {
				QuestProcVO vo = new QuestProcVO();
				vo.readFromDBObj(dbObj);
				player.getQuestProcMap().put(vo.getCid(), vo);
			}
		}
		List<DBObj> recDatas = (List<DBObj>) data.get(DATA_SIGN_QUEST_REC);
		if(recDatas != null && !recDatas.isEmpty()){
			for (DBObj dbObj : recDatas) {
				QuestRecVO vo = new QuestRecVO();
				vo.readFromDBObj(dbObj);
				player.getQuestRecMap().put(vo.getType(), vo);
			}
		}
	}

	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
		
	}

	@Override
	public void dailyReset(CMD cmd, Map parent, PlayerVO pvo, long ms) {
		update(cmd, parent, LoopType.d, pvo);
		pvo.setLastDailyQuestRefTime(ms);
		playerBO.save(pvo);
	}
	
	/**
	 * @see com.cellsgame.game.module.DailyResetable#dailyResetable(com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public boolean dailyResetable(PlayerVO pvo) {
		return true;
	}
	
	/**
	 * @see com.cellsgame.game.module.DailyResetable#lastDailyResetTime(com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public long lastDailyResetTime(PlayerVO pvo) {
		return pvo.getLastDailyQuestRefTime();
	}


	public PlayerBO getPlayerBO() {
		return playerBO;
	}


	public void setPlayerBO(PlayerBO playerBO) {
		this.playerBO = playerBO;
	}
}
