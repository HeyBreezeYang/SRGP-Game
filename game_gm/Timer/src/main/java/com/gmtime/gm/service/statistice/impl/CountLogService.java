package com.gmtime.gm.service.statistice.impl;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.context.DataType;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.ReadFile;
import com.gmtime.cache.CacheGmConfig;
import com.gmtime.cache.GmDataConnectionCache;
import com.gmtime.gm.dao.OperateDBIF;
import com.gmtime.gm.dao.OperateRDBIF;
import com.gmtime.gm.service.statistice.CountLogServiceIF;
import com.gmtime.gm.sql.impl.BackDataSql;
import com.gmtime.gm.sql.impl.PlatformSql;
import com.gmtime.util.InitWithUse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by DJL on 2018/4/18.
 *
 * @ClassName gm
 * @Description
 */
@Service("countLogService")
public class CountLogService implements CountLogServiceIF{
    private Logger log = LoggerFactory.getLogger(CountLogService.class);
    private final OperateDBIF operateDB;
    private final OperateRDBIF operateRDB;
    @Autowired
    public CountLogService(@Qualifier("operateDB") OperateDBIF operateDB,
                           @Qualifier("operateRDB") OperateRDBIF operateRDB) {
        this.operateDB = operateDB;
        this.operateRDB = operateRDB;
    }

    private Map<String,Map<String,Set<String>>>  queryAccount(String endTime,String startTime) throws GmException {
        Map<String,Map<String,Set<String>>> map =new HashMap<>();
        List<Map<String,Object>> list= this.operateDB.queryResultForLocal(BackDataSql.SID_PLATFORM_UID,startTime,endTime);
        InitWithUse.queryKV2(map,list,"sid","platform","uid");
        return map;
    }

    private Map<String,Map<String,GmHashMap>> queryAllPay(String endTime)throws GmException {
        Map<String,Map<String,GmHashMap>> map =new HashMap<>();
        List<Map<String,Object>> list= this.operateDB.queryResultForLocal(BackDataSql.GET_PAY,endTime);
        for(Map<String,Object> m:list ){
            Map<String,GmHashMap> channel_map=map.get(m.get("sid").toString());
            if(channel_map==null){
                channel_map=new HashMap<>();
            }
            GmHashMap money=channel_map.get(m.get("channel").toString());
            if(money==null){
                money=new GmHashMap();
            }
            money.put("money",m.get("price"));
            money.put("num",m.get("num"));
            channel_map.put(m.get("channel").toString(),money);
            map.put(m.get("sid").toString(),channel_map);
        }
        return map;
    }

    private Map<String,Map<String,GmHashMap>> queryPlayerPay(String startTime,String endTime)throws GmException {
        List<Map<String,Object>> list= this.operateDB.queryResultForLocal(BackDataSql.GET_PAY_EVERYONE,startTime,endTime);
        return paymentConvertMap(list);
    }

    private Map<String,Map<String,GmHashMap>> paymentConvertMap(List<Map<String,Object>> payList){
        Map<String,Map<String,GmHashMap>> map=new HashMap<>();
        for(Map<String,Object> m:payList ){
            Map<String,GmHashMap> channel_map=map.get(m.get("sid").toString());
            if(channel_map==null){
                channel_map=new HashMap<>();
            }
            GmHashMap pid_map=channel_map.get(m.get("channel").toString());
            if(pid_map==null){
                pid_map=new GmHashMap();
            }
            pid_map.put(m.get("pid").toString(),m.get("price"));
            channel_map.put(m.get("channel").toString(),pid_map);
            map.put(m.get("sid").toString(),channel_map);
        }
        return map;
    }


