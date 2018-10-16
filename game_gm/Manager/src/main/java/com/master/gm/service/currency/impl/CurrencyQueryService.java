package com.master.gm.service.currency.impl;

import java.rmi.RemoteException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.context.DataType;
import com.gmdesign.exception.GmException;
import com.gmdesign.rmi.GmOperate;
import com.gmdesign.rmi.impl.GmRmiOperate;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.ReadFile;
import com.gmdesign.util.SendUtil;
import com.master.bean.back.Player;
import com.master.bean.back.SnapshotBean;
import com.master.bean.back.WorldBossInNight;
import com.master.bean.dispoly.Channel;
import com.master.bean.dispoly.GameServer;
import com.master.bean.dispoly.Parameter;
import com.master.cache.GmDocConfig;
import com.master.gm.BaseService;
import com.master.gm.bean.GmSql;
import com.master.gm.service.currency.CurrencyQueryServiceIF;
import com.master.gm.service.currency.QueryType;
import com.master.gm.service.currency.StatisticalBody;
import com.master.util.SqlUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Sql;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/7/5.
 *
 * @ClassName CurrencyQueryService
 * @Description
 */
@IocBean
public class CurrencyQueryService extends BaseService implements CurrencyQueryServiceIF{
    @Inject
    private RedisService redisService;

    private GmHashMap queryCnd(String key,Object value){
        GmHashMap exclude=new GmHashMap();
        List<Object> c=new ArrayList<>();
        c.add(value);
        exclude.put(key,c);
        return exclude;
    }

    private List<GmHashMap> queryWordBossInNight(String start,String end,String server,String channel) throws GmException {
        List<GmHashMap> count=new ArrayList<>();
        if(!channel.equals("all")){
            List<WorldBossInNight> list= this.dao.query(WorldBossInNight.class,Cnd.where("logTime",">=",start).and("logTime","<=",end)
                    .and("channel","=",channel).and("sid","=",server));
            for(WorldBossInNight bean:list){
                GmHashMap cm =new GmHashMap();
                cm.put("date",bean.getTime());
                cm.put("frequency",bean.getFrequency());
                cm.put("num",bean.getNum());
                count.add(cm);
            }
        }else{
            Sql sql = SqlUtil.getSql(GmSql.QUERY_BOSS_NIGHT,server,start,end);
            this.dao.execute(sql);
            count=sql.getList(GmHashMap.class);
        }
        return count;
    }
    private List<GmHashMap> queryForSQL(GmSql gmSql, Object... params){
        List<GameServer> gameServers = null;
        List<Channel> channels = null;
        if (params.length <= 1){
            gameServers = this.dao.query(GameServer.class,null);
        }else {
            gameServers = this.dao.query(GameServer.class,Cnd.where("serverID","=",params[0]));
        }
        if (params.length <= 1){
            if (params[0].equals("all")){
                channels = this.dao.query(Channel.class,null);
            }else {
                channels = this.dao.query(Channel.class,Cnd.where("cname","=",params[1]));
            }
        }else {
            if (params[1].equals("all")){
                channels = this.dao.query(Channel.class,null);
            }else {
                channels = this.dao.query(Channel.class,Cnd.where("cname","=",params[1]));
            }
        }
        System.out.println("渠道集合是否为空:"+channels+"=="+channels.size());
        //渠道恢复正常的时候删除这行代码即可
        if (channels != null && channels.size()>0){
            channels.get(0).setName("1006");
        }
        List<GmHashMap> ret = new ArrayList<>();
        for (GameServer gs:gameServers) {
            for (Channel c:channels) {
                Object[] obj ;
                if (params.length <= 1){
                    obj = new Object[1];
                    obj[0] = c.getName();
                }else {
                    obj = new Object[2];
                    obj[0] = gs.getServerID();
                    obj[1] = c.getName();
                }
                Sql sql = SqlUtil.getSql(gmSql, obj);
                System.out.println("sql" + sql.toString());
                this.dao.execute(sql);
                List<GmHashMap> list = sql.getList(GmHashMap.class);
                if (list != null && list.size() > 0) {
                    ret.addAll(list);
                }
            }
        }

        return ret;
    }

    private List<GmHashMap> queryCurrencyDetailed(int type,String start,String end,String server,String value) throws GmException {
        GmHashMap cnd=new GmHashMap();
        cnd.putAll(queryCnd("pname",value));
        cnd.putAll(queryCnd("type",type));
        List<GmHashMap> res=new ReadFile(server,DataType.HB,start,end).getMsg(cnd);
        if(!res.isEmpty()){
//            System.out.println(GmDocConfig.CMD);
            for(GmHashMap r:res){
                r.put("cmdName",GmDocConfig.CMD.get(r.get("cmd").toString()));
                r.put("date", DateUtil.formatDateTime((Long) r.get("dt")));
            }
        }
        return res;
    }

