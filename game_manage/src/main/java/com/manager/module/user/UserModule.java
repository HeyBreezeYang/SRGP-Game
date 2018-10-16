package com.manager.module.user;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.manager.bean.PlatFormHashMap;
import com.manager.bean.UserBean;
import com.manager.exception.ManageException;
import com.manager.module.PlatformManageModule;
import com.manager.service.MenuServiceIF;
import com.manager.service.UserServiceIF;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/6/20.
 *
 * @ClassName UserModule
 * @Description 用户设置
 */
@IocBean
@Ok("json:full")
@At(PlatformManageModule.USER)
@Fail("http:500")
public class UserModule {
    private static Log log = Logs.get();

    @Inject
    private UserServiceIF userService;
    @Inject
    private MenuServiceIF menuService;

    @At("/list")
    @Ok("jsp:jsp/query/userList")
    public void queryUserList(@Param("menuId") int menuId, HttpServletRequest requset){
        log.info("come menu:"+menuId);
        try {
            requset.setAttribute("userList",userService.allUser());
        } catch (ManageException e) {
            log.error(e.getMessage());
        }
    }

    @At("/add")
    @Ok("jsp:jsp/operation/addUser")
    public void addUser(@Param("menuId") int menuId){
        log.info("come menu:"+menuId);
    }

    @At("/setPwd")
    @Ok("jsp:jsp/operation/resetPassword")
    public void resetPassword(@Param("menuId") int menuId){
        log.info("come menu:"+menuId);
    }

    @At("/subNewPwd")
    @Ok("json:full")
    public String subResetPwd(@Param("..") UserBean bean,@Param("pwdN") String pwd_new){
        PlatFormHashMap result=new PlatFormHashMap();
        try {
            userService.resetPassword(bean,pwd_new);
            result.put("res",1);
        } catch (ManageException e) {
            e.printStackTrace();
            result.put("res",0);
            result.put("msg",e.getMessage());
        }
        return JSON.toJSONString(result);
    }


    @At("/sub")
    @Ok("json:full")
    public String subUser(@Param("..")UserBean bean){
        PlatFormHashMap result=new PlatFormHashMap();
        try {
            if(bean.getName().trim().length()<=1||bean.getPassword().trim().length()<=1){
                throw new ManageException("账号信息不全!~");
            }
            userService.createUser(bean);
            result.put("res",1);
        } catch (ManageException e) {
            e.printStackTrace();
            result.put("res",0);
            result.put("msg",e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    @At("/power")
    @Ok("jsp:jsp/operation/power")
    public void gotoPower(@Param("menuId") int menuId,HttpServletRequest request){
        log.info("come menu:"+menuId);
        try {
            request.setAttribute("allMenu",menuService.getAllMenu());
        } catch (ManageException e) {
            log.error(e.getMessage());
        }
    }

    @At
    @Ok("json:full")
    public String queryPower(@Param("name")String name){
        PlatFormHashMap result=new PlatFormHashMap();
        try {
            result.put("res",1);
            result.put("msg",this.userService.userPower(name));
        } catch (ManageException e) {
            log.error(e.getMessage());
            result.put("res",0);
            result.put("error",e.getMessage());
        }
        return JSON.toJSONString(result);
    }
    @At
    @Ok("json:full")
    public String subPower(@Param("name")String name,@Param("menus")int[] menus){
        PlatFormHashMap result=new PlatFormHashMap();
        try {
            this.menuService.updatePower(name,menus);
            result.put("res",1);
        } catch (ManageException e) {
            log.error(e.getMessage());
            result.put("res",0);
            result.put("error",e.getMessage());
        }
        return JSON.toJSONString(result);
    }

}