    private List<String> queryGameLog(String sid,String channel,Object[] p,Object[] b,String time,String start,String end) throws GmException {
        List<String> pidList=new ArrayList<>();
        List<Map<String,Object>> list=this.operateDB.queryResultForLocal(BackDataSql.QUERY_OTHER_COMPREHENSIVE,channel,sid,end,channel,time,sid);
        List<Map<String,Object>> pids=this.operateDB.queryResultForLocal(BackDataSql.GET_CREATE,start,end,sid,channel);
        for(Map<String,Object> pid:pids){
            pidList.add(pid.get("pid").toString());
        }
        p[3]=b[3]=pidList.size();
        p[4]=list.get(0).get("player");
        p[6]=list.get(0).get("login");
        return pidList;
    }
    private void queryRegister(Object[] p,Set<String> all_account){
        if(all_account==null){
            p[5]=0;
        }else{
            p[5]=all_account.size();
        }
    }
    private void queryPayLog(Object[] p,Double allMoney,Double allPay,List<String> newPid,GmHashMap pid_pay){
        p[7]=allMoney==null?0D:allMoney;
        p[10]=allPay==null?0:allPay.intValue();
        boolean empty=pid_pay==null;
        p[11]=empty?0:pid_pay.size();
        double moneyToDay=0D;
        if(!empty){
            for(Object m:pid_pay.values()){
                moneyToDay+=Double.parseDouble(m.toString());
            }
            p[8]=moneyToDay;
            double newPay=0;
            int newNum=0;
            for(String pid:newPid){
                if(pid_pay.get(pid)!=null){
                    newNum+=1;
                    newPay+=Double.parseDouble(pid_pay.get(pid).toString());
                }
            }
            p[9]=newPay;
            p[12]=newNum;
            newPid.clear();
            pid_pay.clear();
        }else {
            p[8]=p[9]=p[12]=0;
        }
    }
    @Override
    public void saveAllLogResult(String time) throws GmException {
       String endTime=time.concat(" 23:59:59");
        String startTime=time.concat(" 00:00:00");
        saveComprehensive(time,startTime,endTime);
        saveLoss(time,endTime);
    }

    @Override
    public void saveSnapshot(String time) throws GmException {
        String keyRoot="olNum:".concat(time).concat(":");
        for(String sid: CacheGmConfig.getServer()){
            List<Object[]> dates=new ArrayList<>();
            String sidKey=keyRoot.concat(sid).concat(":");
            for(int i=0;i<=23;i++){
                Object[] o=new Object[5];
                o[0]=time;
                o[1]=sid;
                o[2]=i;
                String key;
                if(i<10){
                    key=sidKey+"0"+i;
                }else{
                    key=sidKey+i;
                }
                Set<String> olNum=this.operateRDB.getValueOfSet(key);
                if(olNum==null||olNum.isEmpty()){
                    continue;
                }
                List<Integer> valueList=new ArrayList<>();
                for(String v:olNum){
                    valueList.add(Integer.parseInt(v));
                }
                o[3]=Collections.max(valueList);
                o[4]=Collections.min(valueList);
                dates.add(o);
                this.operateRDB.deleteValue(key);
            }
            this.operateDB.BatchOperate(BackDataSql.ADD_OL,dates);
        }
    }

    private void saveComprehensive(String time,String startTime,String endTime)throws GmException{
        List<Object[]> pme=new ArrayList<>();
        List<Object[]> base=new ArrayList<>();
        Map<String,Map<String,Set<String>>> all_account= queryAccount(endTime,startTime);
        Map<String,Map<String,GmHashMap>> allMoney=queryAllPay(endTime);
        Map<String,Map<String,GmHashMap>> toDayMoney=queryPlayerPay(startTime,endTime);
        List<String> pidList;
        Double mny=null;
        Double num=null;
        GmHashMap tdm=null;
        for(String sid:CacheGmConfig.getServer()) {
            for (String kc : CacheGmConfig.getChannel()) {
                Object[] p=new Object[13];
                Object[] b=new Object[4];
                p[0]=b[0]=sid;
                p[1]=b[1]=kc;
                p[2]=b[2]=time;
                pidList=queryGameLog(sid,kc,p,b,time,startTime,endTime);
                try {
                    queryRegister(p,all_account.get(sid).get(kc));
                }catch (NullPointerException e){
                    queryRegister(p,null);
                }
                if(allMoney.get(sid)!=null){
                    if(allMoney.get(sid).get(kc)!=null){
                        mny=Double.parseDouble(allMoney.get(sid).get(kc).get("money").toString());
                        num=Double.parseDouble(allMoney.get(sid).get(kc).get("num").toString());
                    }
                }
                if(toDayMoney.get(sid)!=null){
                    tdm=toDayMoney.get(sid).get(kc);
                }
                queryPayLog(p,mny,num,pidList,tdm);
                pme.add(p);
                base.add(b);
                mny=0D;
                num=0D;
            }
        }
        this.operateDB.BatchOperate(BackDataSql.SAVE_COMPREHENSIVE,pme);
        this.operateDB.BatchOperate(BackDataSql.SAVE_BASE,base);
        log.info("ComprehensiveData end !~~");
    }

