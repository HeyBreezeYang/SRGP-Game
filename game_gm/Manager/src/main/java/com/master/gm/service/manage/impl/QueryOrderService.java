package com.master.gm.service.manage.impl;

import com.alibaba.fastjson.JSONObject;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.URLTool;
import com.master.bean.dispoly.Channel;
import com.master.bean.dispoly.GameServer;
import com.master.gm.BaseService;
import com.master.gm.service.manage.QueryOrderServiceIF;
import com.master.util.ManageConf;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/6.
 */
@IocBean
public class QueryOrderService extends BaseService implements QueryOrderServiceIF {
    @Override
    public Map queryQuerySelect() {
        Map map = new HashMap();
        List<GameServer> allServer = this.dao.query(GameServer.class,null);
        map.put("allServer",allServer);
        List<Channel> allChannel = this.dao.query(Channel.class,null);
        map.put("allChannel",allChannel);

        try {
            List allSetMeal = JSONObject.parseObject(URLTool.sendMsg(ManageConf.SERVERID+":9895/queryOrder/queryAllSetMeal",null),List.class);
            map.put("allSetMeal",allSetMeal);

        } catch (GmException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public List<Map> queryOrder(String sid, String channel, String paySetMeal, String status, String startDate, String endDate, String pid, String orderNum) {
        Map param = new HashMap();
        param.put("sid",sid);
        param.put("channel",channel);
        param.put("paySetMeal",paySetMeal);
        param.put("status",status);
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        param.put("pid",pid);
        param.put("orderNum",orderNum);
        String paramStr = "msg="+JSONObject.toJSONString(param);
        List<Map> orders = new ArrayList<>();
        try {
            orders = JSONObject.parseObject(URLTool.sendMsg(ManageConf.SERVERID+":9895/queryOrder/queryOrder",paramStr),List.class);
        } catch (GmException e) {
            e.printStackTrace();
        }

        String u=this.dao.query(GameServer.class, Cnd.where("serverID","=",sid)).get(0).getServerUrl().concat("/loadPlayer?playerId=");
        for (Map map:orders) {
            try {
                String res = URLTool.sendMsg(u.concat(map.get("pid").toString()),null);
                Map resmap = JSONObject.parseObject(res,Map.class);
                Map retMap = JSONObject.parseObject(resmap.get("ret").toString(),Map.class);
                Map pInfoMap = JSONObject.parseObject(retMap.get("pInfo").toString(),Map.class);
                map.put("pname",pInfoMap.get("nm"));
            } catch (GmException e) {
                e.printStackTrace();
            }

            if (Integer.parseInt(map.get("sendState").toString()) == 0){
                map.put("status","未发货");
            }if (Integer.parseInt(map.get("sendState").toString()) == 1){
                map.put("status","已发货");
            }if (Integer.parseInt(map.get("sendState").toString()) != 0 && Integer.parseInt(map.get("sendState").toString()) != 1){
                map.put("status",map.get("sendState").toString());
            }

            map.put("date", DateUtil.getCurrentTimeMillisToString(Long.parseLong(map.get("logTime").toString()),"yyyy-MM-dd"));


        }
        System.out.println(orders);
        return orders;
    }
}
