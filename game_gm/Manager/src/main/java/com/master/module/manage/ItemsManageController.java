package com.master.module.manage;

import com.gmdesign.exception.GmException;
import com.master.gm.service.manage.ItemsManageServiceIF;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by HP on 2018/6/29.
 */
@IocBean
@Ok("json:full")
@At(GmModule.ITEMS_MANAGE)
public class ItemsManageController {

    @Inject
    private ItemsManageServiceIF itemsManage;

    @At("/itemsManage")
    @Ok("jsp:jsp/other/itemsManage/itemsManage")
    public void gotoItemsManage(HttpServletRequest request){
        request.getSession().setAttribute("cur",null);
        request.getSession().setAttribute("itemsInfo",null);
        request.getSession().setAttribute("status",null);
    }

    @At("/queryRoleItems")
    @Ok("jsp:jsp/other/itemsManage/itemsManage")
    public void queryRoleItems(@Param("rolename") String rolename, HttpServletRequest request){
        Map itemsInfo = itemsManage.queryRoleItems(rolename);
       if (itemsInfo.get("code").toString().equals("0")){
           if (itemsInfo != null && itemsInfo.size()>0 && itemsInfo.get("code").toString().equals("0")){
               request.getSession().setAttribute("cur",itemsInfo.get("cur"));
           }
           request.getSession().setAttribute("itemsInfo",itemsInfo);
           request.getSession().setAttribute("status","查询成功");
       }else {
           request.getSession().setAttribute("cur",null);
           request.getSession().setAttribute("itemsInfo",null);
           request.getSession().setAttribute("status","角色不存在");
       }
    }

    @At("/changeCurrency")
    @Ok("json:full")
    public String changeCurrency(@Param("pid") String pid,@Param("goodsId")String goodsId,@Param("changeNum")String changeNum){
        try {
            itemsManage.changeCurrency(pid,goodsId,changeNum);
            return "修改成功";
        } catch (GmException e) {
            e.printStackTrace();
            return "修改失败";
        }

    }

    @At("/changeGoods")
    @Ok("json:full")
    public String changeGoods(@Param("pid") String pid,@Param("goodsId")String goodsId,@Param("changeNum")String changeNum){

        try {
            itemsManage.changeGoods(pid,goodsId,changeNum);
            return "修改成功";
        } catch (GmException e) {
            e.printStackTrace();
            return "修改失败";
        }
    }
}
