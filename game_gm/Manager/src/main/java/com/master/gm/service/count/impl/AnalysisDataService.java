package com.master.gm.service.count.impl;

import java.util.*;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.master.gm.BaseService;
import com.master.gm.service.count.AnalysisDataServiceIF;
import com.master.util.SqlUtil;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/10/20.
 *
 * @ClassName AnalysisDataService
 * @Description
 */
@IocBean
public class AnalysisDataService extends BaseService implements AnalysisDataServiceIF {


    private void setData(Map<Integer,GmHashMap> source,int hour,int num,String time){
        GmHashMap d=source.get(hour);
        if(d==null){
            d=new GmHashMap();
            d.put("hour",String.valueOf(hour));
        }
        d.put(time,num);
        source.put(hour,d);
    }
    private String createColor(){
        String red;
        String green;
        String blue;
        Random random = new Random();
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();
        red = red.length()==1 ? "0" + red : red ;
        green = green.length()==1 ? "0" + green : green ;
        blue = blue.length()==1 ? "0" + blue : blue ;
        return "#"+red+green+blue;
    }

    private void setField(Map<String,GmHashMap> dataField,String time){
        if(!dataField.containsKey(time)){
            GmHashMap d=new GmHashMap();
            d.put("valueField",time);
            d.put("name",time);
            d.put("color",createColor());
            dataField.put(time,d);
        }
    }

    @Override
    public GmHashMap queryOnlineNum(String sid,String start,String end) throws GmException {

        GmHashMap res =new GmHashMap();
//        res.put("maxSource",maxDataSource.values());
//        res.put("minSource",minDataSource.values());
//        res.put("dataField",dataField.values());
        return res;
    }

    private String ltvSql(String tb,int sIndex,int eIndex){
        StringBuilder sb =new StringBuilder(128);
        sb.append("select logTime,SUM(newPlayer) newPlayer");
        for(int i=sIndex;i<=eIndex;i++){
            sb.append(",SUM(t").append(i).append(") t").append(i);
        }
        sb.append(" from gm_back.").append(tb).append(" where sid=@sid and logTime>=@start and logTime<=@end GROUP BY logTime order by logTime;");
        return sb.toString();
    }

    @Override
    public GmHashMap queryDetailedLTV(String sid,String start,String end,int sIndex,int eIndex){
        String[] pk={"sid","start","end"};
        Object[] p={sid,start,end};
        List<String> col=new ArrayList<>();
        col.add("logTime");
        col.add("newPlayer");
        for(int i=sIndex;i<=eIndex;i++){
            col.add("t"+String.valueOf(i));
        }
        Sql ltv= SqlUtil.getSql(ltvSql("ltv",sIndex,eIndex),pk,p,col);
        Sql ltv_num= SqlUtil.getSql(ltvSql("ltv_num",sIndex,eIndex),pk,p,col);
        this.dao.execute(ltv);
        List<GmHashMap> ltvList=ltv.getList(GmHashMap.class);
        System.out.println("ltvList:"+Arrays.toString(ltvList.toArray()));
        this.dao.execute(ltv_num);
        List<GmHashMap> ltvNumList=ltv_num.getList(GmHashMap.class);
        GmHashMap res =new GmHashMap();
        for(int i=0;i<ltvList.size();i++){
            GmHashMap l=ltvList.get(i);
            GmHashMap ln=ltvNumList.get(i);
            List<GmHashMap> r=new ArrayList<>();
            for(int j=sIndex;j<=eIndex;j++){
                String pt="t"+String.valueOf(j);
                if(ln.get(pt)==null){
                    continue;
                }
                GmHashMap rm=new GmHashMap();
                rm.put("cTime",l.get("logTime"));
                rm.put("cNum",l.get("newPlayer"));
                rm.put("pTime",pt);
                rm.put("pNum",ln.get(pt));
                rm.put("pay",l.get(pt));
                r.add(rm);
            }
            res.put(l.get("logTime").toString(),r);
        }
        System.out.println("res:"+res);
        return res;
    }
}
