package com.master.gm.service.currency.impl;

import java.util.*;

import com.gmdesign.bean.other.GmHashMap;
import com.master.bean.back.CheckpointData;
import com.master.gm.bean.GmSql;
import com.master.gm.service.currency.StatisticalBody;
import com.master.util.SqlUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Sql;

/**
 * Created by DJL on 2018/4/11.
 *
 * @ClassName gm
 * @Description
 */
public class CheckpointStatistics extends StatisticalBody{
    private static CheckpointStatistics ms =null;
    private CheckpointStatistics(){}
    static CheckpointStatistics createCount(){
        if(ms==null){
            ms=new CheckpointStatistics();
        }
        return ms;
    }
    @Override
    public Collection<GmHashMap> countToday(List<GmHashMap> res,String todayString) {
        Map<String,GmHashMap> data=new HashMap<>();
        Map<String,Set<String>> pn=new HashMap<>();
        for(GmHashMap r:res){
            String key =r.get("chapter").toString()+"-"+r.get("part").toString();
            GmHashMap d=data.get(key);
            if(d==null){
                d=new GmHashMap();
                d.put("date",todayString);
                d.put("task",key);
                d.put("passFre",0);
                pn.put(key,new HashSet<>());
            }
            d.put("passFre",(int)d.get("passFre")+1);
            pn.get(key).add(r.get("pid").toString());
            data.put(key,d);
        }
        for (String k:data.keySet()){
            data.get(k).put("passNum",pn.get(k).size());
        }
        return data.values();
    }

    @Override
    public void countDbOne(List<GmHashMap> count,Dao dao,GmHashMap cnd,String... param) {
        List<CheckpointData> list= dao.query(CheckpointData.class,Cnd.where("logTime",">=",param[0]).and("logTime","<=",param[1])
                .and("channel","=",param[2]).and("sid","=",param[3]));
        for(CheckpointData bean:list){
            GmHashMap data =new GmHashMap();
            data.put("date",bean.getLogTime());
            data.put("task",bean.getTask());
            data.put("passNum",bean.getPassNum());
            data.put("passFre",bean.getPassFre());
            count.add(data);
        }
    }

    @Override
    public void countDbTwo(List<GmHashMap> count,Dao dao,GmHashMap cnd,String... param) {
        Sql sql = SqlUtil.getSql(GmSql.QUERY_CPT,param[0],param[1],param[2]);
        dao.execute(sql);
        count.addAll(sql.getList(GmHashMap.class));
    }
}
