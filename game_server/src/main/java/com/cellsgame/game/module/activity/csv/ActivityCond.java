package com.cellsgame.game.module.activity.csv;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.module.activity.cons.ActivityCondType;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class ActivityCond extends DBVO {
	
	//模块
	@Save(ix = 1)
	private String type;
	//作用域
	@Save(ix = 4)
	private int scope;
    // 参数ID, 如怪物ID, 道具ID
	@Save(ix = 5)
    private Map<String, String> param;
    // 数量
	@Save(ix = 6)
    private long value;
    
    //-------------------逻辑数据----------------
    private ActivityCondType condType;
    private ActivityScopeType scopeType;
    
	public ActivityCondType getCondType() {
		if(condType == null)
			condType = Enums.get(ActivityCondType.class, type);
		return condType;
	}
	
	public ActivityScopeType getScopeType(){
		if(scopeType == null)
			scopeType = Enums.get(ActivityScopeType.class, scope);
		return scopeType;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, String> getParam() {
		return param;
	}
	public void setParam(Map<String, String> param) {
		this.param = param;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public int getScope() {
		return scope;
	}
	public void setScope(int scope) {
		this.scope = scope;
	}
	
    public int getIntParam(String key) {
		if(param == null) return 0;
		String val = param.get(key);
        return  val == null ? 0 : Integer.valueOf(val);
    }

	public boolean getBooleanParam(String key) {
		if(param == null) return false;
		String val = param.get(key);
		return val != null && Boolean.parseBoolean(val);
	}

	public String getStringParam(String key) {
		if(param == null) return null;
		return  param.get(key);
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
		param = GameUtil.createSimpleMap();
	}
	@Override
	public Integer getCid() {
		return null;
	}
	@Override
	public void setCid(Integer cid) {
		
	}
    
    
	
}
