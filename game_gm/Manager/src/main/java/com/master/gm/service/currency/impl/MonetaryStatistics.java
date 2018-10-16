package com.master.gm.service.currency.impl;

import java.util.*;

import com.gmdesign.bean.other.GmHashMap;
import com.master.bean.back.MonetaryStatisticsData;
import com.master.cache.GmDocConfig;
import com.master.gm.bean.GmSql;
import com.master.gm.service.currency.StatisticalBody;
import com.master.util.SqlUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Sql;

/**
 * Created by DJL on 2018/3/30.
 *
 * @ClassName gm
 * @Description
 */
public class MonetaryStatistics extends StatisticalBody{

    private static MonetaryStatistics ms =null;
    private MonetaryStatistics(){}
    static MonetaryStatistics createCount(){
        if(ms==null){
            ms=new MonetaryStatistics();
        }
        return ms;
    }

    @Override
    public Collection<GmHashMap> countToday(List<GmHashMap> res,String todayString) {
        Map<String,GmHashMap> resForMap=new HashMap<>();
        String cmd;
        for(GmHashMap r:res){
            cmd=r.get("cmd").toString();
            GmHashMap cm =resForMap.get(cmd);
            if(cm==null){
                cm=new GmHashMap();
                cm.put("date",todayString);
                cm.put("change",0);
                cm.put("numSet",new HashSet<>());
                cm.put("cmdName",GmDocConfig.CMD.get(cmd));
            }
            cm.put("change",(Integer)cm.get("change")+(Integer)r.get("change"));
            Set scm= (Set) cm.get("numSet");
            scm.add(r.get("pid"));
            cm.put("num",scm.size());
            resForMap.put(cmd,cm);
        }
        return resForMap.values();
    }

    @Override
    public void countDbOne(List<GmHashMap> count,Dao dao,GmHashMap cnd,String... param) {

        List<MonetaryStatisticsData> list= dao.query(MonetaryStatisticsData.class,Cnd.where("logTime",">=",param[0]).and("logTime","<=",param[1])
                .and("channel","=",param[2]).and("sid","=",param[3]).and("typeC","=",cnd.get("type")));
        for(MonetaryStatisticsData bean:list){
            GmHashMap cm =new GmHashMap();
            cm.put("date",bean.getTime());
            cm.put("change",bean.getCurrencyNum());
            cm.put("num",bean.getPeopleNum());
//            cm.put("cmdName",GmDocConfig.CMD.get(bean.getUseType()));
            count.add(cm);
        }
    }

    @Override
    public void countDbTwo(List<GmHashMap> count,Dao dao,GmHashMap cnd,String... param) {
        for (int i =0;i<param.length;i++){
            System.out.println("param:"+param[i]);
        }
        Sql sql = SqlUtil.getSql(GmSql.QUERY_MONETARY,param[0],param[1],param[2],cnd.get("type"));
        dao.execute(sql);
        List<GmHashMap> list=sql.getList(GmHashMap.class);
        for (GmHashMap g:list){
            g.put("cmdName",GmDocConfig.CMD.get(g.get("useType").toString()));
            count.add(g);
        }
    }
}
