package com.master.module.manage;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.master.bean.dispoly.Channel;
import com.master.bean.dispoly.GameServer;
import com.master.gm.service.LTV.LTVServiceIF;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 2018/7/20.
 */
@IocBean
@Ok("json:full")
@At(GmModule.LTV)
public class LTVController {

    @Inject
    private LTVServiceIF LTVService;

    @At("/LTV")
    @Ok("jsp:jsp/other/LTV/LTV")
    public void gotoLTV(HttpServletRequest request){
        List<GameServer> allServer = LTVService.getAllServer();
        List<Channel> allChannel = LTVService.getAllChannel();
        request.setAttribute("allServer",allServer);
        request.setAttribute("allChannel",allChannel);
    }

    @At("/queryLTV")
    @Ok("jsp:jsp/other/LTV/table/report_table")
    public void queryLTV(@Param("sid") String sid, @Param("channel") String channel, @Param("LTVType") Integer LTVType, @Param("rstartDate") String rstartDate,
                         @Param("rendDate") String rendDate, @Param("qstartDate") String qstartDate, @Param("qendDate") String qendDate, HttpServletRequest request){
        System.out.println("接收到的参数:sid:"+sid+"==channel:"+channel+"==LTVType:"+LTVType+"==rstartDate:"+rstartDate+"==rendDate:"+rendDate+"==qstartDate:"+qstartDate+"==qendDate:"+qendDate);
        if (rstartDate == null || rstartDate.equals("")){
            rstartDate = DateUtil.dateToString(new Date());
        }
        if (rendDate == null || rendDate.equals("")){
            rendDate = DateUtil.dateToString(new Date());
        }
        if (qstartDate==null||qstartDate.equals("")){
            qstartDate = rstartDate;
        }
        if (qendDate==null||qendDate.equals("")){
            qendDate=rstartDate;
        }

        List head=new ArrayList<>();
        request.setAttribute("tableHead",getHead(head,qstartDate,qendDate));

        try {
            List<GmHashMap> LTVs = LTVService.queryLTV(sid,channel,LTVType,rstartDate,rendDate,qstartDate,qendDate);
            request.setAttribute("LTVs",LTVs);
        } catch (GmException e) {
            e.printStackTrace();
        }

    }


    public List getHead(List head,String startDate,String endDate){
        head.add("创建日期");
        head.add("注册人数");
        List<String> dates = DateUtil.getEveryDay(startDate,endDate);
        if (dates.size()>30){
            for (int i = 0;i<30;i++){
                String d = dates.get(i).replaceAll("-","").substring(4);
                head.add(d);
            }
        }else {
            for (String day:dates) {
                String d =day.replaceAll("-","").substring(4);
                head.add(d);
            }
        }


        return head;
    }
}
