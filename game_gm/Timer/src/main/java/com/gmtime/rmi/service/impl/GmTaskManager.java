package com.gmtime.rmi.service.impl;

import com.gmdesign.exception.GmException;
import com.gmtime.cache.CacheTimer;
import com.gmtime.rmi.service.RmiResult;
import com.gmtime.time.QuartzManager;

/**
 * 
* @ClassName GmTaskManager
* @Description 定时任务管理
* @author DJL
* @date 2015-12-23 下午7:20:21
*
 */
public class GmTaskManager implements RmiResult {
	private static final int CHANGE_TIMER_STATE=10001;
	private static final int DEL_TIMER=10002;
	private static final int RE_TIMER=10003;
	@Override
	public Object rmiResult(int code,Object... prmas) {
		switch (code) {
			case CHANGE_TIMER_STATE:
				return changeTimerState(prmas[0].toString(),prmas[1].toString(),Integer.parseInt(prmas[2].toString()));
			case DEL_TIMER:
				return delTimer(prmas[0].toString(),prmas[1].toString());
			case RE_TIMER:
				return reTimer(prmas);
			default:
				return null;
		}
	}
	private boolean reTimer(Object[] parms){
		boolean flag=true;
		String dsp=parms[0].toString();
		String name=parms[1].toString();
		String group=parms[2].toString();
		long start =Long.parseLong(parms[3].toString());
		long end=Long.parseLong(parms[4].toString());
		String config=parms[5].toString();
		try {
			QuartzManager.modifyTime(dsp,name, group, start, end, config);
			CacheTimer.setTimer(name,config,start,end,(short)-1);
		} catch (GmException e) {
			flag=false;
			e.printStackTrace();
		}
		return flag;
	}
	private boolean delTimer(String name,String group){
		boolean flag=false;
		try {
			QuartzManager.removeJob(name, group);
			CacheTimer.removeTimer(name);
			flag= true;
		} catch (GmException e) {
			e.printStackTrace();
		}
		return flag;
	}
	private boolean changeTimerState(String name,String group,int state){
		boolean flag=false;
		try {
			if(state==1){
				QuartzManager.resumeJob(name, group);
			}else if(state==0){
				QuartzManager.pauseJob(name, group);
			}
			CacheTimer.setTimer(name,null,0,0,(short)state);
			flag= true;
		} catch (GmException e) {
			e.printStackTrace();
		}
		return flag;
	}
}