    private void saveLoss(String time,String endTime)throws GmException{
        Map<String,Set<String>> time_create=new HashMap<>();
        Set<String> login=new HashSet<>();
        for(String sid:CacheGmConfig.getServer()) {
            for (String kc : CacheGmConfig.getChannel()) {
                getTime_Create(time_create,sid,kc,endTime,time);
                getLogin(login,sid,kc,time);
                setLoss(time_create,login,sid,kc,time);
                time_create.clear();
                login.clear();
            }
        }
    }
    private void setLoss(Map<String,Set<String>> time_create,Set<String> login,String sid,String kc,String time) throws GmException {
        String nTime;
        List<Object[]> pnm=new ArrayList<>();
        for(int i=1;i<30;i++){
            nTime= DateUtil.getDateString(DateUtil.getDaytoDay(DateUtil.toDate(time),-i));
            List<Map<String,Object>> list=this.operateDB.queryResultForLocal(BackDataSql.GET_BASE_ID,sid,kc,nTime);
            if(list==null||list.isEmpty()){
                log.info("skip  :"+nTime);
                continue;
            }
            Set<String> tc =time_create.get(nTime);
            if(tc==null){
                continue;
            }
            Set<String> t =new HashSet<>(tc);
            t.removeAll(login);
            Object[] r=new Object[3];
            r[0]=list.get(0).get("id");
            r[1]=i;
            r[2]=t.size();
            pnm.add(r);
            tc.clear();
            t.clear();
        }
        this.operateDB.BatchOperate(BackDataSql.SAVE_LOSS,pnm);
    }
    private void getLogin(Set<String> login,String sid,String chan,String time) throws GmException {
        List<Map<String,Object>> logMsg=this.operateDB.queryResultForLocal(BackDataSql.GET_LOGIN,time,sid,chan);
        for (Map<String, Object> aLogMsg : logMsg) {
            login.add(aLogMsg.get("pid").toString());
        }
    }
    private void getTime_Create(Map<String,Set<String>> time_create, String sid,String chan,String endTime,String time)throws GmException {
        Date start= DateUtil.getDaytoDay(DateUtil.toDate(time),-29);
        String sTime= DateUtil.getFullDateString(start);
        List<Map<String,Object>> createMsg=this.operateDB.queryResultForLocal(BackDataSql.GET_CREATE,sTime,endTime,sid,chan);
        InitWithUse.queryKV(time_create,createMsg,"date","pid");
    }




    @Override
    public void saveNoonBoss(String time) throws GmException {
        List<GmHashMap> gd;
        Map<Object,Set<String>> markNum=new HashMap<>();
        List<Object[]> dataList=new ArrayList<>();
        for(String sid: CacheGmConfig.getServer()){
            gd=new ReadFile(sid, DataType.ZW_BOSS,time).getMsgList();
            for(GmHashMap g:gd){
                Set<String> set=markNum.get(g.get("index"));
                if(set==null){
                    set=new HashSet<>();
                }
                set.add(g.get("pid").toString());
                markNum.put(g.get("index"),set);
            }
            for(Object key:markNum.keySet()){
                dataList.add(new Object[]{sid,time,key,markNum.get(key).size()});
            }
            markNum.clear();
            gd.clear();
        }
        this.operateDB.BatchOperate(BackDataSql.SAVE_NOON_BOSS,dataList);
    }

