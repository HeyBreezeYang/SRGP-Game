package com.master.module.manage;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.master.bean.back.InsideAccountLog;
import com.master.gm.service.manage.InsideAccountServiceIF;
import com.master.gm.service.player.PlayerServiceIF;
import com.master.module.GmModule;
import com.master.util.ManageConf;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/22.
 */
@IocBean
@Ok("json:full")
@At(GmModule.INSIDE_ACCOUNT)
public class InsideAccountController {

    @Inject
    private InsideAccountServiceIF insideAccountService;

    @Inject
    private PlayerServiceIF player;

    private static Log log = Logs.get();

    @At("/insideAccount")
    @Ok("jsp:jsp/other/insideAccount/insideAccount")
    public void gotoInsideAccount(HttpServletRequest request){
        getAllServerAndChannel(request);
    }

    @At("/insideAccountList")
    @Ok("jsp:jsp/other/insideAccount/insideAccountList")
    public void gotoInsideAccountList(HttpServletRequest request){

        getAllServerAndChannel(request);
        List<Map<String,Object>> insideAccounts = insideAccountService.queryInsideAccountList();
        request.getSession().setAttribute("insides",insideAccounts);
    }

    @At("/insideAccountListCondition")
    @Ok("jsp:jsp/other/insideAccount/insideAccountList")
    public void insideAccountListCondition(@Param("sid") String sid,@Param("channel") String channel,@Param("rolename") String rolename,
                                           @Param("pid") String pid,@Param("money") String money,@Param("status") String status,HttpServletRequest request){
        getAllServerAndChannel(request);
        List<Map<String,Object>> insideAccounts = insideAccountService.queryInsideAccountListCondition(sid,channel,rolename,pid,money,status);
        request.getSession().setAttribute("insides",insideAccounts);
    }

    @At("/insideAccountTEList")
    @Ok("jsp:jsp/other/insideAccount/insideAccountTEList")
    public void gotoInsideAccountTEList(HttpServletRequest request){

        getAllServerAndChannel(request);
        List<Map<String,Object>> insideAccounts = insideAccountService.queryInsideAccountList();
        request.getSession().setAttribute("insides",insideAccounts);
    }

    @At("/insideAccountEdit")
    @Ok("jsp:jsp/other/insideAccount/insideAccountEdit")
    public void gotoInsideAccountEdit(@Param("id") Integer id,HttpServletRequest request){
        getAllServerAndChannel(request);
        List<Map<String,Object>> insideAccounts = insideAccountService.queryInsideAccountById(id);
        request.getSession().setAttribute("inside",insideAccounts.get(0));
    }

    @At("/saveOrUpdateInside")
    @Ok("jsp:jsp/other/insideAccount/insideAccountList")
    public String saveOrUpdate(@Param("insideId") String insideId,@Param("sid") String sid, @Param("channel") String channel,@Param("rolename") String rolename,
                             @Param("pid") String pid, @Param("paymoney") String paymoney,@Param("username") String username,
                             @Param("userphone") String userphone, @Param("ascription") String ascription,@Param("applyuser") String applyuser,
                             @Param("applyreason") String applyreason, @Param("remark") String remark ,HttpServletRequest request){
        UserForService user = (UserForService)request.getSession().getAttribute("GM");
        log.info("rolename:"+rolename+"==pid:"+pid);
        if (insideId == null || insideId.equals("")){
            log.info("用户:"+user.getName()+" 新增内部号，角色名:"+rolename);
        }else {
            log.info("用户:"+user.getName()+" 修改内部号，角色名:"+rolename);
        }
        insideAccountService.saveOrUpdate(insideId,sid,channel,rolename,pid,paymoney,username,userphone,ascription,applyuser,applyreason,remark);
        if (insideId == null || insideId.equals("")){
            return "jsp/other/insideAccount/insideAccountList";
        }else {
            return "jsp/other/insideAccount/insideAccountTEList";
        }
    }

    @At("/delInsideAccount")
    @Ok("json:full")
    public String delInsideAccount(@Param("id") String id,HttpServletRequest request){
        try {
            insideAccountService.delInsideAccount(id);
            return "删除成功";
        }catch (GmException g){
            return g.getMessage();
        }
    }

    @At("/insideAccountTE")
    @Ok("jsp:jsp/other/insideAccount/insideAccountList")
    public void insideAccountTE(@Param("id") String id,@Param("type")String type, HttpServletRequest request){
        UserForService user = (UserForService)request.getSession().getAttribute("GM");
        String typeTE = "通过";
        if (1 != Integer.parseInt(type)){
            typeTE = "不通过";
        }
        log.info("用户："+user.getName()+"审核id为:"+id+"的内部号=>"+typeTE);
        insideAccountService.insideAccountTE(id,type,user.getName());
    }

    @At("/grantResource")
    @Ok("json:full")
    public String grantResource(@Param("paymoney") String paymoney,@Param("rolename")String rolename, HttpServletRequest request){
        UserForService user = (UserForService)request.getSession().getAttribute("GM");
        log.info("用户:"+user.getName()+" 给内部号"+rolename+"发放："+paymoney+"的资源");
        try {
            GmHashMap gmHashMap = player.queryPlayerMsg(ManageConf.SERVERID,rolename,1);
//            System.out.println("gmHashMap:"+gmHashMap);
            if(gmHashMap == null || gmHashMap.size()<=0){
                return "账号不存在";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       try{
           insideAccountService.grantResource(paymoney,rolename,user);
           return "发放成功";
       }catch (GmException g){
           g.printStackTrace();
           return g.getMessage();
       }
    }

    @At("/insideAccountLog")
    @Ok("jsp:jsp/other/insideAccount/insideAccountLog")
    public void gotoInsideAccountLog(@Param("startDate") String startDate,@Param("endDate") String endDate,HttpServletRequest request){
        List<InsideAccountLog> insodeLogs = null;
        try {
            insodeLogs = insideAccountService.getInsideAccountLog(startDate,endDate);
        } catch (ParseException e) {
            log.info("时间转换异常");
        }
        request.getSession().setAttribute("insodeLogs",insodeLogs);
    }

    public void getAllServerAndChannel(HttpServletRequest request){

        List<Record> allServer = insideAccountService.getAllServer();

        List<Record> allChannel = insideAccountService.getAllChannel();

        request.getSession().setAttribute("allServer",allServer);
        request.getSession().setAttribute("allChannel",allChannel);

    }
}
