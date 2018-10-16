package com.webJava;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.service.NoticeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/4.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/notice",method = RequestMethod.POST)
public class NoticeController {

    @Autowired
    private NoticeServiceIF notice;

    @RequestMapping(value = "/getNotice.api")
    public String queryNotice(@Param("uid") String uid,@Param("appId")String appId,@Param("noticeType") String noticeType, @Param("sid") Integer sid){
        Map<String,Object> result = new GmHashMap();
        Map<String, Object> noticeImage = null;
        try {
            List<Map<String, Object>> notices = notice.queryNotice(uid,appId,noticeType,sid);
            noticeImage = notice.getNotcieImage();
            if (notices != null && notices.size()>0){
                result.put("co",0);
                result.put("image",noticeImage.get("value"));
                result.put("msg",notices);
            }else {
                result.put("co",0);
                result.put("image",noticeImage.get("value"));
                result.put("msg","");
            }
        }catch (GmException e){
            result.put("co",1);
            result.put("image",noticeImage.get("value"));
            result.put("msg","");
            e.printStackTrace();
            e.getMessage();
        }
        return JSON.toJSONString(result);
    }


}