    @Override
    public void saveNightBoss(String time) throws GmException {
        List<GmHashMap> gd;
        Map<Object,Set<String>> num=new HashMap<>();
        Map<Object,Integer> fre=new HashMap<>();
        List<Object[]> dataList=new ArrayList<>();
        for(String sid: CacheGmConfig.getServer()){
            gd=new ReadFile(sid, DataType.WS_BOSS,time).getMsgList();
            for(GmHashMap g:gd){
                Set<String> set=num.get(g.get("pft"));
                Integer f=fre.get(g.get("pft"));
                if(set==null){
                    set=new HashSet<>();
                }
                if(f==null){
                   f=0;
                }
                set.add(g.get("pid").toString());
                f+=1;
                num.put(g.get("pft"),set);
                fre.put(g.get("pft"),f);
            }
            for(Object key:num.keySet()){
                dataList.add(new Object[]{sid,time,key,fre.get(key),num.get(key).size()});
            }
            num.clear();
            fre.clear();
            gd.clear();
        }
        this.operateDB.BatchOperate(BackDataSql.SAVE_NIGHT_BOSS,dataList);
    }

    @Override
    public void saveShop(String time) throws GmException {
        List<GmHashMap> gd;
        List<Object[]> dataList=new ArrayList<>();
        Map<Object,Map<Object,Map<Object,GmHashMap>>> resMap=new HashMap<>();
        for(String sid: CacheGmConfig.getServer()){
            gd=new ReadFile(sid, DataType.SHOP,time).getMsgList();
            for(GmHashMap g:gd){
                Map<Object,Map<Object,GmHashMap>> type_gid=resMap.get(g.get("pft"));
                if(type_gid==null){
                    type_gid=new HashMap<>();
                }
                Map<Object,GmHashMap> gid_data=type_gid.get(g.get("spType"));
                if(gid_data==null){
                    gid_data=new HashMap<>();
                }
                GmHashMap data=gid_data.get(g.get("cid"));
                if(data==null){
                    data=new GmHashMap();
                    data.put("num",0);
                    data.put("fre",0);
                    data.put("payNum",new HashSet());
                }
                data.put("num", (int)data.get("num")+(int)g.get("value"));
                data.put("fre", (int)data.get("num")+1);
                ((Set)data.get("payNum")).add(g.get("pid"));
                gid_data.put(g.get("cid"),data);
                type_gid.put(g.get("spType"),gid_data);
                resMap.put(g.get("pft"),type_gid);
            }
            for (Object k:resMap.keySet()){
                Map<Object,Map<Object,GmHashMap>> type_gid=resMap.get(k);
                for(Object l:type_gid.keySet()){
                    Map<Object,GmHashMap> mp=type_gid.get(l);
                    for(Object o:mp.keySet()){
                        GmHashMap ghm=mp.get(o);
                        dataList.add(new Object[]{sid,k,time,o,l,ghm.get("num"),ghm.get("fre"),((Set)ghm.get("payNum")).size()});
                    }
                }
            }
            this.operateDB.BatchOperate(BackDataSql.SAVE_SHOP,dataList);
            dataList.clear();
            gd.clear();
            resMap.clear();
        }
    }

