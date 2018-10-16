package com.master.module.manage;

import javax.servlet.http.HttpServletRequest;

import com.gmdesign.exception.GmException;
import com.gmdesign.rmi.GmOperate;
import com.gmdesign.rmi.impl.GmRmiOperate;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.StringUtil;
import com.master.bean.dispoly.Parameter;
import com.master.bean.dispoly.GmTimer;
import com.master.gm.service.timer.GmTimerService;
import com.master.module.GmModule;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by DJL on 2017/3/2.
 *
 * @ClassName GmTimeConfigerModule
 * @Description
 */
@IocBean
@Ok("json:full")
@At(GmModule.TIMER_URL)
@Fail("http:500")
public class GmTimeConfigerModule{

    @Inject
    private GmTimerService gmTimer;
    @At
    @Ok("jsp:jsp/other/gm/time/taskcontrol")
    public void come(@Param("menuId") int menuId,HttpServletRequest request) {
        request.setAttribute("thisMenuID",menuId);
        request.setAttribute("TaskList",this.gmTimer.getDao().query(GmTimer.class,null));
    }

    @At
    @Ok("jsp:jsp/other/gm/time/retaskdiv")
    public void gotoRetaskDiv(){}

    @At
    @Ok("json:full")
    public void delTimer(@Param("id") int id,@Param("name") String name,@Param("group") String group){
        try {
            GmOperate gm= GmRmiOperate.getGameRmiServer(this.gmTimer.getDao().fetch(Parameter.class,"5").getPrams());
            if(gm==null){
                throw new GmException("本地GM RMI 无法连接!~");
            }
            gm.result("gm_task",10002,name,group);
            this.gmTimer.getDao().delete(GmTimer.class,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("json:full")
    public void controlTimer(@Param("id")int id,@Param("state") int state,@Param("name")String name,@Param("group")String group){
        try {
            GmOperate gm= GmRmiOperate.getGameRmiServer(this.gmTimer.getDao().fetch(Parameter.class,"5").getPrams());
            if(gm==null){
                throw new GmException("本地GM RMI 无法连接!~");
            }
            gm.result("gm_task",10001,name,group,state);
            GmTimer timer=new GmTimer();
            timer.setId(id);
            timer.setStatues((short) state);
            this.gmTimer.getDao().updateIgnoreNull(timer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @At
    @Ok("json:full")
    public void reTimer(@Param("id")int id,@Param("dsp")String dsp,@Param("name")String name,@Param("group")String group,@Param("start")String start,
                        @Param("end")String end,@Param("config")String config){
        try {
            long s=-1;
            long e=-1;
            if(!start.equals("")){
                s= DateUtil.toDateTime(start).getTime();
            }
            if(!end.equals("")){
                e=DateUtil.toDateTime(end).getTime();
            }
            String c= StringUtil.coverTimeConfig(config.split(","));
            GmOperate gm= GmRmiOperate.getGameRmiServer(this.gmTimer.getDao().fetch(Parameter.class,"5").getPrams());
            if(gm==null){
                throw new GmException("本地GM RMI 无法连接!~");
            }
            gm.result("gm_task",10003,dsp,name,group,s,e,c);
            GmTimer timer=new GmTimer();
            timer.setId(id);
            timer.setStartTime(s);
            timer.setEndTime(e);
            timer.setConfig(c);
            this.gmTimer.getDao().updateIgnoreNull(timer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
