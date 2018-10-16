package com.master.module.manage;

import javax.servlet.http.HttpServletRequest;

import com.gmdesign.exception.GmException;
import com.master.bean.web.ShopDesign;
import com.master.gm.service.manage.GameConfigServiceIF;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
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
@At(GmModule.SHOP_URL)
public class ShopModule {
    private static Log log = Logs.get();

    @Inject
    private GameConfigServiceIF gameConfig;

    @At("/getAllShop")
    @Ok("jsp:jsp/other/operate/setShop")
    public void gotoGameActivity(HttpServletRequest request,@Param("menuId") int menuId){
        request.setAttribute("thisMenuID",menuId);
        try {
            request.setAttribute("ShopList",gameConfig.getAllShop());
        } catch (GmException e) {
            e.printStackTrace();
        }
    }

    @At("/subDelShop")
    @Ok("json:full")
    public void delShop(@Param("id")int id,@Param("sid") String sid){
        log.info(id);
        try {
            gameConfig.delShop(id,sid);
        } catch (GmException e) {
            e.printStackTrace();
        }
    }

    @At("/subShop")
    @Ok("json:full")
    public void subShop(@Param("..")ShopDesign bean){
        log.info(bean.toString());
        try {
            gameConfig.updateShop(bean);
        } catch (GmException e) {
            e.printStackTrace();
        }
    }
}
