package com.cellsgame.game.module.quest.csv;

import java.util.List;
import java.util.Map;

import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.quest.bo.BehavProc;
import com.cellsgame.game.module.quest.cons.BehavType;

public class BehavCfg extends Indexed implements IQuestItemCfg{

	private int type;
	
	private BehavType behavType;
	
	private List<FuncConfig> funcs;
	
	private Map exParam;
	
	private Map exFuncs;

	public BehavType getType() {
		if(behavType == null)
			behavType = Enums.get(BehavType.class, type);
		return behavType;
	}

	public List<FuncConfig> getFuncs() {
		return funcs;
	}

	public void setFuncs(List<FuncConfig> funcs) {
		this.funcs = funcs;
	}

	public Map getExParam() {
		return exParam;
	}

	public void setExParam(Map exParam) {
		this.exParam = exParam;
	}

	public Map getExFuncs() {
		return exFuncs;
	}

	public void setExFuncs(Map exFuncs) {
		this.exFuncs = exFuncs;
	}

	public BehavProc getProc() {
		return getType().getProc();
	}
	
	
}
