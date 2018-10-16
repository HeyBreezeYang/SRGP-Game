package com.cellsgame.game.module.activity.vo;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class ActivityBevRecVO extends DBVO {

	private int id;
	
	private String relId;
	
	@Save(ix = 1)
	private String activityId;
	
	@Save(ix = 2)
	private int group;
	
	@Save(ix = 3)
	private int index;
	
	@Save(ix = 4)
	private int execNum;
	
	@Save(ix = 5)
	private long refTime;
	
	@Save(ix = 6)
	private Map<String, String> execParams;

	@Save(ix = 7)
	private boolean canExec;

	public boolean isCanExec() {
		return canExec;
	}

	public void setCanExec(boolean canExec) {
		this.canExec = canExec;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRelId() {
		return relId;
	}

	public void setRelId(String relId) {
		this.relId = relId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getExecNum() {
		return execNum;
	}

	public void setExecNum(int execNum) {
		this.execNum = execNum;
	}

	public long getRefTime() {
		return refTime;
	}

	public void setRefTime(long refTime) {
		this.refTime = refTime;
	}

	public Map<String, String> getExecParams() {
		return execParams;
	}

	public void setExecParams(Map<String, String> execParams) {
		this.execParams = execParams;
	}

    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = (int) pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{relId};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys != null && relationKeys.length > 0)
        	relId = (String) relationKeys[0];
    }


	@Override
	protected void init() {
		execParams = GameUtil.createSimpleMap();
	}

	@Override
	public Integer getCid() {
		return null;
	}

	@Override
	public void setCid(Integer cid) {
		
	}

}
