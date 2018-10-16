package com.manager.module.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.manager.bean.GameBean;
import com.manager.bean.LogBean;
import com.manager.bean.UserBean;
import com.manager.bean.VersionBean;
import com.manager.cache.ContextCache;
import com.manager.exception.ManageException;
import com.manager.module.PlatformManageModule;
import com.manager.service.GmLoginLogServiceIF;
import com.manager.service.UserServiceIF;
import com.manager.util.StringUtil;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/6/20.
 *
 * @ClassName MainMenuModule
 * @Description 主菜单
 */
@IocBean
@Ok("json:full")
@At(PlatformManageModule.MAIN_MENU)
@Fail("http:500")
public class MainMenuModule {

    @Inject
    private UserServiceIF userService;

    @Inject
    private GmLoginLogServiceIF logService;

    @At
    @Ok("re")
    public String comePlatform(HttpSession session, @Param("..") UserBean user, HttpServletRequest request,@Param("type") int type){
        user.setPassword(StringUtil.changePassword(user.getPassword()));
        try {
            user=this.userService.judgePassword(user);
            user.setLoginTime(System.currentTimeMillis());
            user.setLoginState(true);
            user.setUserKey(StringUtil.byte2hex(user.getName().getBytes()));
            ContextCache.LOGIN_MARK.put(user.getName(),user.isLoginState());
            GameBean tsBean=null;
            for (GameBean b:user.getGame()){
                if(b.getId()==0){
                    tsBean=b;
                    break;
                }
            }
            if (tsBean!=null){
                user.getGame().remove(tsBean);
                for(VersionBean vb:tsBean.getVersion()){
                    if(vb.getId()==14){
                        request.setAttribute("dataMsg",vb);
                    }else if (vb.getId()==15){
                        request.setAttribute("accountMsg",vb);
                    }
                }
            }
            session.setAttribute("user", user);
            LogBean log=new LogBean();
            log.setTime(user.getLoginTime());
            log.setUser(user);
            log.setType("登录");
            log.setMessage(user.getName()+" 成功进入了GM登录系统。");
            logService.saveLoginLog(log);
        } catch (ManageException e) {
            e.printStackTrace();
            request.setAttribute("userError",e.getMessage());
            if(type==0){
                return "jsp:jsp/login/login";
            }else{
                return "jsp:jsp/login/reLogin";
            }
        }
        if(type==0){
            return "jsp:jsp/main/main";
        }else{
            return "jsp:jsp/main/windowMain";
        }
    }

}
