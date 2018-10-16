package com.master.gm.job;

import com.gmdesign.exception.GmException;
import com.master.gm.BaseService;
import com.master.gm.service.Rank.RankTimeServiceIF;
import com.master.util.ManageConf;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.*;

/**
 * Created by HP on 2018/7/4.
 */
@IocBean
public class Rank22Job implements Job {

    @Inject
    private RankTimeServiceIF rankTimeService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("获取排行榜22的定时任务");
        String server= ManageConf.SERVERID;
        try {
            rankTimeService.getGameRankOutFile(server,22);
        } catch (GmException e) {
           System.out.println("获取排行榜22点的定时任务，获取异常");
        }

    }

}
