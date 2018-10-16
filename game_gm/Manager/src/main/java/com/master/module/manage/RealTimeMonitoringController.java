package com.master.module.manage;

import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.master.gm.service.manage.RealTimeMonitoringServiceIF;
import com.master.module.GmModule;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by HP on 2018/6/12.
 */
@IocBean
@Ok("json:full")
@At(GmModule.REAL_TIME_MONITORING)
public class RealTimeMonitoringController {

    @Inject
    private RealTimeMonitoringServiceIF rtmService;

    @At("/realTimeMonitoring")
    @Ok("jsp:jsp/other/monitoring/realTimeMonitoring")
    public void  gotoRealTimeMonitoring(HttpServletRequest request){
        List<Record> allServcer =  rtmService.getAllServer();

        request.getSession().setAttribute("allServer",allServcer);

        String toDate = DateUtil.dateToString(new Date());
        try {
            GmHashMap allData = rtmService.queryRealTimeMonitoring(allServcer.get(0).get("serverId").toString(),toDate,toDate);
            request.getSession().setAttribute("allData",allData);
        } catch (GmException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @At("/queryRealTimeMonitoring")
    @Ok("jsp:jsp/other/monitoring/realTimeMonitoring")
    public void queryRealTimeMonitoring(@Param("sid") String sid,@Param("startDate") String startDate,@Param("endDate") String endDate, HttpServletRequest request){

        List<Record> allServcer =  rtmService.getAllServer();

        request.getSession().setAttribute("allServer",allServcer);

        try {
            GmHashMap allData = rtmService.queryRealTimeMonitoring(sid,startDate,endDate);
            request.getSession().setAttribute("allData",allData);
        } catch (GmException e) {
            e.printStackTrace();
            e.getMessage();
        }

    }

    @At("/queryOnlineDiagram")
    @Ok("jsp:jsp/other/monitoring/onlineDiagram")
    public void queryOlineDiagram(@Param("sid") String sid,@Param("startDate") String startDate,@Param("endDate") String endDate, HttpServletRequest request){
        if (startDate == null || startDate.equals("")){
           startDate = DateUtil.dateToString(new Date());
           endDate = DateUtil.dateToString(new Date());
       }
        List<Record> allServcer =  rtmService.getAllServer();
        request.getSession().setAttribute("allServer",allServcer);

        try {
            GmHashMap OnlineNumData = rtmService.queryOnlineNum(allServcer.get(0).get("serverId").toString(),startDate,endDate);
            Set<String> keySet = OnlineNumData.keySet();
            GmHashMap OnlineNumDataJson = new GmHashMap();
            for (String s:keySet ) {
                OnlineNumDataJson.put(s,OnlineNumData.get(s));
            }
            List<String> everyDay = DateUtil.getEveryDay(startDate,endDate);
            OnlineNumDataJson.put("everyDay",everyDay);
            request.getSession().setAttribute("OnlineNumDataJson",JSONObject.toJSONString(OnlineNumDataJson));
        } catch (GmException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

}
