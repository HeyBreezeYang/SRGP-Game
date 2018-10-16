package com.master.module.manage;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.master.gm.service.manage.GameConfigServiceIF;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2018/4/2.
 *
 * @ClassName gm
 * @Description
 */
@IocBean
@Ok("json:full")
@At(GmModule.EXC_URL)
public class ExcModule {


    @Inject
    private GameConfigServiceIF gameConfig;

    @At("/getAllExc")
    @Ok("jsp:jsp/other/operate/setExc")
    public void gotoAllExc(HttpServletRequest request,@Param("menuId") int menuId){
        request.setAttribute("thisMenuID",menuId);
        try {
            request.setAttribute("ExcList",gameConfig.getAllExc());
        } catch (GmException e) {
            e.printStackTrace();
        }
    }

    @At("/subExc")
    @Ok("json:full")
    public void subExc(@Param("type") String type,@Param("sTime") String sTime,@Param("eTime") String eTime,@Param("hTime") String hTime,@Param("server") String sid){
        try {
            GmHashMap res =new GmHashMap();
            res.put("type",type);
            res.put("sTime",sTime);
            res.put("eTime",eTime);
            res.put("hTime",hTime);
            System.out.println(res);
            gameConfig.updateExc(sid,"params="+ JSON.toJSONString(res));
        } catch (GmException e) {
            e.printStackTrace();
        }
    }

    @At("/getAllExp")
    @Ok("jsp:jsp/other/operate/setExp")
    public void gotAllExp(HttpServletRequest request,@Param("menuId") int menuId){
        request.setAttribute("thisMenuID",menuId);
        try {
            request.setAttribute("ExpList",gameConfig.getAllExp());
        } catch (GmException e) {
            e.printStackTrace();
        }
    }

    @At("/subExp")
    @Ok("json:full")
    public void subExp(@Param("sTime") String sTime,@Param("eTime") String eTime,@Param("hTime") String hTime,@Param("server") String sid){
        try {
            GmHashMap res =new GmHashMap();
            res.put("sTime",sTime);
            res.put("eTime",eTime);
            res.put("hTime",hTime);
            System.out.println(res);
            gameConfig.updateExp(sid,"params="+ JSON.toJSONString(res));
        } catch (GmException e) {
            e.printStackTrace();
        }
    }


}
