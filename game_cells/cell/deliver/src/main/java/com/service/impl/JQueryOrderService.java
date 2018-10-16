package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.design.util.DateUtil;
import com.design.util.URLTool;
import com.service.JQueryOrderServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/6.
 */
@Service
public class JQueryOrderService implements JQueryOrderServiceIF {

    private static final String QUERY_ALL_SETMEAL="select id,app,goodsId,money,name from price_list";
    private String QUERY_ORDER="select dg.id,dg.serverId,dg.channel,dg.orderUuid,dg.pid,dg.price,dg.sendState,dg.logTime,pl.money,dg.goodsId,dg.orderNumber from " +
            "deliver_goods dg left join price_list pl on dg.app = pl.app and dg.goodsId = pl.goodsId WHERE dg.payType=6";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String,Object>> queryAllSetMeal() {
        List<Map<String, Object>> allSetMeal = jdbcTemplate.queryForList(QUERY_ALL_SETMEAL);
        return allSetMeal;
    }

    @Override
    public List<Map<String, Object>> queryOrder(String msg) {
        Map msgMap = JSONObject.parseObject(msg,Map.class);
        System.out.println("msg转成map:"+msgMap);
        StringBuffer sql = new StringBuffer(QUERY_ORDER);
        List params = new ArrayList();
        if (msgMap.get("sid") != null && !msgMap.get("sid").toString().equals("") ){
            sql.append(" and dg.serverId = ?");
            params.add(msgMap.get("sid").toString());
        }
        if (msgMap.get("channel") != null && !msgMap.get("channel").toString().equals("") ){
            sql.append(" and dg.channel = ?");
            params.add(msgMap.get("channel").toString());
        }
        if (msgMap.get("paySetMeal") != null && !msgMap.get("paySetMeal").toString().equals("") ){
            sql.append(" and pl.id = ?");
            params.add(msgMap.get("paySetMeal").toString());
        }
        if (msgMap.get("status") != null && !msgMap.get("status").toString().equals("") ){
            sql.append(" and dg.sendState = ?");
            params.add(msgMap.get("status").toString());
        }
        if (msgMap.get("startDate") != null && !msgMap.get("startDate").toString().equals("") ){
            long sDate = 0;
            try {
                sDate =DateUtil.getStringToCurrentTimeMillis(msgMap.get("startDate").toString(),"yyyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sql.append(" and dg.logTime >= ?");
            params.add(sDate);
        }
        if (msgMap.get("endDate") != null && !msgMap.get("endDate").toString().equals("") ){
            long eDate = 0;
            try {
                eDate =DateUtil.getStringToCurrentTimeMillis(msgMap.get("endDate").toString(),"yyyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sql.append(" and dg.logTime <= ?");
            params.add(eDate+86399999);
        }
        if (msgMap.get("pid") != null && !msgMap.get("pid").toString().equals("") ){
            sql.append(" and dg.pid = ?");
            params.add(msgMap.get("pid").toString());
        }
        if (msgMap.get("orderNum") != null && !msgMap.get("orderNum").toString().equals("") ){
            sql.append(" and dg.orderUuid = ?");
            params.add(msgMap.get("orderNum").toString());
        }
        List<Map<String,Object>> orders = jdbcTemplate.queryForList(sql.toString(),params.toArray());

        return orders;
    }

}
