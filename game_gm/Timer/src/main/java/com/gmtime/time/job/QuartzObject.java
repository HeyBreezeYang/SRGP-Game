package com.gmtime.time.job;

import java.util.List;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmtime.cache.CacheTimer;
import com.gmtime.rmi.service.bean.TimerTaskBean;
import com.gmtime.time.QuartzManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class QuartzObject implements Job {
	protected static Logger log = LoggerFactory.getLogger(QuartzObject.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String key=arg0.getJobDetail().getKey().getName();
		TimerTaskBean bean= CacheTimer.getTimerNum(key);
		List<GmHashMap> objList=CacheTimer.getTaskObject(key);
		if(bean==null){
			String group =arg0.getJobDetail().getKey().getGroup();
			log.info("幽灵任务："+key);
			try {
				QuartzManager.removeJob(key, group);
			} catch (GmException e) {
				throw new JobExecutionException(e.getMessage());
			}
		}else{
			job(objList);
		}
	}
	public abstract void job(List<GmHashMap> objs);
}
