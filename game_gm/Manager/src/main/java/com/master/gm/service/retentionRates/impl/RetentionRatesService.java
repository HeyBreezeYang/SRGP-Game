package com.master.gm.service.retentionRates.impl;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.FloatUtil;
import com.gmdesign.util.ReadFile;
import com.master.bean.dispoly.Channel;
import com.master.bean.dispoly.GameServer;
import com.master.gm.BaseService;
import com.master.gm.service.retentionRates.RetentionRatesServiceIF;
import org.nutz.ioc.loader.annotation.IocBean;

import java.text.ParseException;
import java.util.*;

/**
 * Created by HP on 2018/7/17.
 */
@IocBean
public class RetentionRatesService extends BaseService implements RetentionRatesServiceIF {
    @Override
    public List<GameServer> getAllServer() {
        return this.dao.query(GameServer.class,null);
    }

    @Override
    public List<Channel> getAllChannel() {
        return this.dao.query(Channel.class,null);
    }

    @Override
    public List queryRetentionRatesData(String sid, String channel, String startDate, String endDate) throws GmException, ParseException {

        System.out.println("接收倒得参数sid："+sid+"==channel:"+channel+"==startDate:"+startDate+"==endDate:"+endDate);
        List allRR = new ArrayList();
        List<String> everyDay = DateUtil.getEveryDay(startDate,endDate);
        for (String day:everyDay) {
            List dayRR = new ArrayList();
            Map toDayRR = new HashMap();
            for(int i=0;i<everyDay.size();i++){
                String newDay1 = null;
                if (i!=0){
                   /*Date dayDate = DateUtil.stringToDate(day);
                   newDay = DateUtil.dateToString(DateUtil.getDaytoDay(dayDate,i));*/
                    long s = 0l;
                    long e = 0l;
                    try {
                        Date dayDate2 = DateUtil.stringToDate(day);
                        newDay1 = DateUtil.dateToString(DateUtil.getDaytoDay(dayDate2,i+1));
                        s = DateUtil.getStringToCurrentTimeMillis(newDay1,"yyyy-MM-dd");
                        e =  DateUtil.getStringToCurrentTimeMillis(DateUtil.getCurrentTimeMillisToString(System.currentTimeMillis(),"yyyy-MM-dd"),"yyyy-MM-dd");
                        if (s>e){
                            newDay1 = null;
                        }
                    }catch (ParseException p){

                    }
                }else {
                    Date dayDate2 = DateUtil.stringToDate(day);
                    newDay1 = DateUtil.dateToString(DateUtil.getDaytoDay(dayDate2,1));
                }
               if(!sid.equals("all")){
                   List<GmHashMap> toDate=new ReadFile(sid, "1",day,day).getMsg();
                   int bDayAddNum= outRepeat(toDate).size();
                   toDayRR.put("newNum",bDayAddNum);
                   toDayRR.put("sid",sid);
                   if (newDay1 != null){

                       List<GmHashMap> loginOut=new ReadFile(sid, "2",newDay1,newDay1).getMsg();
                       int toDayLogin = 0;//outRepeat(loginOut).size();
                       for (GmHashMap map:outRepeat(toDate)) {
                           for (GmHashMap map1:outRepeat(loginOut)) {
                               if (map.get("pid").toString().equals(map1.get("pid").toString()) && map.get("pname").toString().equals(map1.get("pname").toString())){
                                   toDayLogin +=1;
                               }
                           }
                       }
                       if (toDayLogin != 0){
                           dayRR.add((FloatUtil.getFloatKeepTwoBits(toDayLogin,bDayAddNum))*100);
                       }else {
                           dayRR.add(0);
                       }
                     }
               }else {
                   List<GameServer> gameServers = this.dao.query(GameServer.class,null);
                   for (GameServer gm:gameServers) {
                       List<GmHashMap> toDate=new ReadFile(gm.getServerID(), "1",day,day).getMsg();
                       int bDayAddNum= outRepeat(toDate).size();
                       toDayRR.put("newNum",bDayAddNum);
                       toDayRR.put("sid",gm.getServerID());
                       if (newDay1 != null){

                           List<GmHashMap> loginOut=new ReadFile(gm.getServerID(), "2",newDay1,newDay1).getMsg();
                           int toDayLogin = 0;//outRepeat(loginOut).size();
                           for (GmHashMap map:outRepeat(toDate)) {
                               for (GmHashMap map1:outRepeat(loginOut)) {
                                   if (map.get("pid").toString().equals(map1.get("pid").toString()) && map.get("pname").toString().equals(map1.get("pname").toString())){
                                       toDayLogin +=1;
                                   }
                               }
                           }
                           if (toDayLogin != 0){
                               dayRR.add((FloatUtil.getFloatKeepTwoBits(toDayLogin,bDayAddNum))*100);
                           }else {
                               dayRR.add(0);
                           }
                       }
                   }
               }

           }
            toDayRR.put(day,dayRR);
            allRR.add(toDayRR);
        }

        return allRR;
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
