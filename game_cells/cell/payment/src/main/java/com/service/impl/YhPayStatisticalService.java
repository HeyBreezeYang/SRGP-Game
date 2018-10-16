package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.design.util.DateUtil;
import com.service.YhPayStatisticalServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/23.
 */
@Service
public class YhPayStatisticalService implements YhPayStatisticalServiceIF {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String QUERY_DATE_PAY="select id,pid,price from platform_deliver.deliver_goods where payType=6 and serverId=? and channel=? and ?<=logTime<=? ";

    @Override
    public List queryPlayerDatePay(String msg) throws ParseException {
        Map msgMap = JSONObject.parseObject(msg,Map.class);
        String sid = msgMap.get("sid").toString();
        String channel = msgMap.get("channel").toString();
        String pid = msgMap.get("pids").toString();
        String startDate = msgMap.get("startDate").toString();
        String endDate = msgMap.get("endDate").toString();
        String[] pids = pid.split(",");
        List<String> everyDay =DateUtil.getEveryDay(startDate,endDate);
        List ret = new ArrayList();
        for (String toDay:everyDay) {
            long startCTM = DateUtil.getStringToCurrentTimeMillis(toDay,"yyyy-MM-dd");
            long endCTM = startCTM+86399999;
            List<Map<String,Object>> orderList = jdbcTemplate.queryForList(QUERY_DATE_PAY,sid,channel,startCTM,endCTM);
//            List list = new ArrayList();
            int countMoney=0;
            for (int i=0; i<pids.length;i++) {
                for (Map mapOrder:orderList) {
                    if (mapOrder.get("pid").toString().equals(pids[i])){
                        countMoney+=Integer.parseInt(mapOrder.get("price").toString());
                    }
                }
//                list.add(countMoney);
            }
            ret.add(countMoney);
        }
        System.out.println("返回的结果:"+ret);
        return ret;
    }
}