    private List<GmHashMap> queryLogForDetailed(String type,String start,String end,String server,GmHashMap cnd) throws GmException {
        System.out.println("server"+server+"==type"+type+"==start"+start+"==end"+end);
        List<GmHashMap> res=new ReadFile(server,type,start,end).getMsg(cnd);
        if(!res.isEmpty()){
            for(GmHashMap r:res){
                r.put("cmdName",GmDocConfig.CMD.get(r.get("cmd").toString()));
                r.put("date", DateUtil.formatDateTime((Long) r.get("dt")));
                r.put("num",r.get("change".toString()));
                if(r.containsKey("cid")){
                    r.put("goodsName",GmDocConfig.GOODS.get(r.get("cid").toString()));
                }
                if(r.containsKey("gCid")){
                    r.put("goodsName", GmDocConfig.GOODS.get(r.get("cid").toString()));
                }
                if (r.containsKey("spType")){
                    r.put("spType",GmDocConfig.STORE.get(r.get("spid").toString()));
                }else {
                    r.put("spType","商城");
                }
            }
        }
        return res;
    }

    private List<GmHashMap> queryLogForCount(String type,String start,String end,String server,String channel,StatisticalBody bean,GmHashMap c)throws GmException {
        List<GmHashMap> count=new ArrayList<>();
        long now=System.currentTimeMillis();
        boolean isAllChannel=channel.equals("all");
        if(end.length()>1){
            if(DateUtil.toDate(end).getTime()-now>-86399999){
                Date today=new Date(now);
                end=DateUtil.getDateString(DateUtil.getDaytoDay(today,-1));
                String todayString=DateUtil.getDateString(today);
                ReadFile rf =new ReadFile(server,type,start,end);
                GmHashMap cnd=queryCnd("plf",channel);
                if(c!=null){
                    cnd.putAll(c);
                }
                List<GmHashMap> res=isAllChannel?rf.getMsg(c):rf.getMsg(cnd);
                if(!res.isEmpty()){
                    for(GmHashMap r:res){
                        if (r.get("cid").toString() == null || r.get("cid").toString().equals("")){
                            System.out.println("商品id为空");
                        }
                        r.put("goodsName",GmDocConfig.GOODS.get(r.get("cid").toString()));
                    }
                }
                count.addAll(bean.countToday(res,todayString));
            }
        }
        if(!isAllChannel){
            bean.countDbOne(count,dao,c,start,end,channel,server);
        }else{
            bean.countDbTwo(count,dao,c,server,start,end);
        }
        return count;
    }

