package com.master.gm.job2;

import com.gmdesign.exception.GmException;
import com.gmdesign.util.URLTool;
import com.master.bean.back.BanBack;
import com.master.gm.BaseService;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by HP on 2018/7/11.
 */
@IocBean
public class PlayerJob extends BaseService implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap map = jobExecutionContext.getJobDetail().getJobDataMap();
        String url = map.get("url").toString();
        String sid = map.get("sid").toString();
        String username = map.get("username").toString();
        String type = map.get("type").toString();
        System.out.println("调用解禁定时任务:"+url);
        try {
            BanBack bean=this.dao.fetch(BanBack.class, Cnd.where("sid","=",sid).and("playerName","=",username).and("type","=",type));
           if (bean != null){
               URLTool.sendMsg(url,null);
               this.dao.delete(bean);
           }
        } catch (GmException e) {
            e.printStackTrace();
        }
    }
}
