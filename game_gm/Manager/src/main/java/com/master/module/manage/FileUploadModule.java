package com.master.module.manage;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.master.bean.dispoly.GmLog;
import com.master.gm.service.manage.impl.FileConfigService;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

/**
 * Created by DJL on 2017/8/1.
 *
 * @ClassName GM
 * @Description
 */
@IocBean
@Ok("json:full")
@At(GmModule.FILE_URL)
public class FileUploadModule {

    @Inject
    private FileConfigService fileConfigService;

    private static long stem=0L;

    @At("/gameFileUp")
    @Ok("jsp:jsp/other/file/upload_file")
    public void gotoFileUp(){}

    @At("/doUpload")
    @Ok("json:full")
    @AdaptBy(type = UploadAdaptor.class,args = {"/data/GM/server/gm_config/temp","8192", "UTF-8", "2"})
    public String upload(@Param("gameConfig")TempFile file,@Param("fType")String fType,@Param("server")String server, HttpSession session){
        long now=System.currentTimeMillis();
        if((now-stem)<1000){
            return JSON.toJSONString("Upload frequency is too high~~");
        }
        stem=now;
        String[] type =fType.split("-");
        String result;
        try {
            UserForService user = (UserForService) session.getAttribute("GM");
            if(user==null){
                throw new GmException("user is null!");
            }
            result = fileConfigService.obtainGameConfig(file.getFile(),type[0],server!=null&&server.length()>0?server.split(","):null);
            GmLog bean =new GmLog();
            bean.setName(user.getName());
            bean.setTime(System.currentTimeMillis());
            bean.setType("file");
            bean.setContext("file:".concat(file.getFile().getName()));
            this.fileConfigService.getDao().insert(bean);
        } catch (GmException e) {
            e.printStackTrace();
            result=JSON.toJSONString(e.getMessage());
        }
        return result;
    }

}
