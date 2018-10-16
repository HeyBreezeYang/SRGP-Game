package com.gmtime.gm.service.statistice;

import java.util.Map;
import java.util.Set;

import com.gmdesign.exception.GmException;
import com.gmtime.cache.context.SpringContext;
import com.gmtime.gm.dao.OperateDBIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by DJL on 2017/8/14.
 *
 * @ClassName CountGameLogBack
 * @Description 游戏业务数据计算统计
 */
public abstract class CountGameLogBack {

    protected OperateDBIF operateDB=(OperateDBIF) SpringContext.getBean("operateDB");

    protected Logger log = LoggerFactory.getLogger(CountGameLogBack.class);

    protected String time;

    public  CountGameLogBack(String time){
        this.time=time;
    }

    public abstract  void computer(Set<String> server) throws GmException;

    protected int getAll(Map<String,Integer> numMap){
        int all=0;
        for(String k:numMap.keySet()){
            all+=numMap.get(k);
        }
        return all;
    }
}
