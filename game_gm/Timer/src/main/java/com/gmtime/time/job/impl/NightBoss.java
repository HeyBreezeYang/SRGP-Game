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
 * Created by DJL on 2018/4/18.
 *
 * @ClassName gm
 * @Description
 */
public class NightBoss extends QuartzObject{
    private CountLogServiceIF countLogService = (CountLogServiceIF) SpringContext.getBean("countLogService");
    @Override
    public void job(List<GmHashMap> objs) {
        try {
            countLogService.saveNightBoss(DateUtil.getDateString(new Date()));
        } catch (GmException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
