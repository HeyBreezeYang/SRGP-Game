package com.cellsgame.game.module.activity.csv;

import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;

public class ActivityConfig extends BaseCfg {

	private int id;
    //活动开始日期
  	private String startDate;
  	//活动结束日期
  	private String endDate;
  	//刷新间隔{单位：分钟}
  	private int refInterval;
	//** 工作模式 1:逐一检查-逐一执行模式 ， 2 : 活动结束时执行*/
	private int workMode;
	//自动行为
	private boolean autoBev;
  	//客户端参数
  	private String clientAtts;
  	//条件
  	private Map<Integer, Map<Integer, ActivityCond>> conds;
  	//行为
  	private Map<Integer, Map<Integer, ActivityBev>> bevs;
  	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getRefInterval() {
		return refInterval;
	}
	public void setRefInterval(int refInterval) {
		this.refInterval = refInterval;
	}
	public int getWorkMode() {
		return workMode;
	}
	public void setWorkMode(int workMode) {
		this.workMode = workMode;
	}
	public boolean isAutoBev() {
		return autoBev;
	}
	public void setAutoBev(boolean autoBev) {
		this.autoBev = autoBev;
	}
	public String getClientAtts() {
		return clientAtts;
	}
	public void setClientAtts(String clientAtts) {
		this.clientAtts = clientAtts;
	}
	public Map<Integer, Map<Integer, ActivityCond>> getConds() {
		return conds;
	}
	public void setConds(Map<Integer, Map<Integer, ActivityCond>> conds) {
		this.conds = conds;
	}
	public Map<Integer, Map<Integer, ActivityBev>> getBevs() {
		return bevs;
	}
	public void setBevs(Map<Integer, Map<Integer, ActivityBev>> bevs) {
		this.bevs = bevs;
	}
  	
}
