package com.gmtime.gm.service.task.bean;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.gmdesign.bean.other.GmHashMap;

/**
 * 
* @ClassName TaskString
* @Description 任务缓存对象 储存至数据库形式
* @author DJL
* @date 2015-12-24 下午6:26:30
*
 */
public class TaskString {
	private String name;
	private String taskJson;
	public TaskString(){}
	public TaskString(Map<String,Object> map){
		this.name=map.get("taskName").toString();
		this.taskJson=map.get("obj").toString();
	}
	public Object[] getObjPrams(){
		return new Object[]{this.name,this.taskJson};
	}
	
	public String getName() {
		return name;
	}
	public List<GmHashMap> getTaskObj(){
		return JSON.parseArray(this.taskJson,GmHashMap.class);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaskJson() {
		return taskJson;
	}
	public void setTaskJson(String taskJson) {
		this.taskJson = taskJson;
	}
	
	public void setTaskJson(List<GmHashMap> taskJson) {
		this.taskJson = JSON.toJSONString(taskJson);
	}

}
