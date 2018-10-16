package com.master.gm.service.manage.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.*;
import com.master.bean.dispoly.GameServer;
import com.master.gm.BaseService;
import com.master.gm.service.manage.RealTimeMonitoringServiceIF;
import com.master.util.ManageConf;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.*;

/**
 * Created by HP on 2018/6/12.
 */
@IocBean
public class RealTimeMonitoringService extends BaseService implements RealTimeMonitoringServiceIF {
    @Override
    public List<Record> getAllServer() {
        return this.dao.query("gm_config.game_server",null);
    }

    @Override
    public GmHashMap queryRealTimeMonitoring(String sid, String startDate, String endDate) throws GmException {

        List<GameServer> gameServers = null;
        if (sid.equals("all")){
            gameServers = this.dao.query(GameServer.class, null);
        }else {
            gameServers = this.dao.query(GameServer.class, Cnd.where("serverId","=",sid));
        }

        if (gameServers == null || gameServers.size()<=0){
            return new GmHashMap();
        }
        List<String> everyDay = DateUtil.getEveryDay(startDate,endDate);
        GmHashMap allData = new GmHashMap();
        for (String day:everyDay) {
            //将每天的所有服的数据加载一起，按每天的结果封装返回， 服务器id则返回all
            GmHashMap rtm = new GmHashMap();
            int roleCount = 0;
            int newRoleCount = 0;
            int loginCount = 0;
            int payNumInt = 0;
            int payCountPrice = 0;
            float loginArpu = 0f;
            float payArpu =0f;
            for (GameServer gs:gameServers) {
               if (sid.equals("all")){
                   rtm.put("serverId","all");
               }else {
                   rtm.put("serverId",gs.getServerID());
               }
                List<GmHashMap> dayCreate=new ReadFile(gs.getServerID(), "1","2018-04-07",day).getMsg();
                roleCount += outRepeat(dayCreate).size();
                List<GmHashMap> toDate=new ReadFile(gs.getServerID(), "1",day,day).getMsg();
                //System.out.println("新增数："+toDate);
                newRoleCount += outRepeat(toDate).size();
                List<GmHashMap> loginOut=new ReadFile(gs.getServerID(), "2",day,day).getMsg();
                //System.out.println("日活跃数："+loginOut);
                loginCount += outRepeat(loginOut).size();

                try {
                    Map<String,Object> map = new GmHashMap();
                    map.put("serverId",gs.getServerID());
                    map.put("startDate",day);
                    map.put("endDate",day);
                    Map payNum = JSON.parseObject(URLTool.sendMsg(ManageConf.SERVERID+":8012/payStatisc/getToDayPayNum?msg=".concat(URLTool.Encode(JSONObject.toJSONString(map))), null));
                    payNumInt += Integer.parseInt(payNum.get("num").toString());
                    payCountPrice += Integer.parseInt(payNum.get("price").toString());

                    if (Integer.parseInt(payNum.get("price").toString()) <= 0){
                        loginArpu += 0;
                    }else {
                        loginArpu += FloatUtil.getFloatKeepTwoBits(Integer.parseInt(payNum.get("price").toString()),Integer.parseInt(rtm.get("loginCount").toString()));
                    }
                    if (Integer.parseInt(payNum.get("price").toString()) <= 0){
                        payArpu += 0;
                    }else {
                        payArpu += FloatUtil.getFloatKeepTwoBits(Integer.parseInt(payNum.get("price").toString()),Integer.parseInt(payNum.get("num").toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw  new GmException("获取充值人数和充值总额URL请求异常");
                }

            }
            rtm.put("roleCount",roleCount);
            rtm.put("newRoleCount",newRoleCount);
            rtm.put("loginCount",loginCount);
            rtm.put("payNum",payNumInt);
            rtm.put("payCountPrice",payCountPrice);
            rtm.put("loginArpu",loginArpu);
            rtm.put("payArpu",payArpu);
            allData.put(day,rtm);

        }

        allData.put("listDay",everyDay);

        return allData;
    }

    @Override
    public GmHashMap queryOnlineNum(String serverId, String startDate, String endDate) throws GmException {

        GmHashMap allOnlineNum = new GmHashMap();
        List<String> everyDay = DateUtil.getEveryDay(startDate,endDate);
        for (String strDate:everyDay) {
            List<GmHashMap> strDateMsg=new ReadFile(serverId, "37",strDate,strDate).getMsg();
            List dayNum = new ArrayList();
            for (GmHashMap gm: strDateMsg ) {
                dayNum.add(Integer.parseInt(gm.get("size").toString()));
            }
            System.out.println(startDate+"的在线人数情况:"+dayNum);
            allOnlineNum.put(strDate,dayNum);
        }

        return allOnlineNum;
    }

    public List<GmHashMap> outRepeat(List<GmHashMap> oldList){

        List<GmHashMap> newList = new ArrayList<GmHashMap>();

        for(GmHashMap oldmap : oldList){

            String oldTime = oldmap.get("pname").toString();
            int t =0;
            for(Map newmap : newList){

                String newTime = newmap.get("pname").toString();
                if(newTime.equals(oldTime)){
                    break;
                }
                t++;
            }

            if(t == newList.size()){
                newList.add(oldmap);
            }
        }

        return newList;
    }
}
