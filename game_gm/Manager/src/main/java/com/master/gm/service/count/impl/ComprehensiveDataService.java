package com.master.gm.service.count.impl;

import java.util.*;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.master.bean.ComputerTableBean;
import com.master.bean.back.BaseData;
import com.master.bean.back.ComprehensiveBean;
import com.master.gm.BaseService;
import com.master.gm.service.count.ComprehensiveDataServiceIF;
import com.master.util.SqlUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/7/21.
 *
 * @ClassName ComprehensiveDataService
 * @Description
 */
@IocBean
public class ComprehensiveDataService extends BaseService implements ComprehensiveDataServiceIF{

    private Cnd baseCnd(String sid, String start, String end, String platform){
        Cnd condition=Cnd.where("logTime",">=",start).and("logTime","<=",end);
        if(!sid.equals("all")&&!sid.equals("all_count")){
            condition.and("sid","=",sid);
        }
        if(!platform.equals("all")&&!platform.equals("all_count")){
            condition.and("platform","=",platform);
        }
        condition.asc("logTime");
        return condition;
    }

    @Override
    public List<Object[]> Comprehensive(String sid, String start, String end, String platform) throws GmException {
        List<Object[]> msg=new ArrayList<>();
        Cnd condition=baseCnd(sid,start,end,platform);
        List<ComprehensiveBean> beanList=this.dao.query(ComprehensiveBean.class, condition);
        if(sid.equals("all_count")){
            beanList=countAllSid(beanList);
        }
        if(platform.equals("all_count")){
            beanList=countAllPlatform(beanList);
        }
        for(ComprehensiveBean bean :beanList){
            msg.add(bean.toObjectArray());
        }
        return msg;
    }
    private List<ComprehensiveBean> countAllPlatform(List<ComprehensiveBean> beanList){
        Map<String,ComprehensiveBean> resMap=new HashMap<>();
        String key;
        for(ComprehensiveBean bean:beanList){
            key=bean.getLogTime()+bean.getSid();
            ComprehensiveBean r= resMap.get(key);
            if(r==null){
                r=new ComprehensiveBean();
                r.setSid(bean.getSid());
                r.setLogTime(bean.getLogTime());
                r.setPlatform("all");
            }
            r.setCountAll(bean);
            resMap.put(key,r);
        }
        beanList.clear();
        return new ArrayList<>(resMap.values());
    }

    private List<ComprehensiveBean> countAllSid(List<ComprehensiveBean> beanList) {
        Map<String,ComprehensiveBean> resMap=new HashMap<>();
        String key;
        for(ComprehensiveBean bean:beanList){
            key=bean.getLogTime()+bean.getPlatform();
            ComprehensiveBean r= resMap.get(key);
            if(r==null){
                r=new ComprehensiveBean();
                r.setSid("all");
                r.setLogTime(bean.getLogTime());
                r.setPlatform(bean.getPlatform());
            }
            r.setCountAll(bean);
            resMap.put(key,r);
        }
        beanList.clear();
        return new ArrayList<>(resMap.values());
    }
    @Override
    public List<Object[]> LTVDataForMoney(String sid,String start,String end,String platform) throws GmException{
        Cnd condition=baseCnd(sid,start,end,platform);
        List<BaseData> beanList=this.dao.query(BaseData.class, condition);
        String sql ="select baseID,tDay,money from analysis_log.ltv where baseId in ("+beanList.get(0).getId();
        StringBuilder sb =new StringBuilder(64);
        Map<Object,Object[]> res =new HashMap<>();
        Object[] o_0=new Object[34];
        o_0[0]=beanList.get(0).getServer();
        o_0[1]=beanList.get(0).getPlatform();
        o_0[2]=beanList.get(0).getLogTime();
        o_0[3]=beanList.get(0).getNum();
        res.put(beanList.get(0).getId(),o_0);
        for(int i=1;i<beanList.size();i++){
            Object[] o_i=new Object[34];
            o_i[0]=beanList.get(i).getServer();
            o_i[1]=beanList.get(i).getPlatform();
            o_i[2]=beanList.get(i).getLogTime();
            o_i[3]=beanList.get(i).getNum();
            res.put(beanList.get(i).getId(),o_i);
            sb.append(",").append(beanList.get(i).getId());
        }
        sb.append(")");
        sql+=sb.toString();
        List<String> cls=new ArrayList<>();
        cls.add("baseId");
        cls.add("tDay");
        cls.add("money");
        Sql s=SqlUtil.getSql(sql,null,null,cls);
        this.dao.execute(s);
        List<GmHashMap> mapList=s.getList(GmHashMap.class);
        for(GmHashMap rm:mapList){
            Object[] obj=res.get(rm.get("baseId"));
            obj[3+(int) rm.get("tDay")]=rm.get("money");
        }
        if(sid.equals("all_count")){
            res=allSidForMap(res);
        }
        if(platform.equals("all_count")){
            res=allPlatformForMap(res);
        }
        return new ArrayList<>(res.values());
    }

