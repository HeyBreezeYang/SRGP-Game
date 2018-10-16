package com.master.gm.service.manage.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cellsgame.common.buffer.Helper;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.SendUtil;
import com.master.bean.back.NoticeSet;
import com.master.bean.dispoly.Notice;
import com.master.bean.dispoly.Parameter;
import com.master.gm.BaseService;
import com.master.gm.service.manage.NoticeServiceIF;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/7/27.
 *
 * @ClassName GM
 * @Description
 */
@IocBean
public class NoticeService extends BaseService implements NoticeServiceIF {

    @Override
    public Map sendSystemNotice(String sid, String msg) throws GmException {
        String url=this.dao.fetch(Parameter.class,sid).getPrams().concat("/notify");
        try {
            return Helper.mapFromBytes(SendUtil.sendHttpMsgResponseByte(url,"msg=".concat(msg)));
        } catch (IOException e) {
            throw new GmException(e.getMessage());
        }
    }

    @Override
    public List<Record> getAllServer() {
        return this.dao.query("gm_config.game_server",null);
    }

    @Override
    public List<Record> getAllChannel() {

        return this.dao.query("gm_config.channel",null);
    }

    public void saveOrUpdateNotice(String noticeId,String jsonmap, String sid, String channel,String noticeType) {
        if (noticeId != null && !noticeId.equals("") && Integer.parseInt(noticeId)>0){
            Notice notice = new Notice();
            notice.setId(Integer.parseInt(noticeId));
            notice.setChannel(sid);
            notice.setType(noticeType);
            notice.setMsg(jsonmap);
            this.dao.update(notice);
        }else {

            Notice notice = new Notice();
            notice.setChannel(sid);
            notice.setType(noticeType);
            notice.setMsg(jsonmap);
            this.dao.insert(notice);
        }

    }

    public List<Record> queryNoticeList() {

        return this.dao.query("gm_config.notice", null);
    }

    @Override
    public List<Notice> queryNoticeById(String noticeId) {
        return this.dao.query(Notice.class, Cnd.where("id","=",noticeId));

    }

    public String delNotice(String id) throws GmException {
        Notice notice = new Notice();
        notice.setId(Integer.parseInt(id));
        int delete = this.dao.delete(notice);
        if (delete <= 0){
            throw new GmException("删除的数据不存在");
        }

        return "删除成功";
    }

    @Override
    public void saveOrUpdateBroadcastNotice(String noticeId, String jsonmap, String sid, String channel, String noticeType, String startTime, String endTime, Integer intervalTime) {

    }

    @Override
    public List<NoticeSet> getAllNoticeSet() {
        return this.dao.query(NoticeSet.class,Cnd.where("setting","=","公告弹出限制"));
    }

    @Override
    public int updateNoticeSet(Integer noticeSetId, Integer noticeSetValue) throws GmException {
        int i = 0;
        List<NoticeSet> noticeSets = this.dao.query(NoticeSet.class,Cnd.where("id","=",noticeSetId));
        if (noticeSets != null && noticeSets.size()>0){
            for (NoticeSet noticeSet:noticeSets) {
                if (noticeSetValue == 0){
                    noticeSet.setValue("1");
                }else {
                    noticeSet.setValue("0");
                }
                i=this.dao.update(noticeSet);
                if (i==0){
                    throw new GmException("设置修改了0条记录");
                }
            }
        }else {
            throw new GmException("设置不存在");
        }
        return i;
    }

    @Override
    public void updateNoticeImage(String imgeId) throws GmException {
        int i = 0;
        List<NoticeSet> noticeSets = this.dao.query(NoticeSet.class,Cnd.where("setting","=","noticeImage"));
        System.out.println("公告设置:"+noticeSets);
        for (NoticeSet noticeSet:noticeSets) {
            noticeSet.setValue(imgeId);
            i=this.dao.update(noticeSet);
            if (i==0){
                throw new GmException("公告图片更新，修改了0条记录");
            }
        }

    }
}
