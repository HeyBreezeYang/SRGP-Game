package com.gmtime.gm.service.statistice;

import java.util.Set;

import com.gmdesign.exception.GmException;
import com.gmtime.cache.context.SpringContext;
import com.gmtime.gm.dao.OperateDBIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by DJL on 2016/10/26.
 *
 * @ClassName DataAnalysisBack
 * @Description 综合数据统计
 */
public abstract class DataAnalysisBack {

    protected OperateDBIF operateDB=(OperateDBIF) SpringContext.getBean("operateDB");

    protected Logger log = LoggerFactory.getLogger(DataAnalysisBack.class);


    protected String time;

    public DataAnalysisBack(String time){
        this.time=time;
    }

    public abstract  void computer(Set<String> server,Set<String> chan) throws GmException;

}
