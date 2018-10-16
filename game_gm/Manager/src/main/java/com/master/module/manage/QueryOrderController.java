package com.master.module.manage;

import com.master.gm.service.manage.QueryOrderServiceIF;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/6.
 */
@IocBean
@Ok("json:full")
@At(GmModule.ORDER)
public class QueryOrderController {

    @Inject
    private QueryOrderServiceIF orderService;

    @At("/queryOrderPage")
    @Ok("jsp:jsp/other/order/queryOrder")
    public void gotoQueryOrder(HttpServletRequest request){
        queryAllSelect(request);
        request.getSession().setAttribute("orders",null);
    }

    @At("/queryOrder")
    @Ok("jsp:jsp/other/order/queryOrder")
    public void queryOrder(@Param("sid") String sid,@Param("channel") String channel, @Param("paySetMeal") String paySetMeal,@Param("status") String status,
                           @Param("startDate") String startDate,@Param("endDate") String endDate,@Param("pid") String pid,@Param("orderNum") String orderNum,HttpServletRequest request){

        List<Map> orders= orderService.queryOrder(sid,channel,paySetMeal,status,startDate,endDate,pid,orderNum);
        request.getSession().setAttribute("orders",orders);
    }

    public void queryAllSelect(HttpServletRequest request){

        Map allSelect = orderService.queryQuerySelect();
        request.getSession().setAttribute("allSelect",allSelect);
    }

}
