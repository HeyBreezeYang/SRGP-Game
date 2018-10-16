package com.gmtime.cache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.gmdesign.bean.other.GmHashMap;
import com.gmtime.gm.service.task.bean.TaskString;
import com.gmtime.rmi.service.bean.TimerTaskBean;


public class CacheTimer {
	/**
	 * key:任务名
	 * value:任务配置对象
	 */
	private static final Map<String,TimerTaskBean> timerNum=new ConcurrentHashMap<>();
	/**
	 * key:任务名
	 * value:任务执行使用对象
	 */
	private static final Map<String,List<GmHashMap>> useObject=new HashMap<>();
	public static void setTimer(String key,String config,long start,long end,short statues){
		if(config!=null)
			timerNum.get(key).setConfig(config);
		if(end!=0)
			timerNum.get(key).setEndTime(end);
		if(start!=0)
			timerNum.get(key).setStartTime(start);
		if(statues!=-1)
			timerNum.get(key).setStatues(statues);
	}
	public static String queryNowTimer(){
		StringBuilder sb =new StringBuilder(128);
		Set<TimerTaskBean> tn =getTimerAll();
		for(TimerTaskBean bean:tn){
			sb.append(bean.thisTimer());
		}
		return sb.toString();
	}
	public static String queryNowObject(){
		StringBuilder sb =new StringBuilder(128);
		for(String k:useObject.keySet()){
			sb.append(k).append("  ");
		}
		return sb.toString();
	}

	public static List<TaskString> converTask(){
		List<TaskString> list= new ArrayList<>();
		for(String k:useObject.keySet()){
			TaskString ts =new TaskString();
			ts.setName(k);
			ts.setTaskJson(useObject.get(k));
			list.add(ts);
		}
		return list;
	}
	public static Set<TimerTaskBean> getTimerAll(){
		return new HashSet<>(timerNum.values());
	}
	public static List<GmHashMap> getTaskObject(String key){
		return useObject.get(key);
	}
	public static void setTaskObject(String key,List<GmHashMap> value){
		useObject.put(key, value);
	}
	public static TimerTaskBean getTimerNum(String key) {
		return timerNum.get(key);
	}
	public static void setTimerNum(String key,TimerTaskBean value){
		timerNum.put(key, value);
	}
	public static void removeTimer(String key) {
		timerNum.remove(key);
		useObject.remove(key);
	}
	public static Set<String> allKeyForTimerNum(){
		return timerNum.keySet();
	}
}