    @Override
    public void saveMonetary(String time) throws GmException {
        List<GmHashMap> gd;
        List<Object[]> dataList=new ArrayList<>();
        Map<Object,Map<Object,Map<Object,GmHashMap>>> resMap=new HashMap<>();
        for(String sid: CacheGmConfig.getServer()){
            gd=new ReadFile(sid, DataType.HB,time).getMsgList();
            for(GmHashMap g:gd){
                Map<Object,Map<Object,GmHashMap>> tc_tu=resMap.get(g.get("ptf"));
                if(tc_tu==null){
                    tc_tu=new HashMap<>();
                }
                Map<Object,GmHashMap> tu_data=tc_tu.get(g.get("type"));
                if(tu_data==null){
                    tu_data=new HashMap<>();
                }
                GmHashMap data=tu_data.get(g.get("cmd"));
                if(data==null){
                    data=new GmHashMap();
                    data.put("numC",0);
                    data.put("numP",new HashSet());
                }
                ((Set)data.get("numP")).add(g.get("id").toString());
                data.put("numC",(int)data.get("numC")+(int)g.get("change"));
                tu_data.put(g.get("cmd"),data);
                tc_tu.put(g.get("type"),tu_data);
                resMap.put(g.get("ptf"),tc_tu);
            }
            for(Object ptf:resMap.keySet()){
                for(Object typeC:resMap.get(ptf).keySet()){
                    for (Object typeU:resMap.get(ptf).get(typeC).keySet()){
                        GmHashMap k=resMap.get(ptf).get(typeC).get(typeU);
                        dataList.add(new Object[]{sid,ptf,time,typeC,typeU,k.get("numC"),((Set)k.get("numP")).size()});
                    }
                }
            }
            this.operateDB.BatchOperate(BackDataSql.SAVE_MONETARY,dataList);
            dataList.clear();
            gd.clear();
            resMap.clear();
        }
    }

    @Override
    public void saveCheckpoint(String time) throws GmException {
        List<GmHashMap> gd;
        List<Object[]> dataList=new ArrayList<>();
        Map<Object,Map<String,GmHashMap>> resMap=new HashMap<>();
        for(String sid: CacheGmConfig.getServer()){
            gd=new ReadFile(sid, DataType.GK,time).getMsgList();
            String key;
            for(GmHashMap g:gd){
                Map<String,GmHashMap> type_data=resMap.get(g.get("pft"));
                if(type_data==null){
                    type_data=new HashMap<>();
                }
                key=g.get("chapter").toString()+"-"+g.get("part").toString();
                GmHashMap data=type_data.get(key);
                if(data==null){
                    data=new GmHashMap();
                    data.put("passNum",new HashSet());
                    data.put("passFre",0);
                }
                data.put("passFre",(int)data.get("passFre")+1);
                ((Set)data.get("passNum")).add(g.get("pid"));
                type_data.put(key,data);
                resMap.put(g.get("pft"),type_data);
            }
            for(Object pft:resMap.keySet()){
                for(String task:resMap.get(pft).keySet()){
                    dataList.add(new Object[]{sid,time,pft,task,resMap.get(pft).get(task).get("passFre"),((Set)resMap.get(pft).get(task).get("passNum")).size()});
                }
            }
            this.operateDB.BatchOperate(BackDataSql.SAVE_CPT,dataList);
            dataList.clear();
            gd.clear();
            resMap.clear();
        }
    }


    private void createSave(String sid,String date,String hour) throws GmException {
        try {
            List<GmHashMap> player=ReadFile.readFileOfOne(sid,DataType.PLAYER,date,hour);
            if(player==null||player.isEmpty()){
                return;
            }
            List<Object[]> msg=new ArrayList<>();
            for(GmHashMap p:player){
                msg.add(new Object[]{p.get("pid"),p.get("pname"),p.get("actId"),
                        DateUtil.getFullDateString(new Date((Long) p.get("dt")))
                        ,p.get("ptf"),sid});
            }
            this.operateDB.BatchOperate(BackDataSql.Add_PLAYER,msg);
        } catch (IOException e) {
            throw new GmException(e.getMessage());
        }

    }
    private void updateSave(String sid,String date,String hour) throws GmException {
        try {
            List<GmHashMap> player=ReadFile.readFileOfOne(sid,DataType.PLAYER_MSG,date,hour);

            if(player==null||player.isEmpty()){
                return;
            }
            Map<String,GmHashMap> convertMap=new HashMap<>();
            for(GmHashMap map:player){
                GmHashMap cm=convertMap.get(map.get("pid").toString());
                if(cm==null){
                    convertMap.put(map.get("pid").toString(),map);
                }else{
                    long t=(cm.get("dt") instanceof Long)? (long) cm.get("dt") :0L;
                    long n=(map.get("dt") instanceof Long)? (long) map.get("dt") :0L;
                    if(t<=n){
                        convertMap.put(map.get("pid").toString(),map);
                    }
                }
            }
            Set<String> keys=convertMap.keySet();
            List<Object[]> update=new ArrayList<>();
            for(String key:keys){
                GmHashMap m=convertMap.get(key);
                String login=DateUtil.getFullDateString(new Date((Long) m.get("lgnDt")));
                GmHashMap cur= JSON.parseObject(m.get("cur").toString(),GmHashMap.class);
                int tre= cur.get("80400002")==null?0: (int) cur.get("80400002");
                int mny= cur.get("80400001")==null?0: (int) cur.get("80400001");
                update.add(new Object[]{tre, m.get("lv"),m.get("vip"),mny,m.get("ff"),login,key});
            }
            this.operateDB.BatchOperate(BackDataSql.UPDATE_P_MSG,update);
        } catch (IOException e) {
            throw new GmException(e.getMessage());
        }

    }

