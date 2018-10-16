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
 * Created by DJL on 2018/4/20.
 *
 * @ClassName gm
 * @Description
 */
public class Monetary extends QuartzObject{

    private CountLogServiceIF countLogService = (CountLogServiceIF) SpringContext.getBean("countLogService");
    @Override
    public void job(List<GmHashMap> objs) {
        String time = DateUtil.getDateString(DateUtil.getDaytoDay(new Date(),-1));
        try {
            countLogService.saveMonetary(time);
        } catch (GmException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
