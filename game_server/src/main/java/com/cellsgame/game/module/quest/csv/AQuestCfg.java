package com.cellsgame.game.module.quest.csv;

import java.util.Collection;
import java.util.List;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.quest.cons.BehavType;
import com.cellsgame.game.module.quest.cons.CditType;
import com.cellsgame.game.module.quest.cons.LoopType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public abstract class AQuestCfg extends BaseCfg{
	
	private int type;
	
	private Multimap<CditType,CditCfg> acceptCdits;//接受条件组
	
	private List<CditCfg> acceptCditLst;

	private Multimap<CditType,CditCfg> finCdits;//完成条件组
	
	private List<CditCfg> finCditLst;
	
	private Multimap<BehavType,BehavCfg> autoBehavs;//自动执行行为组
	
	private List<BehavCfg> autoBehavLst;
	
	private Multimap<BehavType,BehavCfg> manualBehavs;

	private List<BehavCfg> manualBehavLst;
	
	private LoopType loop = LoopType.n;
	
	
	public boolean isLoop(){
		return LoopType.n.equals(loop);
	}
	
	public LoopType getLoop() {
		return loop;
	}

	public void setLoop(String loop) {
		if(loop!=null)
			this.loop = LoopType.valueOf(loop);
		else
			this.loop = LoopType.n;
	}
	
	public Multimap<BehavType,BehavCfg> getAutoBehavs() {
		return autoBehavs;
	}

	public void setAutoBehavs(List<BehavCfg> autoBehavs) {
		this.autoBehavLst = autoBehavs;
		this.autoBehavs = ArrayListMultimap.create();
		int ix = 0;
		for (BehavCfg behav : autoBehavs) {
			behav.setIx(ix++);
			this.autoBehavs.put(behav.getType(), behav);
		}
	}

	public Multimap<BehavType,BehavCfg> getManualBehavs() {
		return manualBehavs;
	}

	public void setManualBehavs(List<BehavCfg> manualBehavs) {
		this.manualBehavLst = manualBehavs;
		this.manualBehavs = ArrayListMultimap.create();
		int ix = 0;
		for (BehavCfg behav : manualBehavs) {
			behav.setIx(ix++);
			this.manualBehavs.put(behav.getType(), behav);
		}
	}

	public Multimap<CditType,CditCfg> getFinCdits() {
		return finCdits;
	}

	public void setFinCdits(List<CditCfg> finCdits) {
		this.finCditLst = finCdits;
		this.finCdits =  ArrayListMultimap.create();
		if(finCdits.isEmpty())//加入空条件
			finCdits.add(new CditCfg());
		int ix = 0;
		for (CditCfg cdit : finCdits) {
			cdit.setIx(ix++);
			this.finCdits.put(cdit.getType(), cdit);
		}
	}

	public Multimap<CditType,CditCfg> getAcceptCdits() {
		return acceptCdits;
	}

	public void setAcceptCdits(List<CditCfg> acceptCdits) {
		this.acceptCditLst = acceptCdits;
		this.acceptCdits = ArrayListMultimap.create();
		if(acceptCditLst.isEmpty()){
			acceptCditLst.add(new CditCfg());
		}//加入空条件
		
		int ix = 0;
		for (CditCfg cdit : acceptCdits) {
			cdit.setIx(ix++);
			this.acceptCdits.put(cdit.getType(), cdit);
		}
	}
	
	
	public Collection<CditCfg> getAcceptCditBy(CditType type){
		return acceptCdits.get(type);
	}
	
	public Collection<CditCfg> getFinCditBy(CditType type){
		return finCdits.get(type);
	}
	
	public Collection<BehavCfg> getAutoBehavBy(BehavType type){
		return autoBehavs.get(type);
	}
	
	public Collection<BehavCfg> getManualBehavBy(BehavType type){
		return manualBehavs.get(type);
	}

	public List<CditCfg> getAcceptCditLst() {
		return acceptCditLst;
	}

	public List<CditCfg> getFinCditLst() {
		return finCditLst;
	}

	public List<BehavCfg> getAutoBehavLst() {
		return autoBehavLst;
	}

	public List<BehavCfg> getManualBehavLst() {
		return manualBehavLst;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}	
	
	
}
