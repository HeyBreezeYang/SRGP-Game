package com.master.gm.service.currency.impl;

import java.util.*;

import com.gmdesign.bean.other.GmHashMap;
import com.master.bean.back.ShopData;
import com.master.cache.GmDocConfig;
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
public class ShopStatistics extends StatisticalBody{

    private static ShopStatistics ms =null;
    private ShopStatistics(){}
    static ShopStatistics createCount(){
        if(ms==null){
            ms=new ShopStatistics();
        }
        return ms;
    }

    @Override
    public Collection<GmHashMap> countToday(List<GmHashMap> res,String todayString) {
        List<GmHashMap> list =new ArrayList<>();
        Map<String,Map<String,GmHashMap>> shop_gid_data=new HashMap<>();
        for(GmHashMap r:res){
            if(!r.get("type").equals("BUY")){
                continue;
            }
            Map<String,GmHashMap> gid_data=shop_gid_data.get(r.get("spType").toString());
            if(gid_data==null){
                gid_data=new HashMap<>();
            }
            GmHashMap data=gid_data.get(r.get("cid").toString());
            if(data==null){
                data=new GmHashMap();
                data.put("date",todayString);
                data.put("shopType",r.get("spType").toString());
                data.put("goodsName",GmDocConfig.GOODS.get(r.get("cid").toString()));
                data.put("payNum",new HashSet<>());
                data.put("frequency",0);
                data.put("num",0);
            }
            ((Set)data.get("payNum")).add(r.get("pid"));
            data.put("num",(int)data.get("num")+(int)r.get("value"));
            data.put("frequency",(int)data.get("frequency")+1);
            gid_data.put(r.get("cid").toString(),data);
            shop_gid_data.put(r.get("spType").toString(),gid_data);
        }
        Set<String> key1=shop_gid_data.keySet();
        for(String k1:key1){
            Map<String,GmHashMap> gd= shop_gid_data.get(k1);
            for (String k2:gd.keySet()){
                GmHashMap data=gd.get(k2);
                data.put("payNum",((Set)data.get("payNum")).size());
                list.add(data);
            }
        }
        shop_gid_data.clear();
        return list;
    }

    @Override
    public void countDbOne(List<GmHashMap> count,Dao dao,GmHashMap cnd,String... param) {
        List<ShopData> list= dao.query(ShopData.class,Cnd.where("logTime",">=",param[0]).and("logTime","<=",param[1])
                .and("channel","=",param[2]).and("sid","=",param[3]));
        for(ShopData bean:list){
            GmHashMap data =new GmHashMap();
            data.put("date",bean.getDate());
            data.put("shopType",bean.getType());
            data.put("goodsName",GmDocConfig.GOODS.get(bean.getGid()));
            data.put("payNum",bean.getPayNum());
            data.put("frequency",bean.getFrequency());
            data.put("num",bean.getNum());
            count.add(data);
        }
    }

    @Override
    public void countDbTwo(List<GmHashMap> count,Dao dao,GmHashMap cnd,String... param) {
        Sql sql = SqlUtil.getSql(GmSql.QUERY_GOODS,param[0],param[1],param[2]);
        dao.execute(sql);
        List<GmHashMap> list=sql.getList(GmHashMap.class);
        for (GmHashMap g:list){
            g.put("goodsName",GmDocConfig.GOODS.get(g.get("goods").toString()));
            count.add(g);
        }
    }
}
