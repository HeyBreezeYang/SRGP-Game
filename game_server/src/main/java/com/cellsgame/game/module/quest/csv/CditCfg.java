package com.cellsgame.game.module.quest.csv;

import java.util.List;
import java.util.Map;

import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.quest.bo.CditProc;
import com.cellsgame.game.module.quest.cons.CditType;

public class CditCfg extends Indexed implements IQuestItemCfg{
	public static final int REC_MODE_EARLY_REC = 0;
	public static final int REC_MODE_LATER_REC = 1;
	
	
	private CditType cditType;//条件类型
	
	private int type;
	
	private int param;
	
	private long val;
	
	private int recMode;
	
	private Map<?,?> exParams;//扩展参数集
	
	private List<FuncConfig> exFuncs;//扩展功能集
	
	public CditType getType() {
		if(cditType == null)
			cditType = Enums.get(CditType.class, type);
		return cditType;
	}


	public int getParam() {
		return param;
	}

	public void setParam(int param) {
		this.param = param;
	}

	public long getVal() {
		return val;
	}

	public void setVal(long val) {
		this.val = val;
	}


	public List<FuncConfig> getExFuncs() {
		return exFuncs;
	}

	public void setExFuncs(List<FuncConfig> exFuncs) {
		this.exFuncs = exFuncs;
	}

	public Map<?,?> getExParams() {
		return exParams;
	}

	public void setExParams(Map<?,?> exParams) {
		this.exParams = exParams;
	}

	public CditProc getProc(){
		return cditType.getProc();
	}







	public int getRecMode() {
		return recMode;
	}



	public void setRecMode(int recMode) {
		this.recMode = recMode;
	}

}
