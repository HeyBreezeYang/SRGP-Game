package com.master.module.login;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gmdesign.bean.other.UserForService;
import com.master.bean.dispoly.Channel;
import com.master.bean.dispoly.GameServer;
import com.master.cache.InitTableDataCache;
import com.master.gm.service.login.impl.ManagerLoginService;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

/**
 * Created by DJL on 2017/3/3.
 *
 * @ClassName GM
 * @Description
 */
@IocBean
@Ok("json:full")
@At(GmModule.BASE_URL)
@Fail("http:500")
@Filters
public class MenuDataModule {

    @Inject
    private ManagerLoginService managerLoginService;

    private static Log logger = Logs.get();
    @At
    @Ok("re")
    public String come( @Param("menuData")String menuData, @Param("..") UserForService user, HttpServletRequest request,
                        HttpSession session,@Param("versionName")String versionName){
        logger.info(user.toString());
        try{
            session.setAttribute("GmBase","../../../Manager");
            session.setAttribute("menu", this.managerLoginService.judgeUserData(menuData, user));
            session.setAttribute("vName", versionName);
            session.setAttribute("GM",user);
            session.setAttribute("channl",managerLoginService.getDao().query(Channel.class,null));
            session.setAttribute("serverConfig",managerLoginService.getDao().query(GameServer.class,null));

            if(InitTableDataCache.judge(InitTableDataCache.getReport())){
                InitTableDataCache.setReport(managerLoginService.initWindowComprehensive());
            }
            if(InitTableDataCache.judge(InitTableDataCache.getRate())){
                InitTableDataCache.setRate(managerLoginService.initWindowRate());
            }

            request.setAttribute("comprehensive", InitTableDataCache.getReport());
            request.setAttribute("rate",InitTableDataCache.getRate());
            request.setAttribute("nowData",InitTableDataCache.getNowData());
            request.setAttribute("nowTime",System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            request.setAttribute("dataErorr", e.getMessage());
            return "jsp:jsp/error/error";
        }
        return "jsp:jsp/main/logindata";
    }

    @At
    @Ok("json:full")
    public List<String[]> queryNowData(){
        return InitTableDataCache.getNowData();
    }

}
