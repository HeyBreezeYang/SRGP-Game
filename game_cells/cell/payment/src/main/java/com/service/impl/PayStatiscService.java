package com.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.design.util.DateUtil;
import com.service.PayStatiscServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/15.
 */
@Service
public class PayStatiscService implements PayStatiscServiceIF {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String,Object> getToDayPayNum(String msg) {

        Map<String,Object> msgMap = JSON.parseObject(msg);
        Map<String,Object> map = new HashMap<>();
        String sql="SELECT id,price FROM platform_deliver.deliver_goods WHERE serverId=? and ?<logTime and logTime <?";
        System.out.println(msgMap);
        Long startDate = 0l;
        Long endDate = 0l;
        try {
            startDate=DateUtil.getStringToCurrentTimeMillis(msgMap.get("startDate").toString(),"yyyy-MM-dd");
            endDate=DateUtil.getStringToCurrentTimeMillis(msgMap.get("endDate").toString(),"yyyy-MM-dd")+86399999;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Map<String,Object>> deliverGoods = jdbcTemplate.queryForList(sql,msgMap.get("serverId"),startDate,endDate);
        map.put("num",deliverGoods.size());
        int countPrice = 0;
        for (Map goodsMap: deliverGoods ) {
            countPrice += Integer.parseInt(goodsMap.get("price").toString());
        }
        map.put("price",countPrice);
        return map;
    }

}
