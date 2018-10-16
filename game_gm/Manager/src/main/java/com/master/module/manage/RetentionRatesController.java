package com.master.module.manage;

import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.master.bean.dispoly.Channel;
import com.master.bean.dispoly.GameServer;
import com.master.gm.service.retentionRates.RetentionRatesServiceIF;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * Created by HP on 2018/7/17.
 */
@IocBean
@Ok("json:full")
@At(GmModule.RETENTION_RATES)
public class RetentionRatesController {

    @Inject
    private RetentionRatesServiceIF retentionRates;

    @At("/retentionRates")
    @Ok("jsp:jsp/other/retentionRates/retentionRates")
    public void gotoRetentionRates(HttpServletRequest request){
        List<GameServer> allServer = retentionRates.getAllServer();
        List<Channel> allChannel = retentionRates.getAllChannel();
        request.setAttribute("allServer",allServer);
        request.setAttribute("allChannel",allChannel);
    }

    @At("/queryRetentionRates")
    @Ok("jsp:jsp/other/retentionRates/table/report_table")
    public void queryRetentionRatesData(@Param("sid") String sid, @Param("channel") String channel,@Param("startDate") String startDate, @Param("endDate") String endDate, HttpServletRequest request){

        if (startDate == null || endDate.equals("")){
            startDate = DateUtil.dateToString(new Date());
        }
        if (endDate == null || endDate.equals("")){
            endDate = DateUtil.dateToString(new Date());
        }
        List head=new ArrayList<>();
        request.setAttribute("tableHead",getHead(head,startDate,endDate));

        try {
            List msg=retentionRates.queryRetentionRatesData(sid,channel,startDate,endDate);
            System.out.println("留存率:"+msg);

            Map set = new HashMap();
            set.put("everyDay",DateUtil.getEveryDay(startDate,endDate));
            //set.put("sid",sid);
            set.put("channel",channel);
            for (Object obj:msg) {
                Map map = (Map) obj;
                for (String s:DateUtil.getEveryDay(startDate,endDate)) {
                    if (map.get(s) != null && !map.get(s).toString().equals("")){
                        set.put(s,map.get("newNum"));
                        set.put("sid",map.get("sid"));
                    }
                }
            }
            request.setAttribute("set",set);

            request.setAttribute("RRMsg",msg);
        } catch (GmException e) {
            e.printStackTrace();
        }catch (ParseException p){
            System.out.println("日期转换出错");
        }

    }

    public List getHead(List head,String startDate,String endDate){
        head.add("服务器");
        head.add("渠道");
        head.add("日期");
        head.add("新增");
        List<String> dates = DateUtil.getEveryDay(startDate,endDate);
        int i = 1;
        for (String day:dates) {
            if (i<=7 || i==15 || i==dates.size()){
                head.add(i+"d");
            }
            i++;
        }

        return head;
    }
}
