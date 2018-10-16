package com.cellsgame.game.module.quest.bo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtNoticer;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.quest.cons.BehavType;
import com.cellsgame.game.module.quest.csv.BehavCfg;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.cellsgame.game.module.quest.vo.QuestProcVO;
import com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO;
import com.cellsgame.orm.BaseDAO;

@SuppressWarnings("rawtypes")
public abstract class BehavProc<T extends ProcItemVO> extends EvtNoticer implements QuestItemProc<T,BehavCfg>{
	
	/**
	 * 处理自动行为
	 * @param parent
	 * @param cmd
	 * @param qHolder
	 * @param qProcVO
	 * @return
	 */
	public static boolean tryDoAutoBehav( CMD cmd, Map parent, Map prizeMap, QuestHolder qHolder, QuestProcVO qProcVO) {
		if(!qProcVO.isAutoBehavFin()){
			return execBehavs(cmd, parent, prizeMap, qHolder,  qProcVO.getCfg().getAutoBehavLst(), qProcVO.getAutoBehavProcs());
		}
		return false;
	}
	
	/**
	 * 处理手动行为
	 * @param parent
	 * @param cmd
	 * @param qHolder
	 * @param qProcVO
	 * @return
	 */
	public static boolean tryDoManualBehav(CMD cmd, Map parent, Map prizeMap,  QuestHolder qHolder, QuestProcVO qProcVO) {
		if(!qProcVO.isManualBehavFin()){
			return execBehavs(cmd, parent, prizeMap, qHolder,  qProcVO.getCfg().getManualBehavLst(), qProcVO.getManualBehavProcs());
		}
		return false;
	}


	public static boolean execBehavs(CMD cmd, Map parent, Map prizeMap, QuestHolder qHolder, List<BehavCfg> behavCfgs, Map<Integer, ProcItemVO> behavProcs) {
		/*
		 * 此处首先将功能集合创建一个副本, 在这个副本中加入新功能项, 
		 * 然后由该副本执行一次可运行检测, 如果检测通过则以这个副本作
		 * 为下一次迭代的功能集合, 如果检测不通过则继续使用原功能集合
		 * 进行迭代, 这样的优势在于可执行功能集中可执行的功能(基于性
		 * 能考虑并未作最优组合处理),留下无法执行的功能待以后执行,劣
		 * 势在于多次可执行检测,增加性能开销
		 */
		FuncsExecutor<?> allExec = FuncsExecutorsType.Base.getExecutor(cmd);
		int size = behavCfgs.size();
		boolean change = false;
		Set<Integer> execBehavIxs = GameUtil.createSet();
		for (int i = 0; i < size; i++) {//
			BehavCfg behavCfg = behavCfgs.get(i);
			ProcItemVO procItemVO = behavProcs.get(i);
			if(behavCfg.getProc().satisfied(behavCfg, procItemVO))//如果完成则不再执行
				continue;
			
			FuncsExecutor<?> copy = allExec.copy();
			copy.addExecutor(behavCfg.getProc().extractBehavFuncs(cmd,qHolder,behavCfg,procItemVO));
			try{
				copy.selectAndCheck(qHolder.getPlayer());//尝试检查加入的功能是否能够执行
				execBehavIxs.add(i);
				allExec = copy;//
			}catch(LogicException e){
				e.printStackTrace();
			}
			
		}
		try{
			
			allExec.exec(parent, prizeMap, qHolder.getPlayer());
			for (Integer ix : execBehavIxs) {//
				BehavCfg behavCfg = behavCfgs.get(ix);
				ProcItemVO procItemVO = behavProcs.get(ix);
				change|=behavCfg.getProc().execBehavRec(cmd,qHolder,behavCfg,procItemVO);
			}
		}catch(LogicException e){
			e.printStackTrace();
		}
		return change;
	}

	private BehavType behavType;
	
	
	private BaseDAO<QuestProcVO> questProcDAO;
	
	public BehavProc(){
//		questProcDAO = SpringBeanFactory.getBean("questProcDAO");
	}
	

	public BehavType getBehavType() {
		return behavType;
	}

	public void setBehavType(BehavType behavType) {
		this.behavType = behavType;
	}

	public void listenEvtAsAutoBehav(Map parent, BehavCfg behavCfg, QuestHolder qHolder, QuestProcVO qProcVO, GameEvent event) {
		listenAsSomeTypeBehav(parent, behavCfg, qHolder, qProcVO, event,
				ix->qProcVO.getAutoBehavProcs().get(ix),
				(ix,procItem)->qProcVO.addAutoBehavProcVO(ix, procItem)
				);
	}


	
	

	public void listenEvtAsManualBehav(Map parent, BehavCfg behavCfg, QuestHolder qHolder, QuestProcVO qProcVO, GameEvent event) {
		listenAsSomeTypeBehav(parent, behavCfg, qHolder, qProcVO, event,
				ix->qProcVO.getManualBehavProcs().get(ix),
				(ix,procItem)->qProcVO.addManualBehavProcVO(ix, procItem)
				);
	}
	
	
	
	private void listenAsSomeTypeBehav(Map parent, BehavCfg behavCfg, QuestHolder qHolder, QuestProcVO qProcVO, GameEvent event, 
			Function<Integer,ProcItemVO> getProcItem,
			BiConsumer<Integer,ProcItemVO> addProcVO
			) {
		ProcItemVO procItem = getProcItem.apply(behavCfg.getIx());
		if(procItem == null){
			procItem = createProcItem(behavCfg, qHolder);
			addProcVO.accept(behavCfg.getIx(), procItem);
			msgFactory.getQuestProcUpdateMsg(parent, qProcVO);
			questProcDAO.save(qProcVO);
		}
		if(listenEvt(parent, behavCfg, (T)procItem, qProcVO, qHolder, event)){
			msgFactory.getQuestProcUpdateMsg(parent, qProcVO);
			questProcDAO.save(qProcVO);
		}
	}
	
	
	public FuncsExecutor extractBehavFuncs(CMD cmd, QuestHolder qHolder, BehavCfg behavCfg, ProcItemVO procItemVO) {
		List<FuncConfig> funcs = behavCfg.getFuncs();
		FuncsExecutor exec = FuncsExecutorsType.All.getExecutor(cmd);
		exec.addSyncFunc(funcs);
		return exec;
	}


	/**
	 * 将行为的执行情况写入进度对象之中
	 * @param cmd
	 * @param qHolder
	 * @param behavCfg
	 * @param procItemVO
	 * @return
	 */
	public  abstract boolean execBehavRec(CMD cmd, QuestHolder qHolder, BehavCfg behavCfg, T procItemVO);

	
	
}