    @Override
    public List<GmHashMap> queryMassage(String start, String end, String server, Object[] other) throws GmException {
        int type=Integer.parseInt(other[0].toString());
        List<GmHashMap> res;
        switch (type) {
            case QueryType.GOODS:
                res=queryLogForDetailed(DataType.GOODS,start,end,server,queryCnd("pname",other[1].toString()));
                break;
            case QueryType.CURR_JY_1:
                res=queryCurrencyDetailed(80400004,start,end,server,other[1].toString());
                break;
            case QueryType.CURR_RM_1:
                res=queryCurrencyDetailed(80400003,start,end,server,other[1].toString());
                break;
            case QueryType.CURR_ZS_1:
                res=queryCurrencyDetailed(80400002,start,end,server,other[1].toString());
                break;
            case QueryType.CURR_XJ_1:
                res=queryCurrencyDetailed(80400001,start,end,server,other[1].toString());
                break;
            case QueryType.CURR_JY_2:
                res=queryLogForCount(DataType.HB,start,end,server,other[1].toString(),MonetaryStatistics.createCount(), queryCnd("type",80400004));
                break;
            case QueryType.CURR_RM_2:
                res=queryLogForCount(DataType.HB,start,end,server,other[1].toString(),MonetaryStatistics.createCount(), queryCnd("type",80400003));
                break;
            case QueryType.CURR_XJ_2:
                res=queryLogForCount(DataType.HB,start,end,server,other[1].toString(),MonetaryStatistics.createCount(), queryCnd("type",80400001));
                break;
            case QueryType.CURR_ZS_2:
                res=queryLogForCount(DataType.HB,start,end,server,other[1].toString(),MonetaryStatistics.createCount(), queryCnd("type",80400002));
                break;
            case QueryType.CURR_MLZ_1:
                res=queryLogForDetailed(DataType.MLZ,start,end,server,queryCnd("pname",other[1].toString()));
                break;
            case QueryType.CURR_QMD_1:
                res=queryLogForDetailed(DataType.QMD,start,end,server,queryCnd("pname",other[1].toString()));
                break;
            case QueryType.CURR_MLZ_2:
                res=queryDataStatistics(DataType.MLZ,start,end,server,other[1].toString());
                break;
            case QueryType.CURR_QMD_2:
                res=queryDataStatistics(DataType.QMD,start,end,server,other[1].toString());
                break;
            case QueryType.CURR_KTV_1:
                res=queryCurrencyDetailed(80400006,start,end,server,other[1].toString());
                break;
            case QueryType.CURR_KTV_2:
                res=queryLogForCount(DataType.HB,start,end,server,other[1].toString(),MonetaryStatistics.createCount(), queryCnd("type",80400006));
                break;
            case QueryType.CL_FQ:
                res=countData3(DataType.CL_FQ,server,start,end,other[1].toString());
                break;
            case QueryType.CL_WC:
                res=countData3(DataType.CL_WC,server,start,end,other[1].toString());
                break;
            case QueryType.CURR_YLZ_1:
                res=queryLogForDetailed(DataType.YH_FNISH,start,end,server,queryCnd("pname",other[1].toString()));
                break;
            case QueryType.WS_BOSS_1:
                res=queryLogForDetailed(DataType.WS_BOSS,start,end,server,queryCnd("pname",other[1].toString()));
                break;
            case QueryType.WS_BOSS_2:
                res=queryWordBossInNight(start,end,server,other[1].toString());
                break;
            case QueryType.ZW_BOSS_1:
                res=queryLogForDetailed(DataType.ZW_BOSS,start,end,server,queryCnd("pname",other[1].toString()));
                break;
            case QueryType.ZW_BOSS_2:
                res=queryForSQL(GmSql.QUERY_BOSS_NOON,server,start,end);
                break;
            case QueryType.LOSE_STEP:
                res=queryStepCount(end,server,other[1].toString(),other[2].toString());
                break;
            case QueryType.CL_GOODS:
                res=countData4(DataType.CL_DJ,server,start,end,other[1].toString());
                break;
            case QueryType.CYD_ZLZ:
                res=queryYLZ(server,start,end,other[1].toString());
                break;
            case QueryType.CYD_XB:
                res=querySchoolSuperMan(server,start,end,other[1].toString());
                break;
            case QueryType.CYD_LY:
                res=countLY(server,start,end,other[1].toString());
                break;
            case QueryType.CYD_ZS:
                res=countData(DataType.ZS,server,start,end,other[1].toString());
                break;
            case QueryType.CYD_JJ:
                res=queryTeach(server,start,end,other[1].toString());
                break;
            case QueryType.CYD_DS:
                res=queryMaxIndex(DataType.DS,server,start,end,other[1].toString());
                break;
            case QueryType.CYD_QDP:
                res=queryMaxIndex(DataType.QDP,server,start,end,other[1].toString());
                break;
            case QueryType.PLAYER:
                res=queryForSQL(GmSql.PLAYER_MSG,other[1].toString().concat(" 00:00:00"),other[2].toString().concat(" 23:59:59"),server);
                break;
            case QueryType.PAY_MSG:
                res=payMsg(server,start,end,other[1].toString());
                break;
            case QueryType.SHOP_C:
                res=queryLogForCount(DataType.SHOP,start,end,server,other[1].toString(),ShopStatistics.createCount(),queryCnd("spid",other[2].toString()));
                break;
            case QueryType.SHOP_D:
                res=queryLogForDetailed(DataType.SHOP,start,end,server,queryCnd("cid",other[1].toString()));
                break;
            case QueryType.LEAGUE_B:
                res=queryLeagueBoss(server,start,end,other[1].toString());
                break;
            case QueryType.LEAGUE_R:
                res=getRank(server,"10");
                break;
            case QueryType.LEAGUE_M:
                res=queryGuildMsg(server,other[1].toString());
                break;
            case QueryType.PLAYER_RANK:
                res=getRank(server,other[1].toString());
                break;
            case QueryType.TASK_MR:
                res=queryTask(server,start,end,other[1].toString());
                break;
            case QueryType.TASK_GK:
                res=queryLogForCount(DataType.GK,start,end,server,other[1].toString(),CheckpointStatistics.createCount(),null);
                break;
            case QueryType.VIP_RANK:
                System.out.println("VIP_RANK");
                res=queryForSQL(GmSql.QUERY_VIP_RANK,server,other[1].toString());
                break;
            case QueryType.VIP_NUM:
                System.out.println("VIP_NUM");
                res=queryForSQL(GmSql.QUERY_VIP_NUM,server,other[1].toString());
                break;
            case QueryType.VIP_NUM_ALL:
                System.out.println("VIP_NUM_ALL");
                res=queryForSQL(GmSql.QUERY_VIP_NUM_ALL,other[1].toString());
                break;
            case QueryType.CYD_XF:
                res=countData2(DataType.XF,server,start,end,other[1].toString());
                break;
            case QueryType.LV_DISTRIBUTION:
                res=queryLvDistribution(server,end,other[1].toString(),other[2].toString());
                break;
            case QueryType.ONLINE_NUM:
                res=getOnlineNum(server,start,end);
                break;
            case QueryType.LOGIN_TIME:
                res=getLOGIN_TIME(server,start,end,other[1].toString());
                break;
            case QueryType.LW_ZS:
                res=getDataForOneKey(DataType.LW_ZS,"len",server,start,end,other[1].toString());
                break;
            case QueryType.LW_JJ:
                res=getDataForOneKey(DataType.LW_JJ,"maxLen",server,start,end,other[1].toString());
                break;
            default:
                res=null;
                break;
        }
        return res;
    }



