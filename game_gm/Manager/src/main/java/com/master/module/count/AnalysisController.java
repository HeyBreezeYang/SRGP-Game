package com.master.module.count;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.master.gm.service.count.AnalysisDataServiceIF;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/10/19.
 *
 * @ClassName AnalysisController
 * @Description 数据分析
 */
@IocBean
@Ok("json:full")
@At(GmModule.ANALYSIS_URL)
@Fail("http:500")
public class AnalysisController {
    private static Log log = Logs.get();

    @Inject
    private AnalysisDataServiceIF analysisDataService;

    @At("/onlineNum")
    @Ok("jsp:jsp/other/query/onlineNum")
    public void comeOnlineNum(){}
    @At("/ltv")
    @Ok("jsp:jsp/other/query/ltv")
    public void comeLTV(){}

    @At("/dataForOnline")
    @Ok("json:full")
    public GmHashMap queryOnlineNum(@Param("serverId") String serverId,@Param("start") String start,@Param("end") String end){
        try {
            return analysisDataService.queryOnlineNum(serverId,start,end);
        } catch (GmException e) {
            log.error(e.getMessage());
            GmHashMap error= new GmHashMap();
            error.put("msg",e.getMessage());
            return error;
        }
    }

    @At("/ltvMsg")
    @Ok("json:full")
    public GmHashMap queryLTV(@Param("serverId") String serverId,
                              @Param("cst") String createStart,@Param("ced") String createEnd,
                              @Param("pst") int payStart,@Param("ped") int payEnd){
        if(payStart<1||payStart>30){
            payStart=1;
        }
        if(payEnd<1||payEnd>30){
            payEnd=30;
        }
        return analysisDataService.queryDetailedLTV(serverId,createStart,createEnd,payStart,payEnd);
    }

}
