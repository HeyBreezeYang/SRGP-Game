package com.gmtime.time;

import java.text.ParseException;
import java.util.Date;

import com.gmdesign.exception.GmException;
import com.gmtime.cache.CacheTimer;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.newJob;

/**
 * quartz 2.x 动态添加 修改 删除工具类
 */
public class QuartzManager {
	static Logger log = LoggerFactory.getLogger(QuartzManager.class);

	private QuartzManager(){}
    private static Scheduler scheduler = null;

    static {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
        } catch (SchedulerException ex) {
            log.error("初始化调度器=> [失败]:"+ex.getLocalizedMessage());
        }
    }
	public static void addJob(String dsp,String name,String group, Class clazz,long start,long end ,String config){
    	try {
    		JobDetail job = newJob(clazz).withIdentity(name, group).build();
            CronTriggerImpl trg =new CronTriggerImpl();
            if(start>0){
                trg.setStartTime(new Date(start));
            }
            if(end>0){
                trg.setEndTime(new Date(end));
            }
            trg.setName(dsp);
            trg.setJobName(name);
            trg.setJobGroup(group);
            trg.setKey(TriggerKey.triggerKey(name, group));
            trg.setCronExpression(config);
			scheduler.scheduleJob(job, trg);
		} catch (SchedulerException | ParseException e) {
			log.info("创建作业=> [作业名称："+name+" group "+group+" 失败]"+ e.getMessage());
		}
    }
	public static void removeJob(String name,String group) throws GmException {
        try {
            TriggerKey tk = TriggerKey.triggerKey(name, group);
            scheduler.pauseTrigger(tk);//停止触发器  
            scheduler.unscheduleJob(tk);//移除触发器
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.deleteJob(jobKey);//删除作业
        } catch (SchedulerException e) {
        	String k="删除作业=> [作业名称："+name+" group "+group+" 失败]";
        	log.info(k+ e.getMessage());
        	throw new GmException(k);
        }
    }

    public static void pauseJob(String name,String group)throws GmException {
        try {
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
        	String k="暂停作业=> [作业名称："+name+" group "+group+" 失败]";
        	log.info(k+ e.getMessage());
        	throw new GmException(k);
        }
    }

    public static void resumeJob(String name,String group)throws GmException {
        try {
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
        	String k="恢复作业=> [作业名称："+name+" group "+group+" 失败]";
        	log.info(k+ e.getMessage());
        	throw new GmException(k);
        }
    }
    public static void modifyTime(String dsp,String name,String group,long start,long end ,String config) throws GmException {
        try {
            removeJob(name,group);
            addJob(dsp,name,group,Class.forName(CacheTimer.getTimerNum(name).getClazz()),start,end,config);
            log.info("修改定时任务："+dsp);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String state(String name,String group){
        String s="";
        try {
           s=  scheduler.getTriggerState(TriggerKey.triggerKey(name, group)).name();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return s;
    }
    
    public static void start() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("启动调度器=> [失败] "+e.getMessage());
        }
    }

    public static void shutdown() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error("停止调度器=> [失败] "+e.getMessage());
        }
    }
}