    private List<GmHashMap> getOnlineNum(String sid,String start,String end){
        List<GmHashMap> res=new ArrayList<>();
        if(end.length()>1){
            long now=System.currentTimeMillis();
            if(DateUtil.toDate(end).getTime()-now>-86399999){
                Date today=new Date(now);
                end=DateUtil.getDateString(DateUtil.getDaytoDay(today,-1));
                String todayString=DateUtil.getDateString(today);
                GmHashMap r=new GmHashMap();
                r.put("logTime",todayString);
                String keyRoot="olNum:"+todayString+":"+sid+":";
                List<Integer> valueList=new ArrayList<>();
                for(int i=0;i<=23;i++){
                    String key;
                    if(i<10){
                        key=keyRoot+"0"+i;
                    }else{
                        key=keyRoot+i;
                    }
                    Set<String> olNum=this.redisService.smembers(key);
                    if(olNum==null||olNum.isEmpty()){
                        continue;
                    }
                    for(String v:olNum){
                        valueList.add(Integer.parseInt(v));
                    }
                }
                r.put("pcu",Collections.max(valueList));
                int all=0;
                for (Integer a:valueList){
                    all+=a;
                }
                r.put("acu",all/valueList.size());
                res.add(r);
            }
        }
        res.addAll(queryForSQL(GmSql.GET_CU,sid,start,end));
        return res;
    }


    private List<GmHashMap> queryGuildMsg(String server,String name) throws GmException{
        String url= this.dao.fetch(GameServer.class,Cnd.where("serverID","=",server)).getServerUrl().concat("/queryGuild");
        GmHashMap res=JSON.parseObject(SendUtil.sendHttpMsg(url,"name="+name),GmHashMap.class) ;
        System.out.println(res);
        List<JSONObject>list= (List<JSONObject>) res.get("ret");
        List<JSONObject>members= (List<JSONObject>) list.get(0).get("members");
        List<GmHashMap> resList=new ArrayList<>();
        for(JSONObject mem:members){
            GmHashMap map=new GmHashMap();
            map.putAll(mem);
            map.put("join",DateUtil.formatDateTime((Integer) map.get("join")*1000L));
            map.put("login",DateUtil.formatDateTime((Integer) map.get("login")*1000L));
            map.put("logout",DateUtil.formatDateTime((Integer) map.get("logout")*1000L));
            resList.add(map);
        }
        return resList;
    }

    private List<GmHashMap> getRank(String server,String type) throws GmException {
        String url= this.dao.fetch(GameServer.class,Cnd.where("serverID","=",server)).getServerUrl().concat("/getRank");
        GmHashMap res=JSON.parseObject(SendUtil.sendHttpMsg(url,"rankType="+type+"&start=0&end=100"),GmHashMap.class) ;
//        List<GmHashMap> r=( List<GmHashMap>) res.get("ret");
//        Collections.sort(r,Comparator.comparing(o -> new Integer(o.get("val").toString())));
//        return r;
        return ( List<GmHashMap>) res.get("ret");
    }

