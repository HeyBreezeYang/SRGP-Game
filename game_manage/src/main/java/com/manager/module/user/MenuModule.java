package com.manager.module.user;

import javax.servlet.http.HttpServletRequest;

import com.manager.bean.GameBean;
import com.manager.bean.GroupBean;
import com.manager.bean.MenuBean;
import com.manager.bean.VersionBean;
import com.manager.exception.ManageException;
import com.manager.module.PlatformManageModule;
import com.manager.service.MenuServiceIF;
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
 * @ClassName MenuModule
 * @Description 菜单设置
 */
@IocBean
@Ok("json:full")
@At(PlatformManageModule.MENU)
@Fail("http:500")
public class MenuModule {
    private static Log log = Logs.get();
    @Inject
    private MenuServiceIF menuService;

    @At
    @Ok("jsp:jsp/operation/menu/game_menu")
    public void gameList(@Param("menuId") int menuId, HttpServletRequest request){
        log.info("come menu:"+menuId);
        try {
            request.setAttribute("allGame", menuService.queryGameMenu());
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }
    @At
    @Ok("jsp:jsp/operation/menu/version_menu")
    public void versionList(@Param("menuId") int menuId, HttpServletRequest request){
        try {
            request.setAttribute("gid",menuId);
            request.setAttribute("allVersion", menuService.queryVersionMenu(menuId));
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }
    @At
    @Ok("jsp:jsp/operation/menu/group_menu")
    public void groupList(@Param("menuId") int menuId, HttpServletRequest request){
        try {
            request.setAttribute("vid",menuId);
            request.setAttribute("gid",menuService.queryVersion(menuId).getGameId());
            request.setAttribute("allGroup", menuService.queryGroupMenuForVersion(menuId));
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }
    @At
    @Ok("jsp:jsp/operation/menu/menu")
    public void menuList(@Param("menuId") int menuId, HttpServletRequest request){
        try {
            request.setAttribute("groupID",menuId);
            request.setAttribute("vid",menuService.queryGroup(menuId).getVid());
            request.setAttribute("sonGroup", menuService.queryGroupMenuForGroup(menuId));
            request.setAttribute("allMenu", menuService.queryMenu(menuId));
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("json:full")
    public void updateGame(@Param("..")GameBean bean){
        try {
            if(bean.getId()==0){
                this.menuService.createGameMenu(bean);
            }else{
                this.menuService.updateGameMenu(bean);
            }
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }
    @At("/delGame")
    @Ok("json:full")
    public void deleteGame(@Param("id")int id){
        try {
            this.menuService.deleteGameMenu(id);
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("json:full")
    public void updateVersion(@Param("..")VersionBean bean){
        try {
            if(bean.getId()==0){
                this.menuService.createVersionMenu(bean);
            }else{
                this.menuService.updateVersionMenu(bean);
            }
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }
    @At("/delVersion")
    @Ok("json:full")
    public void deleteVersion(@Param("id")int id){
        try {
            this.menuService.deleteVersionMenu(id);
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }


    @At
    @Ok("json:full")
    public void updateGroup(@Param("..")GroupBean bean){
        try {
            if(bean.getId()==0){
                this.menuService.createGroupMenu(bean);
            }else{
                this.menuService.updateGroupMenu(bean);
            }
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }
    @At("/delGroup")
    @Ok("json:full")
    public void deleteGroup(@Param("id")int id){
        try {
            this.menuService.deleteGroupMenu(id);
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("json:full")
    public void updateMenu(@Param("..")MenuBean bean){
        try {
            if(bean.getId()==0){
                this.menuService.createMenu(bean);
            }else{
                this.menuService.updateMenu(bean);
            }
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }
    @At("/delMenu")
    @Ok("json:full")
    public void deleteMenu(@Param("id")int id){
        try {
            this.menuService.deleteMenu(id);
        } catch (ManageException e) {
            e.printStackTrace();
        }
    }




}
