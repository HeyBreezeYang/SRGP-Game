
package com.master.gm.job2;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;


/**
 * Created by HP on 2018/7/11.
 */

@IocBean
public class ObjectJob  {

    @Inject
    private static Log log = Logs.get();

    public void addJob(long time,String url,String sid,String username,int type) throws SchedulerException {

        //1.创建Scheduler的工厂
        SchedulerFactory sf = new StdSchedulerFactory();
        //2.从工厂中获取调度器实例
        Scheduler scheduler = sf.getScheduler();


        //3.创建JobDetail
        JobDetail jb = JobBuilder.newJob(PlayerJob.class)
                .withDescription("this is a PlayerJob job") //job的描述
                .withIdentity("player"+System.currentTimeMillis(), "playerGroup") //job 的name和group
                .build();

        //向execute方法传参
        jb.getJobDataMap().put("url",url);
        jb.getJobDataMap().put("sid",sid);
        jb.getJobDataMap().put("username",username);
        jb.getJobDataMap().put("type",type);
        //任务运行的时间，SimpleSchedle类型触发器有效
//        long t=  System.currentTimeMillis() + 3*1000L; //3秒后启动任务
        Date statTime = new Date(time);

        //4.创建Trigger
        //使用SimpleScheduleBuilder或者CronScheduleBuilder
        Trigger tri = TriggerBuilder.newTrigger()
                .withDescription("")
                .withIdentity("playerTrigger"+System.currentTimeMillis(), "playerTriggerGroup")
                //.withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .startAt(statTime)  //默认当前时间启动
                //.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?")) //两秒执行一次
                .build();

        //5.注册任务和定时器
        scheduler.scheduleJob(jb, tri);

        //6.启动 调度器
        scheduler.start();
        log.info("启动时间 ： " + new Date());

    }
}

