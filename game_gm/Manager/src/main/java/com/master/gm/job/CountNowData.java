package com.master.gm.job;

import java.io.IOException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.context.DataType;
import com.gmdesign.context.GmConfiger;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.ReadFile;
import com.gmdesign.util.SendUtil;
import com.master.bean.dispoly.Parameter;
import com.master.cache.InitTableDataCache;
import com.master.gm.BaseService;
import com.master.gm.service.count.impl.NowDataService;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by DJL on 2017/8/11.
 *
 * @ClassName CountNowData
 * @Description 统计当前(主界面)数据
 */
@IocBean
public class CountNowData extends BaseService implements Job{

    @Inject
    private NowDataService nowDataService;
    @Inject
    protected RedisService redisService;

    private static final Map<String,Map<String,Integer>> createCount=new HashMap<>();
    private static final Map<String,Set<String>> loginCount=new HashMap<>();

    private static String day;
    static{
        day=DateUtil.getDateString(new Date());
    }
    private int computer(Map<String,Integer> map){
        int all=0;
        for(Integer a:map.values()){
            all+=a;
        }
        return all;
    }
    private Map<String,Integer> readyMap(Map<String,List<GmHashMap>> map){
        Map<String,Integer> res =new HashMap<>();
        for(String key:map.keySet()){
            res.put(key,countToday(map.get(key)));
        }
        return res;
    }
    private static long QUERY_PAY_TIME=0L;
    private static final Map<String,GmHashMap> payMap=new HashMap<>();
    private void resetPayMsg() throws GmException {
        String p="time="+String.valueOf(DateUtil.toDate(day).getTime())+"&app="+ GmConfiger.APP;
        String url=this.dao.fetch(Parameter.class,"deliverUrl").getPrams().concat("/payMsg");
        String res=SendUtil.sendHttpMsg(url,p);
        List<GmHashMap> rl=JSON.parseArray(res,GmHashMap.class);
        for(GmHashMap r:rl){
            payMap.put(r.get("serverId").toString(),r);
        }
    }

    private Set<String> readyMapForLogin(Map<String,List<GmHashMap>> map){
        Set<String> set =new HashSet<>();
        Set <String> ks=map.keySet();
        List<GmHashMap> mList;
        for(String key:ks){
            mList=map.get(key);
            for (GmHashMap m:mList){
                set.add(m.get("pid").toString());
            }
        }
        return set;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date d=new Date();
        String toDay= DateUtil.getDateString(d);
        if(!toDay.equals(day)){
            createCount.clear();
            loginCount.clear();
            payMap.clear();
            day=toDay;
        }
        try {
            if(QUERY_PAY_TIME+300000<=d.getTime()){
//                resetPayMsg();
                QUERY_PAY_TIME=d.getTime();
            }
            String hour=DateUtil.getDateHour(d);
            Map<String,String> urlAll=this.nowDataService.allServerUrl();
            List<String[]> msg=new ArrayList<>();
            int create,login;
            String keyRoot="olNum:"+toDay.concat(":");
            for(String sid:urlAll.keySet()){
                Map<String,Integer> cc=createCount.get(sid);
                if(cc==null){
                    cc=readyMap(new ReadFile(sid, DataType.PLAYER,toDay).getMsgMap());
                }
                Set<String> lc=loginCount.get(sid);
                if(lc==null){
                    lc=readyMapForLogin(new ReadFile(sid, DataType.LOGIN,toDay).getMsgMap());
                }
                cc.put(hour,countToday(ReadFile.readFileOfOne(sid, DataType.PLAYER,toDay,hour)));
                lc.addAll(convertSet(ReadFile.readFileOfOne(sid, DataType.LOGIN,toDay,hour)));
                create=computer(cc);
                login=lc.size();
                Map m=JSON.parseObject(SendUtil.sendHttpMsg(urlAll.get(sid),null),Map.class);
                String olNum=((Map)m.get("ret")).get("size").toString();
//                GmHashMap pm=payMap.get(sid);
//                if(pm!=null&&!pm.isEmpty()){
//                    msg.add(new String[]{sid,String.valueOf(create),String.valueOf(login),olNum,StringUtil.isNullString(pm.get("num")),StringUtil.isNullString(pm.get("money"))});
//                }else{
//                    msg.add(new String[]{sid,String.valueOf(create),String.valueOf(login),olNum,"0","0"});
//                }
                msg.add(new String[]{sid,String.valueOf(create),String.valueOf(login),olNum,"0","0"});
                String key=keyRoot.concat(sid).concat(":").concat(hour);
                this.redisService.sadd(key,olNum);
                createCount.put(sid,cc);
                loginCount.put(sid,lc);
            }
            String msgKey="rpMsg:"+toDay.concat(":")+DateUtil.getFullDateString2(d).substring(0,4)+"0";
            this.redisService.set(msgKey,JSON.toJSONString(msg));
            InitTableDataCache.setNowData(msg);
        } catch (GmException | IOException e) {
            e.printStackTrace();
        }
    }

    private Set<String> convertSet(List<GmHashMap> msg){
        Set<String> pid =new HashSet<>();
        for (GmHashMap m:msg){
            pid.add(m.get("pid").toString());
        }
        return pid;
    }

    private int countToday(List<GmHashMap> msg){
        Set<String> pid =convertSet(msg);
        return pid.size();
    }
}
