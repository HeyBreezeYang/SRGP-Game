/**
 * 
 */
package com.cellsgame.game.module.quest.msg;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.cellsgame.game.module.quest.vo.QuestProcVO;
import com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO;
import com.cellsgame.game.module.quest.vo.QuestRecItemVO;
import com.cellsgame.game.module.quest.vo.QuestRecVO;

/**
 * @author peterveron
 *
 */
public class MsgFactoryQuest extends MsgFactory{
	


	private static final String QUEST = "qst";
	private static final String FIN_CDIT = "fCdit";
	private static final String AUTO_BEHAV = "aBehav";
	private static final String MANUAL_BEHAV = "mBehav";
	
	public static final String RECORD_INT = "irec";
	public static final String RECORD_BOOLEAN = "brec";
	public static final String RECORD_LONG = "lrec";
	
	private static final MsgFactoryQuest instance = new MsgFactoryQuest();

	/**
	 * @see com.cellsgame.game.module.MsgFactory#getModulePrefix()
	 */
	@Override
	public String getModulePrefix() {
		return QUEST;
	}
	
	
	public static MsgFactoryQuest instance() {
		return instance ;
	}

	
	public Map getQuestInfoMsg(Map parent, QuestHolder holder){
		Map info = gocInfoMap(parent);
		
		Map<Integer,Map> dataMap = gocMapIn(info, PROGRESS);
		Set<Entry<Integer, QuestProcVO>> procEs = holder.getQuestProcMap().entrySet();
		for (Entry<Integer, QuestProcVO> e : procEs) {
			dataMap.put(e.getKey(), getQuestProcInfo(e.getValue()));
		}
		
		dataMap = gocMapIn(info, RECORD);
		Set<Entry<Integer, QuestRecVO>> recEs = holder.getQuestRecMap().entrySet();
		for (Entry<Integer, QuestRecVO> e : recEs) {
			Integer type = e.getKey();
			Map<Integer,Map> itemInfos = gocMapIn(dataMap, type);
			QuestRecVO recvo = e.getValue();
			Set<Entry<Integer, QuestRecItemVO>> itemEs = recvo.getQuestRecs().entrySet();
			for (Entry<Integer, QuestRecItemVO> ie : itemEs) {
				itemInfos.put(ie.getKey(), getQuestRecItemInfo(ie.getValue()));	
			}
		}
		
		return parent;
	}
	
	
	private Map getQuestProcOpMsg(Map parent, String opSign, QuestProcVO qproc){
		Map opMsg = gocOpMap(parent);
		Map procOpMsg = gocMapIn(opMsg, PROGRESS);
		Map<Integer,Map> actOpMsg = gocMapIn(procOpMsg, opSign);
		actOpMsg.put(qproc.getCid(), getQuestProcInfo(qproc));
		return parent;
	}
	
	/**
	 * @param qproc
	 * @return
	 */
	private Map getQuestProcInfo(QuestProcVO qproc) {
		Map ret = GameUtil.createSimpleMap();
		ret.put(FIN_CDIT, getProcItemInfos(qproc.getFinCditProcs()));
		ret.put(AUTO_BEHAV, getProcItemInfos(qproc.getAutoBehavProcs()));
		ret.put(MANUAL_BEHAV, getProcItemInfos(qproc.getManualBehavProcs()));
		ret.put(CID, qproc.getCid());
		return ret;
	}


	private Map<Integer, Map> getProcItemInfos(Map<Integer,ProcItemVO> map) {
		Set<Entry<Integer, ProcItemVO>> es = map.entrySet();
		Map<Integer,Map> infos = GameUtil.createSimpleMap();
		for (Entry<Integer, ProcItemVO> e : es) {
			infos.put(e.getKey(), e.getValue().toInfoMap());
		}
		return infos;
	}


	private Map getQuestRecOpMsg(Map parent, String opSign, int recType, QuestRecItemVO qrec){
		Map opMsg = gocOpMap(parent);
		Map recOpMsg = gocMapIn(opMsg, RECORD);
		Map actOpMsg = gocMapIn(recOpMsg, opSign);
		Map typedOpMsg = gocMapIn(actOpMsg, recType);
		typedOpMsg.put(qrec.getCid(), getQuestRecItemInfo(qrec));
		return parent;
	}
	
	/**
	 * @param qrec
	 * @return
	 */
	private Map getQuestRecItemInfo(QuestRecItemVO qrec) {
		Map ret = GameUtil.createSimpleMap();
		ret.put(CID, qrec.getCid());
		return ret;
	}


	public Map getQuestProcAddMsg(Map parent, Collection<QuestProcVO> qprocs){
		for (QuestProcVO qproc : qprocs) {
			parent = getQuestProcAddMsg(parent, qproc);
		}
		return parent;
	}
	
	public Map getQuestProcAddMsg(Map parent, QuestProcVO qproc){
		return getQuestProcOpMsg(parent, ADD, qproc);
	}
	
	
	public Map getQuestProcUpdateMsg(Map parent, QuestProcVO qproc){
		return getQuestProcOpMsg(parent, UPDATE, qproc);
	}
	
	public Map getQuestPrizeMsg(Map parent, Map prizeMap){
		Map opMap = gocOpMap(parent);
		addPrizeMsg(opMap, prizeMap);
		return parent;
	}
	
	public Map getQuestProcUpdateMsg(Map parent, Collection<QuestProcVO> qprocs){
		for (QuestProcVO qproc : qprocs) {
			parent = getQuestProcUpdateMsg(parent, qproc);
		}
		return parent;
	}
	
	
	public Map getQuestProcRmMsg(Map parent, Integer rmProcCid){
		Map opMsg = gocOpMap(parent);
		Map procOpMsg = gocMapIn(opMsg, PROGRESS);
		List<Integer> deleteMsg = gocLstIn(procOpMsg, DELETE);
		deleteMsg.add(rmProcCid);
		return parent;
	}
	
	
	public Map getQuestProcRmMsg(Map parent, Collection<Integer> rmProcCids){
		Map opMsg = gocOpMap(parent);
		Map procOpMsg = gocMapIn(opMsg, PROGRESS);
		List<Integer> deleteMsg = gocLstIn(procOpMsg, DELETE);
		deleteMsg.addAll(rmProcCids);
		return parent;
	}

	
	public Map getQuestRecAddMsg(Map parent, int recType, QuestRecItemVO qrec){
		return getQuestRecOpMsg(parent, ADD, recType, qrec);
	}
	
	
	public Map getQuestRecRmMsg(Map parent,  int recType, Collection<Integer> rmRecCids) {
		Map opMsg = gocOpMap(parent);
		Map procOpMsg = gocMapIn(opMsg, RECORD);
		Map deleteMsg = gocMapIn(procOpMsg, DELETE);
		List<Integer> deleteCids = gocLstIn(deleteMsg, recType);
		deleteCids.addAll(rmRecCids);
		return parent;
	}
	
	public Map getQuestRecRmMsg(Map parent,  int recType, Integer rmRecCid) {
		Map opMsg = gocOpMap(parent);
		Map procOpMsg = gocMapIn(opMsg, RECORD);
		Map deleteMsg = gocMapIn(procOpMsg, DELETE);
		List<Integer> deleteCids = gocLstIn(deleteMsg, recType);
		deleteCids.add(rmRecCid);
		return parent;
	}
	

}
