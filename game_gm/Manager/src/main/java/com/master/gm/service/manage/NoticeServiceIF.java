package com.master.gm.service.manage;

import java.util.List;
import java.util.Map;

import com.gmdesign.exception.GmException;
import com.master.bean.back.NoticeSet;
import com.master.bean.dispoly.Notice;
import org.nutz.dao.entity.Record;

/**
 * Created by DJL on 2017/8/1.
 *
 * @ClassName NoticeServiceIF
 * @Description
 */
public interface NoticeServiceIF {
    Map sendSystemNotice(String sid, String msg)throws GmException;

    List<Record> getAllServer();

    List<Record> getAllChannel();

    void saveOrUpdateNotice(String noticeId,String jsonmap, String sid, String channel,String noticeType);

    List<Record> queryNoticeList();

    List<Notice> queryNoticeById(String noticeId);

    String delNotice(String id) throws GmException;

    void saveOrUpdateBroadcastNotice(String noticeId, String jsonmap, String sid, String channel, String noticeType, String startTime, String endTime, Integer intervalTime);

    List<NoticeSet> getAllNoticeSet();

    int updateNoticeSet(Integer noticeSetId, Integer noticeSetValue) throws GmException;

    void updateNoticeImage(String imageId) throws GmException;
}
