package com.master.gm.job;

import com.gmdesign.exception.GmException;
import com.master.gm.BaseService;
import com.master.gm.service.Rank.VipRankServiceIF;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by HP on 2018/7/19.
 */
@IocBean
public class VipJob implements Job {

    @Inject
    private VipRankServiceIF vipRank;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("vip排名信息更新任务");
        try {
            vipRank.toDBVipRankInfo();
        } catch (GmException e) {
            System.out.println("vip排名信息更新任务异常");
        }
    }
}
