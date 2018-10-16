package com.master.gm.service.LTV.impl;

import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.FloatUtil;
import com.gmdesign.util.ReadFile;
import com.gmdesign.util.URLTool;
import com.master.bean.dispoly.Channel;
import com.master.bean.dispoly.GameServer;
import com.master.gm.BaseService;
import com.master.gm.service.LTV.LTVServiceIF;
import com.master.util.ManageConf;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.*;

/**
 * Created by HP on 2018/7/20.
 */
@IocBean
public class LTVService extends BaseService implements LTVServiceIF {

    private static String PLAYER_DATE_PAY_URL= ManageConf.SERVERID+":8012/yhPayStatistical/playerDatePay?msg=";

    @Override
    public List<GameServer> getAllServer() {
        return this.dao.query(GameServer.class,null);
    }

    @Override
    public List<Channel> getAllChannel() {
        return this.dao.query(Channel.class,null);
    }

    @Override
    public List<GmHashMap> queryLTV(String sid, String channel, Integer ltvType, String rstartDate, String rendDate,String qstartDate,String qendDate) throws GmException {
        List<GmHashMap> ret = new ArrayList<>();
        List<String> everyDay = DateUtil.getEveryDay(rstartDate,rendDate);
        Map param = new HashMap();

        param.put("channel",channel);
        param.put("startDate",qstartDate);
        param.put("endDate",qendDate);
        String pids = "";

        List<GameServer> gameServers;
        if (sid.equals("all")){
            gameServers = this.dao.query(GameServer.class,null);
        }else {

            gameServers = this.dao.query(GameServer.class, Cnd.where("serverID","=",sid));
        }
        for (GameServer gm:gameServers) {
            param.put("sid",sid);
            if (ltvType == 1){
                for (String toDay:everyDay) {
                    //日充值金额
                    GmHashMap dayDataMap = new GmHashMap();
                    List<GmHashMap> newAddRole=outRepeat(new ReadFile(gm.getServerID(), "1",toDay,toDay).getMsg());
                    //System.out.println("新增数："+newAddRole);
                    for (GmHashMap gmMap:newAddRole) {
                        pids += gmMap.get("pid").toString()+",";
                    }
                    param.put("pids",pids);
                    List<GmHashMap> loginOut=new ReadFile(gm.getServerID(), "2",toDay,toDay).getMsg();
                    String urlRet = URLTool.sendMsg(PLAYER_DATE_PAY_URL.concat(URLTool.Encode(JSONObject.toJSONString(param))),null);
                    List<String> urlList = Arrays.asList(urlRet.replace("[","").replace("]","").split(","));
                    List retList = new ArrayList();
                    for (String s:urlList) {
                        if (s.equals("0")){
                            retList.add(0);
                        }else {
                            retList.add(FloatUtil.getFloatKeepTwoBits(Integer.parseInt(s),loginOut.size()));
                        }
                    }
                    dayDataMap.put(toDay,retList);
                    dayDataMap.put("register",newAddRole.size());
                    dayDataMap.put("date",toDay);
                    ret.add(dayDataMap);
                }

            }
            if (ltvType == 2){
                //充值金额
                for (String toDay:everyDay) {
                    GmHashMap dayDataMap = new GmHashMap();
                    List<GmHashMap> newAddRole=outRepeat(new ReadFile(gm.getServerID(), "1",toDay,toDay).getMsg());
                    //System.out.println("新增数："+newAddRole);
                    for (GmHashMap gmMap:newAddRole) {
                        pids += gmMap.get("pid").toString()+",";
                    }
                    param.put("pids",pids);
                    String urlRet = URLTool.sendMsg(PLAYER_DATE_PAY_URL.concat(URLTool.Encode(JSONObject.toJSONString(param))),null);
                    List<String> urlList = Arrays.asList(urlRet.replace("[","").replace("]","").split(","));
                    dayDataMap.put(toDay,urlList);
                    dayDataMap.put("register",newAddRole.size());
                    dayDataMap.put("date",toDay);
                    ret.add(dayDataMap);
                }
            }
        }
        System.out.println("ret:"+ret);
        return ret;
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
