package com.master.module.manage;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.master.bean.dispoly.GmLog;
import com.master.gm.bean.UserQuestion;
import com.master.gm.service.manage.impl.CustomerService;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/7/27.
 *
 * @ClassName CustomerModule
 * @Description 客服回复
 */
@IocBean
@Ok("json:full")
@At(GmModule.CUSTOMER_URL)
public class CustomerModule {
    private static Log log = Logs.get();
    @Inject
    private CustomerService customerService;

    @At
    @Ok("jsp:jsp/other/customer/question")
    public void questionReplyForTrue(HttpServletRequest request,@Param("menuId") int menuId){
        request.setAttribute("thisMenuID",menuId);
        request.setAttribute("thisMenuUrl","customer/questionReplyForTrue");
        List<UserQuestion> list=new ArrayList<>();
        try {
            List<UserQuestion> all=customerService.queryAllQuestion();
            for(UserQuestion uq :all){
                if(uq.getType()!=0){
                    list.add(uq);
                }
            }
            request.setAttribute("questionList",list);
        } catch (GmException e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("jsp:jsp/other/customer/question")
    public void questionReplyForFalse(HttpServletRequest request,@Param("menuId") int menuId){
        request.setAttribute("thisMenuID",menuId);
        request.setAttribute("thisMenuUrl","customer/questionReplyForFalse");
        List<UserQuestion> list=new ArrayList<>();
        try {
            List<UserQuestion> all=customerService.queryAllQuestion();
            for(UserQuestion uq :all){
                if(uq.getType()==0){
                    list.add(uq);
                }
            }
            request.setAttribute("questionList",list);
        } catch (GmException e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("json:full")
    public void subQA(HttpSession session,@Param("serverId") String serverId, @Param("pid") String pid, @Param("time") long time, @Param("type") int type, @Param("context") String context){
        try {
            UserForService user = (UserForService) session.getAttribute("GM");
            if(user==null){
                throw new GmException("user is null!");
            }
            customerService.saveAnswer(serverId,pid,time,type,context);
            GmLog bean =new GmLog();
            bean.setName(user.getName());
            bean.setTime(System.currentTimeMillis());
            bean.setType("customer");
            bean.setContext("customer:".concat(context).concat("for ").concat(pid));
            this.customerService.getDao().insert(bean);
        } catch (GmException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    @At("/goAnswer")
    @Ok("jsp:jsp/other/customer/answer_window")
    public void comeAnswer(HttpServletRequest request, @Param("sid") String sid,@Param("pid") String pid,@Param("time") long time){
        request.setAttribute("sid",sid);
        request.setAttribute("pid",pid);
        request.setAttribute("time",time);
        try {
            request.setAttribute("question",customerService.getUserQuestion(sid,pid,time));
        } catch (GmException e) {
            e.printStackTrace();
        }
    }

}