    @Override
    public List<Object[]> LTVData(String sid, String start, String end, String platform) throws GmException {
        List<Object[]> msg=LTVDataForMoney(sid,start,end,platform);
        List<Object[]> lastRes=new ArrayList<>();
        for(Object[] objects:msg){
            Object[] obj =new Object[34];
            obj[0]=objects[0];
            obj[1]=objects[1];
            obj[2]=objects[2];
            obj[3]=objects[3];
            double pNum= (double) objects[3];
            for(int j=1;j<=30;j++){
                if(objects[j+3]==null){
                    continue;
                }
                double payOne= (double) objects[j+3];
                for(int h=j-1;h>=1;h--){
                    if(objects[h+3]==null){
                        continue;
                    }
                    payOne+=(double) objects[h+3];
                }
                obj[j+3]= ComputerTableBean.convertOne(payOne/pNum);
            }
            lastRes.add(obj);
        }
        msg.clear();
        return lastRes;
    }

    @Override
    public List<Object[]> LRRate(String sid, String start, String end, String platform, boolean lr) throws GmException {
        Cnd condition=baseCnd(sid,start,end,platform);
        List<BaseData> beanList=this.dao.query(BaseData.class, condition);
        if(beanList==null || beanList.isEmpty()){
            return Collections.emptyList();
        }
        String sql="select baseId,tDay,lossNum from analysis_log.loss_rate where baseId in ("+beanList.get(0).getId();
        StringBuilder sb =new StringBuilder(64);
        Map<Object,Object[]> res =new HashMap<>();
        Object[] o_0=new Object[33];
        o_0[0]=beanList.get(0).getServer();
        o_0[1]=beanList.get(0).getPlatform();
        o_0[2]=beanList.get(0).getLogTime();
        o_0[3]=beanList.get(0).getNum();
        res.put(beanList.get(0).getId(),o_0);
        for(int i=1;i<beanList.size();i++){
            Object[] o_i=new Object[33];
            o_i[0]=beanList.get(i).getServer();
            o_i[1]=beanList.get(i).getPlatform();
            o_i[2]=beanList.get(i).getLogTime();
            o_i[3]=beanList.get(i).getNum();
            res.put(beanList.get(i).getId(),o_i);
            sb.append(",").append(beanList.get(i).getId());
        }
        sb.append(")");
        sql+=sb.toString();
        List<String> cls=new ArrayList<>();
        cls.add("baseId");
        cls.add("tDay");
        cls.add("lossNum");
        Sql s=SqlUtil.getSql(sql,null,null,cls);
        this.dao.execute(s);
        List<GmHashMap> mapList=s.getList(GmHashMap.class);
        if(lr){
            for(GmHashMap rm:mapList){
                Object[] obj=res.get(rm.get("baseId"));
                obj[2+(int) rm.get("tDay")]=rm.get("lossNum");
            }
        }else {
            for(GmHashMap rm:mapList){
                Object[] obj=res.get(rm.get("baseId"));
                obj[2+(int) rm.get("tDay")]=(int)obj[3]-(int)rm.get("lossNum");
            }
        }
        if(sid.equals("all_count")){
            res=allSidForMap(res);
        }
        if(platform.equals("all_count")){
            res=allPlatformForMap(res);
        }
        List<Object[]> msg=new ArrayList<>(res.values());
        for(Object[] objects:msg){
            for(int i=4;i<33;i++){
                objects[i]= ComputerTableBean.convertTwo(rate(objects[i],objects[3]));
            }
        }
        return msg;
    }
    private double rate(Object a,Object b){
        double fz=a==null?0D: (double) a;
        double fm=(double)b;
        return fz/fm;
    }
    private void dataPlus(Object[] r,Object[] data){
        for(int i=3;i<r.length;i++){
            if(data[i]==null){
                continue;
            }
            if(r[i]==null){
                r[i]=0;
            }
            r[i]=(int)data[i]+(int)r[i];
        }
    }
    private Map<Object,Object[]> allPlatformForMap(Map<Object,Object[]> dataMap){
        Map<Object,Object[]> resMap=new HashMap<>();
        String key;
        Set mapKey=dataMap.keySet();
        Object[] c;
        for(Object k:mapKey){
            c=dataMap.get(k);
            key=c[2].toString()+c[0];
            Object[] r= resMap.get(key);
            if(r==null){
                r=new Object[c.length];
                r[1]="all";
                r[0]=dataMap.get(k)[0];
                r[2]=dataMap.get(k)[2];
            }
            dataPlus(r,c);
            resMap.put(key,r);
        }
        dataMap.clear();
        return resMap;
    }
    private Map<Object,Object[]> allSidForMap(Map<Object,Object[]> dataMap){
        Map<Object,Object[]> resMap=new HashMap<>();
        String key;
        Set mapKey=dataMap.keySet();
        Object[] c;
        for(Object k:mapKey){
            c=dataMap.get(k);
            key=c[2].toString()+c[1];
            Object[] r= resMap.get(key);
            if(r==null){
                r=new Object[c.length];
                r[0]="all";
                r[1]=dataMap.get(k)[1];
                r[2]=dataMap.get(k)[2];
            }
            dataPlus(r,c);
            resMap.put(key,r);
        }
        dataMap.clear();
        return resMap;
    }
}
