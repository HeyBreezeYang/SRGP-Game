package com.cellsgame.game.module.activity.vo;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ActivityCondRecVO extends DBVO {

	private int id;
	
	private String relId;

	@Save(ix = 1)
	private String activityId;

	@Save(ix = 2)
	private int group;

	@Save(ix = 3)
	private int index;

	@Save(ix = 4)
	private long finishData;
	
	@Save(ix = 5)
	private long refTime;

	@Save(ix = 6)
	private boolean finish;

	@Save(ix = 7)
	private long lastRecordTime;

	@Save(ix = 8)
	private Set<Integer> recordPlayers;

	@Save(ix = 9)
	private Map<Integer, Long> recordDetails;
	
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

	public long getFinishData() {
		return finishData;
	}

	public void setFinishData(long finishData) {
		this.finishData = finishData;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public long getRefTime() {
		return refTime;
	}

	public void setRefTime(long refTime) {
		this.refTime = refTime;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public long getLastRecordTime() {
		return lastRecordTime;
	}

	public void setLastRecordTime(long lastRecordTime) {
		this.lastRecordTime = lastRecordTime;
	}

	public Set<Integer> getRecordPlayers() {
		return recordPlayers;
	}

	public void setRecordPlayers(Set<Integer> recordPlayers) {
		this.recordPlayers = recordPlayers;
	}

	public Map<Integer, Long> getRecordDetails() {
		return recordDetails;
	}

	public void setRecordDetails(Map<Integer, Long> recordDetails) {
		this.recordDetails = recordDetails;
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
		recordPlayers = new HashSet<>();
		recordDetails = GameUtil.createSimpleMap();
	}

	@Override
	public Integer getCid() {
		return null;
	}

	@Override
	public void setCid(Integer cid) {
		
	}

}
