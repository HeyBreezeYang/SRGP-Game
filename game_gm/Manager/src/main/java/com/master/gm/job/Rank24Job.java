package com.master.gm.job;

import com.gmdesign.exception.GmException;
import com.master.gm.BaseService;
import com.master.gm.service.Rank.RankTimeServiceIF;
import com.master.util.ManageConf;
import org.nutz.integration.quartz.annotation.Scheduled;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by HP on 2018/7/4.
 */
//@Scheduled(cron="0 0 24 * * ?") //直接使用注解来声明cron
@IocBean
public class Rank24Job implements Job {

    @Inject
    private RankTimeServiceIF rankTimeService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("获取排行榜24点的定时任务");
        String server= ManageConf.SERVERID;
        try {
            rankTimeService.getGameRankOutFile(server,24);
        } catch (GmException e) {
            System.out.println("获取排行榜24点的定时任务，获取异常");
        }

    }

}
