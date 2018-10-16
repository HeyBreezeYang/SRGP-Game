package com.cellsgame.game.module.quest.vo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.game.module.quest.cache.AQuestCfgCache;
import com.cellsgame.game.module.quest.csv.AQuestCfg;
import com.cellsgame.game.module.quest.csv.BehavCfg;
import com.cellsgame.game.module.quest.csv.CditCfg;
import com.cellsgame.game.module.quest.csv.IQuestItemCfg;
import com.cellsgame.orm.DBObj;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * 任务进度记录对象
 * @author peterveron
 *
 */
public class QuestProcVO extends DBVO{
	/**
	 * 进度记录项目
	 * @author peterveron
	 */
	public abstract static class ProcItemVO{
		
		private transient IQuestItemCfg cfg;

		public IQuestItemCfg getCfg() {
			return cfg;
		}

		public void setCfg(IQuestItemCfg cfg) {
			this.cfg = cfg;
		}

		/**
		 * @return
		 */
		public abstract Map toInfoMap(); 
	}
	
	
	private Integer id;
	
	private Integer cid;
	
	private AQuestCfg cfg;
	
	private Integer holderId;
	
	@Save(ix = 1)
	private Map<Integer,String> finCditProcsData;
	
	@Save(ix = 2)
	private Map<Integer,String> autoBehavProcsData;
	
	@Save(ix = 3)
	private Map<Integer,String> behavProcsData;
	
	private Map<Integer,ProcItemVO> finCditProcs;
	
	private Map<Integer,ProcItemVO> autoBehavProcs;
	
	private Map<Integer,ProcItemVO> manualBehavProcs;
	
	public AQuestCfg getCfg(){
		if(cfg == null)
			cfg = AQuestCfgCache.getQuestCfg(getCid());
		return cfg;
	}	
	
	public QuestProcVO(){}
	
	public QuestProcVO(AQuestCfg cfg, QuestHolder qHolder){
		this.cfg = cfg;
		cid = cfg.getId();
		setHolderId(qHolder.holderID());
		createProcItem(cfg.getFinCditLst(), finCditProcs, qHolder);
		createProcItem(cfg.getAutoBehavLst(), autoBehavProcs, qHolder);
		createProcItem(cfg.getManualBehavLst(), manualBehavProcs, qHolder);
	}

	public void createProcItem(List<? extends IQuestItemCfg> cfgItemList, Map<Integer, ProcItemVO> container, QuestHolder qHolder) {
		int size = cfgItemList.size();
		for (int i = 0; i < size; i++) {
			IQuestItemCfg cfgItem = cfgItemList.get(i);
			ProcItemVO procItem = cfgItem.getProc().createProcItem(cfgItem, qHolder);
			procItem.setCfg(cfgItem);
			container.put(i, procItem);
		}
	}
	
	@Override
	public void readFromDBObj(DBObj dbObj) {
		super.readFromDBObj(dbObj);
		AQuestCfg cfg = getCfg();
		
		List<CditCfg> finCditLst = cfg.getFinCditLst();
		loadData(finCditProcsData, finCditProcs, ix->finCditLst.get(ix));
		
		List<BehavCfg> autoBehavLst = cfg.getAutoBehavLst();
		loadData(autoBehavProcsData, autoBehavProcs, ix->autoBehavLst.get(ix));
		
		List<BehavCfg> manualBehavLst = cfg.getManualBehavLst();
		loadData(behavProcsData, manualBehavProcs, ix->manualBehavLst.get(ix));
		
	}
	
	private void loadData(Map<Integer,String> srcDataMap,  Map<Integer, ProcItemVO> tgtVOMap,Function<Integer,IQuestItemCfg> cfgGetter){
		Set<Entry<Integer, String>> srcEs = srcDataMap.entrySet();
		for (Entry<Integer, String> e : srcEs) {
			Integer ix = e.getKey();
			String jsonStr = e.getValue();
			IQuestItemCfg cfg = cfgGetter.apply(ix);
			ProcItemVO procVO = (ProcItemVO) JSONUtils.fromJson(jsonStr, cfg.getProc().getProcItemClass());//目前暂时以JSON的格式转储数据
			procVO.setCfg(cfg);
			tgtVOMap.put(ix, procVO);	
		}
	}
	
	@Override
	public DBObj writeToDBObj() {
		writeData(finCditProcs, finCditProcsData);
		writeData(autoBehavProcs, autoBehavProcsData);
		writeData(manualBehavProcs, behavProcsData);
		return super.writeToDBObj();
	}

