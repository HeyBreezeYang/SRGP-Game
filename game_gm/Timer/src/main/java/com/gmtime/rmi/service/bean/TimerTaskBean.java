package com.gmtime.rmi.service.bean;

import java.io.Serializable;
import java.util.Map;

import com.gmtime.time.QuartzManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TimerTaskBean implements Serializable{
	private static final long serialVersionUID = 6906057714713132459L;
	private String timeName;
	private String groupName;
	private long startTime;
	private long endTime;
	private String config;
	private String clazz;
	private short statues;
	private String dsp;

	public TimerTaskBean(Map<String,Object> map){
		this.timeName=map.get("timeName").toString();
		this.groupName=map.get("groupName").toString();
		this.config=map.get("config").toString();
		this.endTime=Long.parseLong(map.get("endTime").toString());
		this.startTime=Long.parseLong(map.get("startTime").toString());
		this.clazz=map.get("clazz").toString();
		this.statues=Short.parseShort(map.get("statues").toString());
		this.dsp=map.get("dsp").toString();
	}
	public TimerTaskBean(){}
	public String getClazz() {
		return "com.gmtime.time.job.impl.".concat(clazz);
	}
	public String getClas() {
		return clazz;
	}
	private String getTimerStatues(){
		return QuartzManager.state(timeName,groupName);
	}
	public String thisTimer(){
		return " {timeName=" + timeName +",clazz=" + clazz +",config=" + config
				+", statues=" + statues+",TimerStatues=" + getTimerStatues() +" } ";
	}
}
