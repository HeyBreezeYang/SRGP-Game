package com.master.module.manage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.master.bean.dispoly.CodeConfig;
import com.master.bean.dispoly.GmLog;
import com.master.bean.dispoly.Parameter;
import com.master.gm.service.manage.impl.CodeService;
import com.master.module.GmModule;
import com.master.util.DownLoadUtil;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/12/11.
 *
 * @ClassName GM
 * @Description
 */
@IocBean
@Ok("json:full")
@At(GmModule.CODE_URL)
public class ExchangeCodeModule {

    @Inject
    private CodeService codeService;

    @At("/editCode")
    @Ok("jsp:jsp/other/code/build_code")
    public void comeBuildCode(HttpServletRequest request){
        request.setAttribute("bdCode",codeService.queryBinding());
    }


    @At("/queryCode")
    @Ok("jsp:jsp/other/code/operate_code")
    public void comeGetCode(HttpServletRequest request,@Param("menuId") int menuId){
        request.setAttribute("thisMenuID",menuId);
        request.setAttribute("AttrList",codeService.queryAll());
    }

    @At
    @Ok("json:full")
    public void delCode(HttpSession session,@Param("id") int aid){
        UserForService user= (UserForService) session.getAttribute("GM");
        try {
        if(user==null){
            throw new GmException("user is null!");
        }
        GmLog log =new GmLog();
        log.setName(user.getName());
        log.setTime(System.currentTimeMillis());
        log.setType("code");
        log.setContext("删除兑换码："+aid);
        this.codeService.getDao().insert(log);
        codeService.deleteCode(aid);
        updateAttr();
        } catch (GmException | IOException e) {
            e.printStackTrace();
        }
    }


    @At
    @Ok("void")
    public void addCode(HttpServletResponse response,@Param("..") CodeConfig bean,HttpSession session,@Param("bindingId") String bindingId,@Param("isAllServer") String isAllServer,@Param("isAllChannel") String isAllChannel){

        try {
            UserForService user= (UserForService) session.getAttribute("GM");
            if(user==null){
                throw new GmException("user is null!");
            }
            if(isAllServer!=null&&isAllServer.length()>1){
                bean.setSid("all");
            }
            if(isAllChannel!=null&&isAllChannel.length()>1){
                bean.setChannel("all");
            }
            File codeFile=codeService.addPrizeCode(bean);
            if(bean.getSpecialType()==2){
                codeService.addBindCode(bean.getId(),bindingId);
            }
            updateAttr();
            GmLog log =new GmLog();
            log.setName(user.getName());
            log.setTime(System.currentTimeMillis());
            log.setType("code");
            log.setContext("配置兑换码奖励："+bean.getDsp());
            this.codeService.getDao().insert(log);
            DownLoadUtil.downFile(response,bean.getDsp().concat(".txt"),codeFile);
        } catch (IOException | GmException e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("void")
    public void exportCode(HttpServletResponse response,@Param("id") int aid){
        Map codeInfo = codeService.exportCode(aid);
        File word = null;
        try {
            word =File.createTempFile("export","txt");
            FileWriter writer=new FileWriter(word);
            List<String> codes = (List<String>) codeInfo.get("code");
            for (String code:codes) {
                writer.write(code+"\n");
                writer.flush();
            }
            DownLoadUtil.downFile(response,codeInfo.get("fileName").toString().concat(".txt"),word);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void updateAttr() throws IOException {
        URL url = new URL(this.codeService.getDao().fetch(Parameter.class,"1").getPrams().concat("/code/update"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(15000);
        conn.getResponseCode();
        conn.disconnect();
    }

}
