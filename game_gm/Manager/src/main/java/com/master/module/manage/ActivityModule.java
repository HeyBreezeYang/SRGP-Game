package com.master.module.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.master.bean.dispoly.GmLog;
import com.master.gm.service.manage.impl.GameConfigService;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/8/2.
 *
 * @ClassName ActivityModule
 * @Description 活动管理
 */
@IocBean
@Ok("json:full")
@At(GmModule.ACTIVITY_URL)
public class ActivityModule {
    private static Log log = Logs.get();
    @Inject
    private GameConfigService gameConfigService;

    @At("/getAllActivity")
    @Ok("jsp:jsp/other/operate/activity")
    public void gotoGameActivity(HttpServletRequest request,@Param("menuId") int menuId){
        try {
            request.setAttribute("thisMenuID",menuId);
            request.setAttribute("allActivity",gameConfigService.getAllActivity());
            request.setAttribute("allServer",gameConfigService.getAllServer());
        } catch (GmException e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("json:full")
    public void deleteActivity(HttpSession session,@Param("id") String id, @Param("sid") String sid){
        try {
            UserForService user = (UserForService) session.getAttribute("GM");
            if(user==null){
                throw new GmException("user is null!");
            }
            this.gameConfigService.deleteActivity(sid,id);
            GmLog bean =new GmLog();
            bean.setName(user.getName());
            bean.setTime(System.currentTimeMillis());
            bean.setType("activity");
            bean.setContext("delete activity:".concat(id).concat("for ").concat(sid));
            this.gameConfigService.getDao().insert(bean);
        } catch (GmException e) {
            log.error(e.getMessage());
        }
    }

    @At
    @Ok("json:full")
    public void batchDelActivity(HttpSession session,@Param("msg") String msg){
        String[] ids=msg.split(",");
        try {
            UserForService user = (UserForService) session.getAttribute("GM");
            if(user==null){
                throw new GmException("user is null!");
            }
            for(String aid_sid:ids){
                String[] as=aid_sid.split("-");
                try {
                    this.gameConfigService.deleteActivity(as[1],as[0]);
                } catch (GmException e) {
                    log.error(e.getMessage());
                }
            }
            GmLog bean =new GmLog();
            bean.setName(user.getName());
            bean.setTime(System.currentTimeMillis());
            bean.setType("activity");
            bean.setContext("delete activity:".concat(msg));
            this.gameConfigService.getDao().insert(bean);
        }catch (GmException e){
            log.error(e.getMessage());
        }
    }

}
