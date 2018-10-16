package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.design.util.DateUtil;
import com.service.AccountLoginServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/13.
 */
@Service
public class AccountLoginService implements AccountLoginServiceIF {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int getAccountIsLogin(String msg) {
        Map<String,Object> map = JSONObject.parseObject(msg,Map.class);
        String sql = "select id from login_day_log where uid=? and loginTime>=? and appId=?";
        List<Map<String,Object>> loginDayLogMap =jdbcTemplate.queryForList(sql,map.get("uid"), DateUtil.getToDayMorning(),map.get("appId"));
        if (loginDayLogMap == null || loginDayLogMap.size() == 0){
            return 0;
        }else {
            return 1;
        }

    }
}
