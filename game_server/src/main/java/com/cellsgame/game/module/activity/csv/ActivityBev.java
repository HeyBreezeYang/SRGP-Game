package com.cellsgame.game.module.activity.csv;

import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.module.activity.cons.ActivityBevType;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class ActivityBev extends DBVO {
	

	@Save(ix = 1)
	private String type;
	
	@Save(ix = 2)
    private Map<Integer, Integer> execLimit;
	
	@Save(ix = 3)
	private List<FuncConfig> funcs;
	
	@Save(ix = 4)
	private Map<String, String> param;

	@Save(ix = 5)
	private List<FuncConfig> cost;
	
	//-------------逻辑数据-------------
	private ActivityBevType bevType;
	private List<ActivityScopeType> scopeTypes;
    
	public ActivityBevType getBevType() {
		if(bevType == null)
			bevType = Enums.get(ActivityBevType.class, type);
		return bevType;
	}
	
	public List<ActivityScopeType> getScopeTypes(){
		if(scopeTypes == null){
			scopeTypes = GameUtil.createList();
			for(int scope : execLimit.keySet()){
				scopeTypes.add(Enums.get(ActivityScopeType.class, scope));
			}
		}
		return scopeTypes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<Integer, Integer> getExecLimit() {
		return execLimit;
	}

	public void setExecLimit(Map<Integer, Integer> execLimit) {
		this.execLimit = execLimit;
	}

	public List<FuncConfig> getFuncs() {
		return funcs;
	}

	public void setFuncs(List<FuncConfig> funcs) {
		this.funcs = funcs;
	}
	
	public Map<String, String> getParam() {
		return param;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	public List<FuncConfig> getCost() {
		return cost;
	}

	public void setCost(List<FuncConfig> cost) {
		this.cost = cost;
	}

	@Override
	protected Object initPrimaryKey() {
		return null;
	}

	@Override
	protected Object getPrimaryKey() {
		return null;
	}

	@Override
	protected void setPrimaryKey(Object pk) {
		
	}

	@Override
	protected Object[] getRelationKeys() {
		return null;
	}

	@Override
	protected void setRelationKeys(Object[] relationKeys) {
		
	}

	@Override
	protected void init() {
		type = "";
		cost = GameUtil.createList();
		funcs = GameUtil.createList();
		param = GameUtil.createSimpleMap();
		execLimit = GameUtil.createSimpleMap();
	}

	@Override
	public Integer getCid() {
		return null;
	}

	@Override
	public void setCid(Integer cid) {
		
	}

	public int getIntParam(String key) {
		if(param == null) return 0;
		String val = param.get(key);
		return  val == null ? 0 : Integer.valueOf(val);
	}
    
    public String getStringParam(String key, String def) {
        return param == null ? def : param.get(key);
    }
	
}
