package com.master.module.manage;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.master.bean.back.NoticeSet;
import com.master.bean.dispoly.Notice;
import com.master.gm.service.manage.NoticeServiceIF;
import com.master.gm.service.manage.impl.NoticeService;
import com.master.module.GmModule;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DJL on 2017/7/27.
 *
 * @ClassName NoticeModule
 * @Description 公告配置模块
 */
@IocBean
//@Ok("json:full")
@At(GmModule.NOTICE_URL)
public class NoticeModule {

    @Inject
    private NoticeServiceIF noticeService;


    @At("/gameNotice")
    @Ok("jsp:jsp/other/notice/gameNotice")
    public void gotoaGameNotice(HttpServletRequest request) {

        getServerAndChannel(request,null);

    }

    @At("/systemNotice")
    @Ok("jsp:jsp/other/notice/systemNotice")
    public void gotoSystemNotice(HttpServletRequest request) {

        getServerAndChannel(request,null);
    }

    @At("/noticeList")
    @Ok("jsp:jsp/other/notice/noticeList")
    public void gotoNoticeList(HttpServletRequest request) {
        List<Record> notices = noticeService.queryNoticeList();
        if (notices != null && notices.size()>0){
            for (Record notice :notices) {
                Map map = JSONObject.parseObject(notice.get("msg").toString(),Map.class);
                notice.put("title",map.get("title"));
            }
        }
        request.getSession().setAttribute("notices", notices);
    }

    @At("/updateGameNoticePage")
    @Ok("jsp:jsp/other/notice/gameNotice")
    public void gotoUpdateGameNotice(@Param("noticeId")String noticeId,HttpServletRequest request){
        getServerAndChannel(request,noticeId);
    }

    @At("/updateSystemNoticePage")
    @Ok("jsp:jsp/other/notice/systemNotice")
    public void gotoUpdateSystemNotice(@Param("noticeId")String noticeId, HttpServletRequest request){

        getServerAndChannel(request,noticeId);

    }

    @At("/noticeSet")
    @Ok("jsp:jsp/other/notice/noticeSet")
    public void gotoNoticeSet(HttpServletRequest request){
        List<NoticeSet> noticeSets = noticeService.getAllNoticeSet();
        request.getSession().setAttribute("noticeSets",noticeSets);
    }

    @At("/saveOrUpdateNotice")
    @Ok("jsp:jsp/other/notice/noticeList")
    public void saveOrUpdateNotice(@Param("noticeId")String noticeId, @Param("noticeTitle")String noticeTitle, @Param("noticeContext") String noticeContext, @Param("isAllServer") String isAllServer, @Param("sid") String sid, @Param("isAllChannel") String isAllChannel, @Param("channel") String channel, @Param("level") String level, @Param("noticeType") String noticeType,@Param("highlightTitle") String highlightTitle, HttpServletRequest request) {
        System.out.println("编辑公告");
        Map<String, Object> map = new GmHashMap();
        map.put("context", noticeContext);
        map.put("title", noticeTitle);
        map.put("level", Integer.parseInt(level));
        map.put("highlight",highlightTitle);
        String jsonmap = JSONUtils.toJSONString(map);

        if (isAllServer != null) {
            if (isAllServer.equals("all")) {
//                List<Record> allServer = noticeService.getAllServer();
                sid = "all";

                /*for (Record r:allServer) {
                    sid += r.get("serverid").toString()+",";
                }*/
            }
        }
        if (isAllChannel != null) {
            if (isAllChannel.equals("all")) {
//                List<Record> allChannel = noticeService.getAllChannel();
                channel = "all";
                /*for (Record r:allChannel) {
                    channel += r.get("id").toString()+",";
                }*/
            }
        }
        try{
            noticeService.saveOrUpdateNotice(noticeId, jsonmap, sid, channel,noticeType);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @At("/delGameNotice")
    @Ok("json:full")
    public String delGameNotice(@Param("noticeId") String noticeId){
        try {
            String s = noticeService.delNotice(noticeId);
            return s;
        }catch (GmException g){
            return g.getMessage();
        }

    }

    @At("/updateNoticeSet")
    @Ok("json:full")
    public String updateNoticeSet(@Param("noticeSetId") Integer noticeSetId,@Param("noticeSetValue")Integer noticeSetValue){

        try {
            noticeService.updateNoticeSet(noticeSetId,noticeSetValue);
        } catch (GmException e) {
            e.printStackTrace();
            e.getMessage();
            return "修改失败--"+e.getMessage();
        }
        return "修改成功";
    }

    @At("/updateNoticeImage")
    @Ok("json:full")
    public String updateNoticeImage(@Param("imageId") String imageId,HttpServletRequest request){

        try {
            noticeService.updateNoticeImage(imageId);
            return "更新成功";
        } catch (GmException e) {
            e.printStackTrace();
            return "更新失败";
        }

    }
    public void getServerAndChannel(HttpServletRequest request,String noticeId) {

        List<Record> allServer = noticeService.getAllServer();
        List<Record> allChannel = noticeService.getAllChannel();

        request.getSession().setAttribute("allServer", allServer);
        request.getSession().setAttribute("allChannel", allChannel);


        Map map = new HashMap();
        if (noticeId != null && !noticeId.equals("")){
           List<Notice> notices = noticeService.queryNoticeById(noticeId);

           for (Notice notice : notices) {
               map.put("sid",notice.getChannel());
               map.put("id", notice.getId());
               map.put("type", notice.getType());
               Map msgMap = JSONObject.parseObject(notice.getMsg(), Map.class);
               map.put("level", msgMap.get("level"));
               map.put("title", msgMap.get("title"));
               map.put("context", msgMap.get("context"));
               map.put("highlightTitle",msgMap.get("highlight"));
           }

       }
        request.getSession().setAttribute("notice",map);
    }


}

