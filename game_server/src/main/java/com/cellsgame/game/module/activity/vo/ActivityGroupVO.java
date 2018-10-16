package com.cellsgame.game.module.activity.vo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class ActivityGroupVO extends DBVO {
	
	@Save(ix = 1)
	private int group;
	
	@Save(ix = 2)
	private Map<Integer, ActivityCond> conds;
	
	@Save(ix = 3)
	private Map<Integer, ActivityBev> bevs;

	//-----------------逻辑数据------------------
	private ActivityGroupDataVO activityGroupDataVO;

	public ActivityGroupDataVO getActivityGroupDataVO() {
		return activityGroupDataVO;
	}

	public void setActivityGroupDataVO(ActivityGroupDataVO activityGroupDataVO) {
		this.activityGroupDataVO = activityGroupDataVO;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public Map<Integer, ActivityCond> getConds() {
		return conds;
	}

	public void setConds(Map<Integer, ActivityCond> conds) {
		this.conds = conds;
	}

	public Map<Integer, ActivityBev> getBevs() {
		return bevs;
	}

	public void setBevs(Map<Integer, ActivityBev> bevs) {
		this.bevs = bevs;
	}

	@Override
	protected Object initPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setPrimaryKey(Object pk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object[] getRelationKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setRelationKeys(Object[] relationKeys) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void init() {
		conds = GameUtil.createSimpleMap();
		bevs = GameUtil.createSimpleMap();
	}

	@Override
	public Integer getCid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCid(Integer cid) {
		// TODO Auto-generated method stub
		
	}

	public Set<EvtType> getConcernEvents() {
		Set<EvtType> list = new HashSet<EvtType>();
		for(ActivityCond cond : getConds().values()){
			ACondProcess process = cond.getCondType().getProcess();
			if(process.getConcernType() == null)continue;
			for(EvtType evt : process.getConcernType())
				list.add(evt);
		}
		for(ActivityBev bev : getBevs().values()){
			ABevProcess process = bev.getBevType().getProcess();
			if(process.getConcernType() == null)continue;
			for(EvtType evt : process.getConcernType())
				list.add(evt);
		}
		return list;
	}

}
