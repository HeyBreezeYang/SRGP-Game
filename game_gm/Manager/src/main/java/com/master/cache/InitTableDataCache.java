package com.master.cache;

import java.util.ArrayList;
import java.util.List;

import com.gmdesign.util.DateUtil;

/**
 * Created by DJL on 2017/4/29.
 *
 * @ClassName InitTableDataCache
 * @Description 主界面报表缓存
 */
public class InitTableDataCache {
    private static final int DAY_S=86400000;
    private static final List<String[]> report =new ArrayList<>();
    private static final List<String[]> rate =new ArrayList<>();
    private static final List<String[]> nowData =new ArrayList<>();

    public static void clearAll(){
        report.clear();
        rate.clear();
        nowData.clear();
    }

    public static boolean judge(List<String[]> msg){
        if(msg.isEmpty()){
            return true;
        }
        String date=msg.get(msg.size()-1)[0];
        return System.currentTimeMillis()- DateUtil.toDate(date).getTime()-DAY_S>DAY_S;
    }
    public static void setReport(List<String[]> msg){
        report.clear();
        report.addAll(msg);
    }
    public static void setRate(List<String[]> msg){
        rate.clear();
        rate.addAll(msg);
    }
    public static void setNowData(List<String[]> msg){
        nowData.clear();
        nowData.addAll(msg);
    }

    public static List<String[]> getRate() {
        return rate;
    }

    public static List<String[]> getReport() {
        return report;
    }

    public static List<String[]> getNowData() {
        return nowData;
    }
}
