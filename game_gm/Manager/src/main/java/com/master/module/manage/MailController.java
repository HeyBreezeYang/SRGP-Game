package com.master.module.manage;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.master.bean.dispoly.GmMail;
import com.master.gm.service.manage.MailServiceIF;
import com.master.module.GmModule;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/4.
 */
@IocBean
@Ok("json:full")
@At(GmModule.MAIL_URL)
public class MailController {

    @Inject
    private MailServiceIF mailService;

    private static Log log = Logs.get();

    @At("/addMail")
    @Ok("jsp:jsp/other/mail/addMail")
    public void gotoAddMail(HttpServletRequest request) {
        getServerAndChannel(null,request);
    }

    @At("/updateMail")
    @Ok("jsp:jsp/other/mail/editMail")
    public void gotoUpdateMail(@Param("mailId") String mailId,HttpServletRequest request) {
        getServerAndChannel(mailId,request);

    }
    @At("/mailList")
    @Ok("jsp:jsp/other/mail/mailList")
    public void gotoMailList(HttpServletRequest request) {
       try {
           List<Record> mails = mailService.getAllMail(null,null,null);
           request.getSession().setAttribute("mails",mails);
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    @At("/toExamineMail")
    @Ok("jsp:jsp/other/mail/toExamineMail")
    public void gotoToExamineMail(@Param("mailId") String mailId,HttpServletRequest request) {
        getServerAndChannel(mailId,request);
    }

    @At("/toExamineMailList")
    @Ok("jsp:jsp/other/mail/toExamineMailList")
    public void gotoToExamineMailList(HttpServletRequest request) {

        try {
            List<Record> mails = mailService.getAllMail(null,null,null);
            request.getSession().setAttribute("mails",mails);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @At("/saveOrUpdateMail")
    @Ok("jsp:jsp/other/mail/mailList")
    public void saveOrUpdateMail(@Param("mailId") String mailId,@Param("sid") String sid,@Param("channel") String channel,@Param("mailTitle") String mailTitle,
                                 @Param("mailContext") String mailContext,@Param("username") String username,@Param("isAllUsername") String isAllUsername,
                                 @Param("enclosure") String enclosure,@Param("createBy") String createBy,@Param("createTime") String createTime,
                                 @Param("mailStatus") String mailStatus,@Param("operation") String operation,HttpServletRequest request){
        UserForService user = (UserForService) request.getSession().getAttribute("GM");
        Map<String,Object> map = new GmHashMap();
        map.put("title",mailTitle);
        map.put("context",mailContext);
        if (enclosure != null && !enclosure.equals("")){
            map.put("funscJson",enclosure);
        }
        String mailMsg = JSONUtils.toJSONString(map);
        GmMail mail = new GmMail();
        if (mailId != null && !mailId.equals("")){
            mail.setId(Integer.parseInt(mailId));
            if (operation.equals("edit")){
                log.info("用户:"+user.getName()+"修改id为:"+mailId+"的邮件");
            }
            if (operation.equals("toExamine")){
                log.info("用户:"+user.getName()+"修改并通过了id为:"+mailId+"的邮件");
            }
        }else {
            log.info("用户:"+user.getName()+"新增邮件:"+mailTitle);
        }
        try {
            if (sid != null && !sid.equals("")){
                mail.setSid(sid);
            }else {
                throw new GmException("服务器id不能为空");
            }
            if (channel != null && !channel.equals("")){
                mail.setChannel(channel);
            }else {
                throw  new GmException("渠道id不能为空");
            }
            if (isAllUsername == null || isAllUsername.equals("")){
                if (username != null && !username.equals("")){
                    mail.setUsername(username);
                }else {
                    throw new GmException("需要发送的玩家不能为空");
                }
            }else {
                mail.setUsername("all");
            }
            if (createBy != null && !createBy.equals("")){
                mail.setCreateBy(createBy);
            }
            if (createTime != null && !createTime.equals("")){
                mail.setCreateTime(Long.parseLong(createTime));
            }
            if (mailStatus != null && !mailStatus.equals("")){
                mail.setStatus(Integer.parseInt(mailStatus));
            }
            if (!operation.equals("edit") && !operation.equals("toExamine")){
                throw  new GmException("操作状态不能为空");
            }
            mailService.saveOrUpdateMail(mailMsg,mail,user,operation);
        }catch (GmException g){
            g.printStackTrace();
            log.error(g.getMessage());
        }


    }

    @At("/delMail")
    @Ok("json:full")
    public String delMail(@Param("mailId") String mailId,HttpServletRequest request){
        UserForService user = (UserForService) request.getSession().getAttribute("GM");
        if (user == null){
            return "登录失效";
        }

        try {
            String s = mailService.delMail(mailId);
            log.info("用户:"+user.getName()+"删除了id为："+mailId+"的邮件");
            return s;
        }catch (GmException g){
            return g.getMessage();
        }
    }

    @At("/mailConditionList")
    @Ok("jsp:jsp/other/mail/mailList")
    public void mailConditionList(@Param("mailStatus") String mailStatus,@Param("startDate") String startDate,@Param("endDate") String endDate,HttpServletRequest request){
        try {
            List<Record> mails =  mailService.getAllMail(mailStatus,startDate,endDate);
            request.getSession().setAttribute("mails",mails);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @At("/toExamineMailStatus")
    @Ok("json:full")
    //@Ok("jsp:jsp/other/mail/toExamineMailList")
    public String toExamineMailStatus(@Param(("mailId")) Integer mailId,@Param("type") Integer type,HttpServletRequest request){
        UserForService user = (UserForService) request.getSession().getAttribute("GM");
        if (type == 2){
            log.info("用户:"+user.getName()+"通过了id为："+mailId+"的邮件");
        }
        if (type == 3 ){
            log.info("用户:"+user.getName()+"驳回了id为："+mailId+"的邮件");
        }
        try {
            mailService.toExamineMailStatus(mailId,type,user);
            if (type==2){
                return "审核通过完成";
            }
            if (type==3){
                return "审核驳回完成";
            }
        } catch (GmException e) {
            e.printStackTrace();
            e.getMessage();
            if (type==2){
                return "审核通过失败";
            }
            if (type==3){
                return "审核驳回失败";
            }
        }
        return "异常";
    }

    public void getServerAndChannel(String mailId,HttpServletRequest request){
        if (mailId != null && !mailId.equals("")){
            List<Record> mails = mailService.getMail(mailId);
            request.getSession().setAttribute("mail",mails.get(0));
        }
        /*else {
            request.getSession().setAttribute("mail",null);
        }*/
        List<Record> allServer = mailService.getAllServer();
        List<Record> allChannel = mailService.getAllChannel();
        request.getSession().setAttribute("allServer",allServer);
        request.getSession().setAttribute("allChannel",allChannel);
    }
}
