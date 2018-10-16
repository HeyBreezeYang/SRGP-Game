package com.manager.module.login;

import javax.servlet.http.HttpSession;

import com.manager.cache.ContextCache;
import com.manager.module.PlatformManageModule;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by DJL on 2017/6/20.
 *
 * @ClassName LoginModule
 * @Description 登录
 */
@IocBean
@Ok("json:full")
@Fail("http:500")
public class LoginModule {
    private static Log log = Logs.get();

    @At(PlatformManageModule.LOGIN)
    @Ok("re")
    public String gotoInterface(String user, HttpSession session) {
        if(user!=null){
            ContextCache.LOGIN_MARK.remove(user);
            session.removeAttribute("user");
        }
        return "jsp:jsp/login/login";
    }

    @At
    public void removeUser(String user, HttpSession session) {
        log.info("test");
        if(user!=null){
            ContextCache.LOGIN_MARK.remove(user);
            session.removeAttribute("user");
        }
    }


}
