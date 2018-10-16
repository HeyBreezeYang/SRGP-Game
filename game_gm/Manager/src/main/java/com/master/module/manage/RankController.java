package com.master.module.manage;

import com.gmdesign.util.DateUtil;
import com.master.gm.service.manage.RankServiceIF;
import com.master.module.GmModule;
import org.nutz.http.Http;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by HP on 2018/7/4.
 */
@IocBean
@Ok("json:full")
@At(GmModule.RANK)
public class RankController {

    @Inject
    private RankServiceIF rankService;

    @At("/rank")
    @Ok("jsp:jsp/other/rank/rank")
    public void gotoRankingList(){}

    @At("/queryRank")
    @Ok("jsp:jsp/other/rank/rank")
    public void queryRank(@Param("type") String type, @Param("queryData") String queryData, HttpServletRequest request){
        if (queryData == null || queryData.equals("")){
            queryData=DateUtil.getCurrentTimeMillisToString(System.currentTimeMillis(),"yyyy-MM-dd");
        }
        Map allRank = rankService.getAllRank(type,queryData);
        request.getSession().setAttribute("allRank",allRank);
        System.out.println(allRank);
    }
}
