package com.service;

import com.gmdesign.exception.GmException;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/4.
 */
public interface NoticeServiceIF {
    List<Map<String, Object>> queryNotice(String uid,String appId,String noticeType, Integer sid) throws GmException;

    Map<String, Object> getNotcieImage();
}
