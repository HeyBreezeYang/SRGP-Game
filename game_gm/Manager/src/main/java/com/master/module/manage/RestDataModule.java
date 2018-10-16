package com.master.module.manage;

import java.rmi.RemoteException;

import com.gmdesign.exception.GmException;
import com.gmdesign.rmi.GmOperate;
import com.gmdesign.rmi.impl.GmRmiOperate;
import com.master.bean.dispoly.Parameter;
import com.master.cache.GmDocConfig;
import com.master.cache.InitTableDataCache;
import com.master.gm.service.timer.GmTimerService;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/8/4.
 *
 * @ClassName RestDataModule
 * @Description GM重置功能
 */
@IocBean
@Ok("json:full")
@At(GmModule.RESET_URL)
public class RestDataModule {


    @Inject
    private GmTimerService gmTimerService;

    @At("/resetData")
    @Ok("jsp:jsp/other/gm/clear")
    public void gotoRest(){}

    @At
    @Ok("json:full")
    public void subBackUp(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("type") int type){
        InitTableDataCache.clearAll();
        try {
            GmOperate gmRmi= GmRmiOperate.getGameRmiServer(gmTimerService.getDao().fetch(Parameter.class,"5").getPrams());
            if(gmRmi==null){
                throw new GmException("gm rmi 连接失败~~");
            }
            gmRmi.result("gm_back",type,startTime,endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("json:full")
    public void subCache(int type){
        if(type<10000){
            forManager(type);
        }else{
            try {
                forTimer(type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void forTimer(int type) throws GmException, RemoteException {
        GmOperate gmRmi= GmRmiOperate.getGameRmiServer(gmTimerService.getDao().fetch(Parameter.class,"5").getPrams());
        if(gmRmi==null){
            throw new GmException("gm rmi 连接失败~~");
        }
        gmRmi.result("gm_clear",type);
    }
    private void forManager(int type){
        if(type==1001){
            GmDocConfig.clearAll();
            GmDocConfig.initFile();
        }
        if (type == 0){
            System.out.println("重置配置文件");
            GmDocConfig.clearAll();
            GmDocConfig.initFile();
        }

    }

}
