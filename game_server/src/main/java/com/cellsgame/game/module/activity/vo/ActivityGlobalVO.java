package com.cellsgame.game.module.activity.vo;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class ActivityGlobalVO extends DBVO {

	
	private int id;
	
	private String activityId;
	
	@Save(ix = 1)
	private int playerId;
	
	@Save(ix = 2)
	private Map<Integer, Long> currencyData;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public Map<Integer, Long> getCurrencyData() {
		return currencyData;
	}

	public void setCurrencyData(Map<Integer, Long> currencyData) {
		this.currencyData = currencyData;
	}


	@Override
	protected Object getPrimaryKey() {
		return id;
	}

	@Override
	protected void setPrimaryKey(Object pk) {
		id = Integer.parseInt(pk.toString());
	}

	@Override
    protected Object[] getRelationKeys() {
        return new Object[]{activityId};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys != null && relationKeys.length > 0)
        	activityId = (String) relationKeys[0];
    }

	@Override
	protected void init() {
		currencyData = GameUtil.createMap();
	}

	@Override
	public Integer getCid() {
		return null;
	}

	@Override
	public void setCid(Integer cid) {
		
	}
	
}