    @Override
    public long savePlayerMsg(String date,String hour,long lastPayLogTime) throws GmException {
        for(String sid:CacheGmConfig.getServer()){
            createSave(sid,date,hour);
            updateSave(sid,date,hour);
        }
        Connection conn_deliver= GmDataConnectionCache.getConnection("platform_deliver");
        List<Map<String,Object>> payMsg=this.operateDB.findOtherDB(conn_deliver,PlatformSql.GET_PAY_PLAYER_MSG,lastPayLogTime);
        GmDataConnectionCache.closeConnection(conn_deliver);
        if(payMsg!=null&&!payMsg.isEmpty()){
            lastPayLogTime=updatePlayerPayMsgOfTime(payMsg);
        }

        return lastPayLogTime;
    }

    @Override
    public void manualPlayerMsg(String start, String end) throws GmException {

    }

    @Override
    public boolean manualSaveComprehensive(String start, String end) throws GmException {
        return false;
    }

    @Override
    public boolean manualSaveLoss(String start, String end, int day) throws GmException {
        return false;
    }

    @Override
    public boolean manualSaveLTV(String start, String end, int day) throws GmException {
        return false;
    }

    @Override
    public boolean manualSaveCreate(String start, String end) throws GmException {
        return false;
    }

    @Override
    public boolean manualSavePay(String start, String end) throws GmException {
        return false;
    }

    @Override
    public boolean manualSaveLogin(String start, String end) throws GmException {
        return false;
    }

    @Override
    public boolean manualSaveFirstPay(String start, String end, int day) throws GmException {
        return false;
    }

    @Override
    public boolean manualSaveSecondPay(String start, String end, int day) throws GmException {
        return false;
    }

    private long updatePlayerPayMsgOfTime(List<Map<String, Object>> payMsg) throws GmException {
        Set<Long> t=new HashSet<>();
        Map<String,Integer> pay=new HashMap<>();
        Map<String,long[]> time=new HashMap<>();
        for(Map<String,Object> msg:payMsg){
            Integer pc=pay.get(msg.get("pid").toString());
            if(pc==null){
                List<Map<String, Object>> list=this.operateDB.queryResultForLocal(BackDataSql.EXIST_P_MSG,msg.get("pid"));
                pc= (Integer) list.get(0).get("payment");
                long lp=DateUtil.toDateTime(list.get(0).get("lastPayTime").toString()).getTime();
                time.put(msg.get("pid").toString(),new long[]{lp,lp});
            }
            long[] pt=time.get(msg.get("pid").toString());
            long lt=(long)msg.get("logTime");
            if( lt> pt[0]){
                pc +=(int) msg.get("price");
                if(lt>pt[1]){
                    pt[1]=lt;
                    t.add(lt);
                    time.put(msg.get("pid").toString(),pt);
                }
                pay.put(msg.get("pid").toString(),pc);
            }
        }
        List<Object[]> pm=new ArrayList<>();
        Set<String> keys=pay.keySet();
        for(String key:keys){
            pm.add(new Object[]{pay.get(key),DateUtil.getFullDateString(new Date(time.get(key)[1])),key});
        }
        this.operateDB.BatchOperate(BackDataSql.UPDATE_P_MSG_PAY,pm);
        return Collections.max(t);
    }



}
