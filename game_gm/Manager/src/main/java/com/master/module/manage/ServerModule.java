package com.master.module.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gmdesign.bean.other.GmHashMap;
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
 * Created by DJL on 2017/8/10.
 *
 * @ClassName GM
 * @Description
 */
@IocBean
@Ok("json:full")
@At(GmModule.SERVER_URL)
public class ServerModule {
    private static Log log = Logs.get();

    @Inject
    private GameConfigService gameConfigService;

    @At("/oneTimeModify")
    @Ok("jsp:jsp/other/operate/server/onetime")
    public void comeSetServerForOneChecked(HttpServletRequest request, @Param("menuId") int menuId){
        try {
            request.setAttribute("thisMenuID",menuId);
            request.setAttribute("serverList",gameConfigService.queryAllConfig());
        } catch (GmException e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("json:full")
    public String changeServerState(HttpSession session, @Param("serverArray") String server, @Param("state")int state){
        try {
            UserForService user = (UserForService) session.getAttribute("GM");
            if(user==null){
                return "修改失败:user null!~";
            }
            GmHashMap res=this.gameConfigService.changeServerState(server,state);
            GmLog bean =new GmLog();
            bean.setName(user.getName());
            bean.setTime(System.currentTimeMillis());
            bean.setType("server");
            bean.setContext("change server:".concat(server).concat(" is ")+state);
            this.gameConfigService.getDao().insert(bean);
            return res.toString();
        } catch (GmException e) {
            e.printStackTrace();
        }
        return "修改失败!~";
    }

    @At
    @Ok("json:full")
    public String changeServerRecommend(@Param("sid") String server){
        try {
            this.gameConfigService.changeServerRecommend(server);
            return server.concat("修改成功!~");
        } catch (GmException e) {
            e.printStackTrace();
        }
        return "修改失败!~";
    }


}
