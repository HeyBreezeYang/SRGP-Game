package com.master.module.manage;

import com.gmdesign.exception.GmException;
import com.master.gm.service.manage.IpWhiteListServiceIF;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/6.
 */
@IocBean
@Ok("json:full")
@At(GmModule.IP_WHITE_LIST)
public class IpWhiteListController {

    @Inject
    private IpWhiteListServiceIF iplistService;

    @At("/ipWhiteListPage")
    @Ok("jsp:jsp/other/ipWhiteList/ipWhiteList")
    public void gotoIpWhiteList(HttpServletRequest request){
        List<Map> ipWhiteLists = iplistService.getAllIpWhiteList();
        request.getSession().setAttribute("ipWhiteLists",ipWhiteLists);
    }

    @At("/addIpWhiteList")
    @Ok("json:full")
    public String addIpWhiteList(@Param("ip") String ip,@Param("remark") String remark){
        try {
            iplistService.addIpWhiteList(ip,remark);
            return "增加成功";
        } catch (GmException e) {
            e.printStackTrace();
            return "增加失败";
        }

    }

    @At("/delIpWhiteList")
    @Ok("json:full")
    public String delIpWhiteList(@Param("id") String id){
        try {
            iplistService.delIpWhiteList(id);
            return "删除成功";
        } catch (GmException e) {
            e.printStackTrace();
            return "删除失败";
        }
    }
}
