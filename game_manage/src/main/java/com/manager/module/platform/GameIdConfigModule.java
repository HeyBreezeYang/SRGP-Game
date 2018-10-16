package com.manager.module.platform;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.manager.bean.AppBean;
import com.manager.bean.PlatFormHashMap;
import com.manager.exception.ManageException;
import com.manager.module.PlatformManageModule;
import com.manager.service.AddGameServiceIF;
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
 * @ClassName GameIdConfigModule
 * @Description 平台管理
 */
@IocBean
@Ok("json:full")
@At(PlatformManageModule.PLATFORM)
@Fail("http:500")
public class GameIdConfigModule {
    private static Log log = Logs.get();

    @Inject
    private AddGameServiceIF addGameService;


    @At("/set")
    @Ok("jsp:jsp/platform/addAppId")
    public void setPlatform(@Param("menuId") int menuId, HttpServletRequest request){
        log.info("come menu:"+menuId);
        try {
            request.setAttribute("versionList", addGameService.queryAllVersion());
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }

    @At("/sub")
    @Ok("json:full")
    public String subPlatform(@Param("..") AppBean bean){
        PlatFormHashMap result=new PlatFormHashMap();
        try {
            if(bean.getAppID().trim().length()<1||bean.getOpenID().trim().length()<1){
                throw new ManageException("信息有误~!");
            }
            addGameService.setGameKey(bean);
            result.put("res",1);
        } catch (ManageException e) {
            log.error(e.getMessage());
            result.put("res",0);
            result.put("error",e.getMessage());
        }
        return JSON.toJSONString(result);
    }
}
