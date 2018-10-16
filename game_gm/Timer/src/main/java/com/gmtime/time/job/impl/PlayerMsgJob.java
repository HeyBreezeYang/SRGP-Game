package com.gmtime.time.job.impl;

import java.util.Date;
import java.util.List;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmtime.cache.context.SpringContext;
import com.gmtime.gm.service.statistice.CountLogServiceIF;
import com.gmtime.time.job.QuartzObject;

/**
 * Created by DJL on 2017/12/18.
 *
 * @ClassName GM
 * @Description
 */
public class PlayerMsgJob extends QuartzObject {
    private CountLogServiceIF countLogService = (CountLogServiceIF) SpringContext.getBean("countLogService");
    private static String date;
    private static String hour;
    private static long time;
    static {
        Date d=new Date();
        time=d.getTime()-6*60*60*1000;
        date= DateUtil.getDateString(d);
        hour= DateUtil.getDateHour(d);
        int m=Integer.parseInt(hour);
        if(m<0){
            m=0;
        }
        if(m<10){
            hour="0"+String.valueOf(m);
        }else{
            hour=String.valueOf(m);
        }
    }
    @Override
    public void job(List<GmHashMap> objs) {
        try {
            time=countLogService.savePlayerMsg(date,hour,time);
            int m=Integer.parseInt(hour)+1;
            if(m>23){
                m=0;
                Date d=new Date();
                date= DateUtil.getDateString(d);
            }
            if(m<10){
                hour="0"+String.valueOf(m);
            }else{
                hour=String.valueOf(m);
            }
        } catch (GmException e) {
            e.printStackTrace();
        }
    }
}
