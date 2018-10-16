package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.config.GmConfig;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.SendUtil;
import com.gmdesign.util.URLTool;
import com.service.NoticeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/4.
 */
@Service
public class NoticeService implements NoticeServiceIF {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> queryNotice(String uid,String appId,String noticeType, Integer sid) throws GmException {


      if (!noticeType.equals("系统公告")){
          String getNoticeSetSql = "select id,setting,value from gm_config.notice_set where setting='公告弹出限制'";
          Map<String,Object> noticeSet = jdbcTemplate.queryForMap(getNoticeSetSql);
          if (Integer.parseInt(noticeSet.get("value").toString()) == 1 ){
              int i = accountIsLogin(uid,appId);
              if (i == 0){
                  String sql = "SELECT id,msg FROM gm_config.notice WHERE type=? and channel like '%"+sid+"%'  or channel like '%all%'  ORDER BY msg ASC";
                  List<Map<String, Object>> notices = jdbcTemplate.queryForList(sql,noticeType);
                  return notices;
              }
              return null;
          }else {
              String sql = "SELECT id,msg FROM gm_config.notice WHERE type=? and channel like '%"+sid+"%'  or channel like '%all%'  ORDER BY msg ASC";
              List<Map<String, Object>> notices = jdbcTemplate.queryForList(sql,noticeType);
              return notices;
          }
      }else {
          String sql = "SELECT id,msg FROM gm_config.notice WHERE type=? and channel like '%"+sid+"%' OR channel like '%all%'  ORDER BY msg ASC";
          List<Map<String, Object>> notices = jdbcTemplate.queryForList(sql,noticeType);
          return notices;
      }

    }

    @Override
    public Map<String, Object> getNotcieImage() {
        String getNoticeImageSql = "select id,setting,value from gm_config.notice_set where setting='noticeImage'";
        Map<String,Object> noticeImage = jdbcTemplate.queryForMap(getNoticeImageSql);
        return noticeImage;
    }

    public int accountIsLogin(String uid,String appId) throws GmException {

        Map<String,Object> param = new GmHashMap();
        param.put("uid",uid);
        param.put("appId",appId);
        String ret = SendUtil.sendHttpMsg(GmConfig.IS_LOGIN_URL.concat(URLTool.Encode(JSONObject.toJSONString(param))),null);
        if (ret != null && !ret.equals("")){
            Map<String,Object> retMap = JSONObject.parseObject(ret,Map.class);
            if (Integer.parseInt(retMap.get("code").toString()) == 0){
                return 0;
            }else {
                return 1;
            }
        }else {
            throw new GmException("返回的ret为空");
        }
    }
}