	private void writeData(Map<Integer, ProcItemVO> srcVOMap, Map<Integer, String> tgtDataMap) {
		tgtDataMap.clear();
		Set<Entry<Integer, ProcItemVO>> es = srcVOMap.entrySet();
		for (Entry<Integer, ProcItemVO> e : es) {
			Integer ix = e.getKey();
			ProcItemVO procItem = e.getValue();
			tgtDataMap.put(ix, JSONUtils.toJSONString(procItem));//目前暂时以JSON的格式转储数据
		}
	}
	
	@Override
	protected Object getPrimaryKey() {
		return id;
	}

	@Override
	protected void setPrimaryKey(Object pk) {
		this.id = (Integer) pk;
	}

	@Override
	protected Object[] getRelationKeys() {
		return new Object[]{holderId};
	}

	@Override
	protected void setRelationKeys(Object[] relationKeys) {
		this.holderId = (Integer) relationKeys[0];
	}

	@Override
	protected void init() {
		finCditProcsData = GameUtil.createSimpleMap();
		autoBehavProcsData = GameUtil.createSimpleMap();
		behavProcsData = GameUtil.createSimpleMap();
		finCditProcs = GameUtil.createSimpleMap();
		autoBehavProcs = GameUtil.createSimpleMap();
		setManualBehavProcs(GameUtil.createSimpleMap());
	}

	@Override
	public Integer getCid() {
		// TODO Auto-generated method stub
		return cid;
	}

	@Override
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	
	private boolean satisfied(Collection<ProcItemVO> procItems) {
		boolean fin = true;
		for (ProcItemVO item : procItems) {
			IQuestItemCfg cfg = item.getCfg();
			fin &= cfg.getProc().satisfied(cfg, item);
			if(!fin)
				break;
		}
		return fin;
	}

	public Integer getHolderId() {
		return holderId;
	}

	public void setHolderId(Integer holderId) {
		this.holderId = holderId;
	}

	public Map<Integer,ProcItemVO> getFinCditProcs() {
		return finCditProcs;
	}

	public void setFinCditProcs(Map<Integer,ProcItemVO> finCditProcs) {
		this.finCditProcs = finCditProcs;
	}
	
	public void addFinCditProcVO(int ix, ProcItemVO cditProcVO){
		finCditProcs.put(ix, cditProcVO);
	}
	

	public Map<Integer,ProcItemVO> getAutoBehavProcs() {
		return autoBehavProcs;
	}

	public void setAutoBehavProcs(Map<Integer,ProcItemVO> autoBehavProcs) {
		this.autoBehavProcs = autoBehavProcs;
	}


	public void addAutoBehavProcVO(int ix, ProcItemVO procItemVO) {
		autoBehavProcs.put(ix, procItemVO);
	}
	
	public Map<Integer,ProcItemVO> getManualBehavProcs() {
		return manualBehavProcs;
	}

	public void setManualBehavProcs(Map<Integer,ProcItemVO> manualBehavProcs) {
		this.manualBehavProcs = manualBehavProcs;
	}

	public void addManualBehavProcVO(int ix, ProcItemVO procItemVO) {
		manualBehavProcs.put(ix, procItemVO);
	}
	
	public Map<Integer,String> getAutoBehavProcsData() {
		return autoBehavProcsData;
	}

	public void setAutoBehavProcsData(Map<Integer,String> autoBehavProcsData) {
		this.autoBehavProcsData = autoBehavProcsData;
	}

	public Map<Integer,String> getBehavProcsData() {
		return behavProcsData;
	}

	public void setBehavProcsData(Map<Integer,String> behavProcsData) {
		this.behavProcsData = behavProcsData;
	}

	public Map<Integer,String> getFinCditProcsData() {
		return finCditProcsData;
	}

	public void setFinCditProcsData(Map<Integer,String> finCditProcsData) {
		this.finCditProcsData = finCditProcsData;
	}

	public boolean isAutoBehavFin() {
		return satisfied(autoBehavProcs.values());
	}


	public boolean isManualBehavFin() {
		return satisfied(manualBehavProcs.values());
	}
	
	public boolean finCditsSatisfied(){
		return satisfied(finCditProcs.values());
	}

	

	public boolean allBehavFin(){
		return isAutoBehavFin()&isManualBehavFin();
	}



	

}