    private List<GmHashMap> countLY(String server,String start,String end,String channel)throws GmException{
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Map<String,List<GmHashMap>> ls=new HashMap<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(server,DataType.LY,t);
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            for(GmHashMap l:list){
                List<GmHashMap> l_m=ls.get(l.get("exam").toString());
                if(l_m==null){
                    l_m=new ArrayList<>();
                }
                l_m.add(l);
                ls.put(l.get("exam").toString(),l_m);
            }
            for(String k:ls.keySet()){
                GmHashMap r=new GmHashMap();
                r.put("logTime",t);
                r.put("sonLv",k);
                r.put("marriage",ls.get(k).size());
                res.add(r);
            }
            ls.clear();
        }
        return res;
    }
    private List<GmHashMap> getDataForOneKey(String dataType,String key,String sid,String start,String end,String channel) throws GmException {
        List<GmHashMap> res=new ArrayList<>();
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Map<String,Set<String>> data=new HashMap<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(sid,dataType,t);
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            for(GmHashMap map:list){
                Set<String> mset=data.get(map.get(key).toString());
                if(mset==null){
                    mset=new HashSet<>();
                }
                mset.add(map.get("pid").toString());
                data.put(map.get(key).toString(),mset);
            }
            for(String k:data.keySet()){
                GmHashMap g=new GmHashMap();
                g.put("logTime",t);
                g.put("no",k);
                g.put("num",data.get(k).size());
                res.add(g);
            }
            data.clear();
        }
        return res;
    }

    private List<GmHashMap> countData(String type,String server,String start,String end,String channel)throws GmException{
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        for(String t:times){
            ReadFile readFile= new ReadFile(server,type,t);
            GmHashMap r=new GmHashMap();
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            r.put("logTime",t);
            r.put("zxNum",list.size());
            res.add(r);
        }
        return res;
    }

    private List<GmHashMap> countData2(String type,String server,String start,String end,String channel)throws GmException{
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Set<String> pids=new HashSet<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(server,type,t);
            GmHashMap r=new GmHashMap();
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            r.put("logTime",t);
            for(GmHashMap map:list){
                pids.add(map.get("pid").toString());
            }
            r.put("num",pids.size());
            r.put("fre",list.size());
            res.add(r);
            pids.clear();
        }
        return res;
    }
    private List<GmHashMap> getLOGIN_TIME(String sid,String start,String end,String channel) throws GmException {
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Map<String,Long> dataLog=new HashMap<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(sid,DataType.OUT,t);
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            for(GmHashMap map:list){
                Long ol=dataLog.get(map.get("pid").toString());
                if(ol==null){
                    ol=0L;
                }
                ol+=((long)map.get("dt")-(long)map.get("lgnDt"));
                dataLog.put(map.get("pid").toString(),ol);
            }
            long all=0L;
            for (Long p:dataLog.values()){
                all+=p;
            }
            GmHashMap meta=new GmHashMap();
            meta.put("logTime",t);
            meta.put("dau",dataLog.size());
            if(all == 0){
                meta.put("olTime",0);
            }else {
                meta.put("olTime",all/(dataLog.size()*60000));
            }
            res.add(meta);
            dataLog.clear();
        }
        return res;
    }

    private List<GmHashMap> countData4(String type,String server,String start,String end,String channel)throws GmException{
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Map<String,GmHashMap> rMap=new HashMap<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(server,type,t);
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            System.out.println("查询结果："+list);
            for(GmHashMap map:list){
                GmHashMap r=rMap.get(map.get("cid").toString());
                if(r==null){
                    r=new GmHashMap();
                    r.put("logTime",t);
                    r.put("cid",map.get("cid"));
                    r.put("fre",0);
                    r.put("numSet",new HashSet());
                }
                r.put("fre",(int)r.get("fre")+(int)map.get("num"));
                Set scm= (Set) r.get("numSet");
                scm.add(map.get("pid"));
                r.put("num",scm.size());
                rMap.put(map.get("cid").toString(),r);
            }
            res.addAll(rMap.values());
            rMap.clear();
        }
        return res;
    }

    private List<GmHashMap> countData3(String type,String server,String start,String end,String channel)throws GmException{
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Map<String,GmHashMap> rMap=new HashMap<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(server,type,t);
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            for(GmHashMap map:list){
                GmHashMap r=rMap.get(map.get("type").toString());
                if(r==null){
                    r=new GmHashMap();
                    r.put("logTime",t);
                    r.put("type",map.get("type"));
                    r.put("fre",0);
                    r.put("numSet",new HashSet());
                }
                r.put("fre",(int)r.get("fre")+1);
                Set scm= (Set) r.get("numSet");
                scm.add(map.get("pid"));
                r.put("num",scm.size());
                rMap.put(map.get("type").toString(),r);
            }
            res.addAll(rMap.values());
            rMap.clear();
        }
        return res;
    }


    private List<GmHashMap> queryMaxIndex(String type,String server,String start,String end,String channel)throws GmException{
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Set<String> actor=new HashSet<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(server,type,t);
            GmHashMap r=new GmHashMap();
            r.put("logTime",t);
            r.put("maxLevel",1);
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            for(GmHashMap map:list){
                actor.add(map.get("pid").toString());
                if((int)r.get("maxLevel")<(int)map.get("index")){
                    r.put("maxLevel",map.get("index"));
                }
            }
            r.put("actor",actor.size());
            actor.clear();
            res.add(r);
        }
        return res;
    }

    private void setMapForTeach(String key,String value,Map<String,Set<String>> map){
        Set<String> j= map.get(key);
        if(j==null){
            j=new HashSet<>();
        }
        j.add(value);
        map.put(key,j);
    }


    private List<GmHashMap> queryYLZ(String server,String start,String end,String channel) throws GmException {
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Map<String,GmHashMap> rMap=new HashMap<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(server,DataType.YH_FNISH,t);
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            for(GmHashMap map:list){
                String cid=map.get("cid").toString();
                GmHashMap rm=rMap.get(cid);
                if(rm==null){
                    rm=new GmHashMap();
                    rm.put("logTime",t);
                    rm.put("cid",cid);
                    rm.put("change",0);
                    rm.put("room",0);
                    rm.put("actor",0);
                    rm.put("breach",0);
                    rm.put("numSet",new HashSet<>());
                }
                rm.put("change",(Integer)rm.get("change")+(Integer)map.get("score"));
                rm.put("actor",(Integer)rm.get("actor")+(Integer)map.get("join"));
                rm.put("breach",(Integer)rm.get("breach")+(Integer)map.get("breach"));
                rm.put("room",(Integer)rm.get("room")+1);
                Set scm= (Set) rm.get("numSet");
                scm.add(map.get("pid"));
                rm.put("num",scm.size());
                rMap.put(cid,rm);
            }
            res.addAll(rMap.values());
            rMap.clear();
        }
        return res;
    }

    private List<GmHashMap> queryDataStatistics(String type,String start,String end,String server,String channel) throws GmException {

        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Map<String,GmHashMap> rMap=new HashMap<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(server,type,t);
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            for(GmHashMap map:list){
                String cmd=map.get("cmd").toString();
                GmHashMap rm=rMap.get(cmd);
                if(rm==null){
                    rm=new GmHashMap();
                    rm.put("date",t);
                    rm.put("cmdName",GmDocConfig.CMD.get(cmd));
                    rm.put("change",0);
                    rm.put("numSet",new HashSet<>());
                }
                rm.put("change",(Integer)rm.get("change")+(Integer)map.get("change"));
                Set scm= (Set) rm.get("numSet");
                scm.add(map.get("pid"));
                rm.put("num",scm.size());
                rMap.put(cmd,rm);
            }
            res.addAll(rMap.values());
            rMap.clear();
        }
        return res;
    }


    private List<GmHashMap> queryTeach(String server,String start,String end,String channel)throws GmException{
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Map<String,Set<String>> finish=new HashMap<>();
        Map<String,Set<String>> join=new HashMap<>();
        Set<String> ak=new HashSet<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(server,DataType.JJ,t);
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            for(GmHashMap map:list){
                if(map.get("type").toString().equals("InSeat")){
                    setMapForTeach(map.get("worker").toString(),map.get("pid").toString(),join);
                }else if (map.get("type").toString().equals("Finish")){
                    setMapForTeach(map.get("worker").toString(),map.get("pid").toString(),finish);
                }
                ak.add(map.get("worker").toString());
            }
            if (ak!=null&&ak.size()>0){
                for (String k:ak){
                    GmHashMap i_r=new GmHashMap();
                    i_r.put("logTime",t);
                    i_r.put("seat",k);
                   if (finish.get(k)!= null){
                       i_r.put("openNum",finish.get(k).size());
                   }else {
                       i_r.put("openNum",0);
                   }
                   if (join.get(k) != null){
                       i_r.put("actor",join.get(k).size());
                   }else {
                       i_r.put("actor",0);
                   }
                    res.add(i_r);
                }
            }
            finish.clear();
            join.clear();
        }
        return res;
    }

    private List<GmHashMap> querySchoolSuperMan(String server,String start,String end,String channel) throws GmException {
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        boolean isAllChannel=channel.equals("all");
        GmHashMap cnd=queryCnd("plf",channel);
        Set<String> actor=new HashSet<>();
        Set<String> player=new HashSet<>();
        for(String t:times){
            ReadFile readFile= new ReadFile(server,DataType.XB,t);
            GmHashMap r=new GmHashMap();
            r.put("logTime",t);
            r.put("createTeam",0);
            List<GmHashMap> list= isAllChannel?readFile.getMsgList():readFile.getMsg(cnd);
            for(GmHashMap map:list){
               if(map.get("type").equals("Create")){
                   r.put("createTeam",(int)r.get("createTeam")+1);
                   player.add(map.get("pid").toString());
               }else if(map.get("type").equals("Join")){
                   actor.add(map.get("pid").toString());
               }
            }
            r.put("createPlayer",player.size());
            r.put("actor",actor.size());
            actor.clear();
            player.clear();
            res.add(r);
        }
        return res;
    }

    private List<GmHashMap> queryTask(String server,String start,String end,String channel) throws GmException {
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        GmHashMap cnd=new GmHashMap();
        cnd.putAll(queryCnd("plf",channel));
        cnd.putAll(queryCnd("type",2));
        for(String t:times){
            List<GmHashMap> data= new ReadFile(server,DataType.TASK,t).getMsg(cnd);
            Map<String,GmHashMap> rMap=new HashMap<>();
            for(GmHashMap dt:data){
                GmHashMap rm=rMap.get(dt.get("cid").toString());
                if(rm==null){
                    rm=new GmHashMap();
                    rm.put("date",t);
                    rm.put("taskName",dt.get("pname"));
                    rm.put("task",dt.get("cid"));
                    rm.put("num",0);
                }
                rm.put("num",(int)rm.get("num")+1);
                rMap.put(dt.get("cid").toString(),rm);
            }
            res.addAll(rMap.values());
        }
        return res;
    }

    private List<GmHashMap> queryLeagueBoss(String sid,String start,String end,String gid) throws GmException {
        List<String> times=DateUtil.getEveryDayNoDurge(start,end);
        List<GmHashMap> res=new ArrayList<>();
        GmHashMap cnd=queryCnd("gName",gid);
        for(String t:times){
            List<GmHashMap> data= new ReadFile(sid,DataType.LEAGUE_BOSS,t).getMsg(cnd);
            Map<String,GmHashMap> rMap=new HashMap<>();
            for(GmHashMap dt:data){
                GmHashMap rm=rMap.get(dt.get("bCid").toString());
                if(rm==null){
                    rm=new GmHashMap();
                    rm.put("date",t);
                    rm.put("bid",dt.get("bCid"));
                    rm.put("openNum",0);
                    rm.put("endNum",0);
                }
                if(dt.get("type").toString().equals("OpenBoss")){
                    rm.put("openNum",(int)rm.get("openNum")+1);
                }else if(dt.get("type").toString().equals("KillBoss")){
                    rm.put("endNum",(int)rm.get("endNum")+1);
                }
                rMap.put(dt.get("bCid").toString(),rm);
            }
            res.addAll(rMap.values());
        }
        return res;
    }

    private List<GmHashMap> payMsg(String server,String start,String end,String name)throws GmException{
        GmOperate gm= GmRmiOperate.getGameRmiServer(this.dao.fetch(Parameter.class,"5").getPrams());
        if(gm==null){
            throw new GmException("本地GM RMI 无法连接!~");
        }
        String startTime=start.concat(" 00:00:00");
        String endTime=end.concat(" 23:59:59");
        long et=DateUtil.toDateTime(endTime).getTime();
        List<GmHashMap> resMsg=new ArrayList<>();
        List<Map<String,Object>> res ;
        try {
            if (name.length()>0){
                String pid=getPid(server,name);
                res= (List<Map<String,Object>>) gm.result("gm_query",10003,pid,DateUtil.toDateTime(startTime).getTime(),et);
                for(Map<String,Object> map:res){
                    map.put("name",name);
                    GmHashMap g=new GmHashMap();
                    g.putAll(map);
                    resMsg.add(g);
                }
                return resMsg;
            }
            Map<String,String> pid_name;
            if(server.equals("all")){
                List<Player> pls=this.dao.query(Player.class,Cnd.where("createTime","<=",endTime));
                List<GmHashMap> mls=new ArrayList<>();
                if(System.currentTimeMillis()<et){
                    String today= DateUtil.getDateString(new Date());
                    List<GameServer> sidList=this.dao.query(GameServer.class,null);
                    for(GameServer sid:sidList){
                        mls.addAll(new ReadFile(sid.getServerID(),DataType.PLAYER,today).getMsgList());
                    }
                }
                pid_name=convertPidAndName(pls,mls);
                res= (List<Map<String,Object>>) gm.result("gm_query",10002,DateUtil.toDateTime(startTime).getTime(),et);

            }else{
                List<Player> pls=this.dao.query(Player.class,Cnd.where("sid","=",server).and("createTime","<=",endTime));
                List<GmHashMap> mls=null;
                if(System.currentTimeMillis()<et){
                    mls=new ReadFile(server,DataType.PLAYER,DateUtil.getDateString(new Date())).getMsgList();
                }
                pid_name=convertPidAndName(pls,mls);
                res= (List<Map<String,Object>>) gm.result("gm_query",10001,server,DateUtil.toDateTime(startTime).getTime(),et);
            }
            for(Map<String,Object> map:res){
                map.put("name",pid_name.get(map.get("pid").toString()));
                GmHashMap g=new GmHashMap();
                g.putAll(map);
                resMsg.add(g);
            }
            res.clear();
        } catch (RemoteException e) {
           throw new GmException(e.getMessage());
        }
        return resMsg;
    }

    private Map<String, String> convertPidAndName(List<Player> pls,List<GmHashMap> mls) {
        Map<String, String> p_n=new HashMap<>();
        if(pls!=null){
            for (Player bean:pls){
                p_n.put(bean.getPid(),bean.getName());
            }
        }
        if(mls!=null){
            for(GmHashMap map:mls){
                p_n.put(map.get("pid").toString(),map.get("pname").toString());
            }
        }
        return p_n;
    }

    private List<GmHashMap> snapshotMsg(String sid,String start,String end){
        List<GmHashMap> msg=new ArrayList<>();
        Date td=new Date();
        String today=DateUtil.getDateString(td);
        List<GmHashMap> todayMsg=null;
        if(today.equals(end)){
            end=DateUtil.getDateString(DateUtil.getDaytoDay(td,-1));
            todayMsg=new ArrayList<>();
            Set<String> keys=redisService.keys("rpMsg:"+today.concat(":*"));
            for(String key :keys){
                String res=redisService.get(key);
                List<String[]> ls=JSON.parseArray(res,String[].class);
                String[] t=key.split(":");
                String time=t[1]+" "+t[2]+":"+t[3]+":00";
                for(String[] r:ls){
                    if(r[0].equals(sid)){
                        GmHashMap map=new GmHashMap();
                        map.put("logTime",time);
                        map.put("createNum",r[1]);
                        map.put("loginCount",r[2]);
                        map.put("money",r[5]);
                        map.put("payNum",r[4]);
                        map.put("olNum",r[3]);
                        todayMsg.add(map);
                        break;
                    }
                }
            }
        }
        List<SnapshotBean> snapshotList=this.dao.query(SnapshotBean.class,Cnd.where("sid","=",sid)
                .and("logTime",">=",start.concat(" 00:00:00")).and("logTime","<=",end.concat(" 23:59:59")).asc("logTime"));
        for (SnapshotBean bean:snapshotList){
            GmHashMap map=new GmHashMap();
            map.put("logTime",bean.getTime());
            map.put("createNum",bean.getCreate());
            map.put("loginCount",bean.getLogin());
            map.put("money",bean.getPay());
            map.put("payNum",bean.getPayNum());
            map.put("olNum",bean.getOnline());
            msg.add(map);
        }
        if(todayMsg!=null&&!todayMsg.isEmpty()){
            msg.addAll(todayMsg);
        }
        return msg;
    }

    private List<GmHashMap> countMac(String start,String end){
        long st=DateUtil.toDateTime(start+" 00:00:00").getTime();
        long et=DateUtil.toDateTime(end+" 23:59:59").getTime();
        Sql sql = SqlUtil.getSql(GmSql.GET_MAC_COUNT,st,et,st,et,st,et);
        this.dao.execute(sql);
        return sql.getList(GmHashMap.class);
    }

    private List<GmHashMap> queryLvDistribution(String sid,String end,String cStart,String cEnd) throws GmException {
        List<GmHashMap> res =new ArrayList<>();
        List<String> createTime= DateUtil.getEveryDay(cStart,cEnd);
        List<String> dateList= DateUtil.getEveryDay(cStart,end);
        Set<String> pids=new HashSet<>();
        for(String ct:createTime){
            List<GmHashMap> playerList=new ReadFile(sid,DataType.PLAYER,ct).getMsgList();
            for(GmHashMap pt:playerList){
                pids.add(pt.get("pid").toString());
            }
        }
        GmHashMap player_lv =new GmHashMap();
        for(String lt:dateList){
            List<GmHashMap> lvList=new ReadFile(sid,DataType.LV,lt).getMsg();
            for(GmHashMap map:lvList){
                if(pids.contains(map.get("pid").toString())){
                    player_lv.put(map.get("pid").toString(),map.get("lv"));
                }
            }
        }
        Map<Object,Integer> lv_pid =new HashMap<>();
        for(String pid:player_lv.keySet()){
            Integer num=lv_pid.get(player_lv.get(pid));
            if(num==null){
                num=0;
            }
            num++;
            lv_pid.put(player_lv.get(pid),num);
        }
        for (Object key:lv_pid.keySet()){
            GmHashMap r=new GmHashMap();
            r.put("lv",key);
            r.put("num",lv_pid.get(key));
            res.add(r);
        }
        return res;
    }

    private List<GmHashMap> queryStepCount(String end,String sid,String uStart,String uEnd) throws GmException{
        System.out.println("接收的参数：end="+end+"==sid="+sid+"==uStart="+uStart+"==uEnd="+uEnd);
        if(DateUtil.getDistinceDay(uEnd,end)<0){
            return new ArrayList<>();
        }
        String startTime=uStart.concat(" 00:00:00");
        Date td=new Date();
        String toDay=DateUtil.getDateString(td);
        System.out.println("toDay:"+toDay);
        List<GmHashMap> pt=null;
        if(toDay.equals(uEnd)){
            uEnd=DateUtil.getDateString(DateUtil.getDaytoDay(td,-1));
//            System.out.println("if里面的uEnd:"+uEnd);
            pt=new ReadFile(sid,DataType.PLAYER,toDay).getMsg();
//            System.out.println("pt:"+pt);
        }
        String endTime=uEnd.concat(" 23:59:59");
        List<Player> newPlayer=this.dao.query(Player.class,
                Cnd.where("sid","=",sid).and("createTime",">=",startTime).and("createTime","<=",endTime));
//        System.out.println("newPlayer:"+newPlayer);
        Set<String> pid=new HashSet<>();
        for (Player p:newPlayer){
            pid.add(p.getPid());
        }
        newPlayer.clear();
        if(pt!=null&&!pt.isEmpty()){
            for(GmHashMap p:pt){
                pid.add(p.get("pid").toString());
            }
            pt.clear();
        }
        List<GmHashMap> playerStepAndDup=new ReadFile(sid,DataType.STEP,uStart,end).getMsg();
        List<GmHashMap> playerStep=new ArrayList<>();
        for(GmHashMap ps :playerStepAndDup){
            if(GmDocConfig.STEP.containsKey(ps.get("sId").toString())&&pid.contains(ps.get("pid").toString())){
                playerStep.add(ps);
            }
        }
        pid.clear();
        playerStepAndDup.clear();
        Map<String,Integer> count=new HashMap<>();
        for(GmHashMap cps:playerStep){
            Integer i=count.get(cps.get("sId").toString());
            if(i==null){
                i=0;
            }
            i=i+1;
            count.put(cps.get("sId").toString(),i);
        }
        int index;
        GmHashMap[] allStep=new GmHashMap[GmDocConfig.STEP.size()];
        for(String k:count.keySet()){
            GmHashMap t=new GmHashMap();
            t.put("proc",k);
            t.put("dsp",GmDocConfig.STEP.get(k)[0]);
            t.put("num",count.get(k));
            index=Integer.parseInt(GmDocConfig.STEP.get(k)[1]);
            allStep[index]=t;
        }
        for(int i=0,j=1,k=0;i<allStep.length;i++){
            if(allStep[i]==null){
                j++;
                continue;
            }
            if(i==0){
                allStep[i].put("lose",0);
            }else{
                if(k==0){
                    allStep[i].put("lose",0);
                }else{
                    allStep[i].put("lose",((int)allStep[i-j].get("num"))-((int)allStep[i].get("num")));
                }
            }
            k++;
            j=1;
        }
        return Arrays.asList(allStep);
    }
}
