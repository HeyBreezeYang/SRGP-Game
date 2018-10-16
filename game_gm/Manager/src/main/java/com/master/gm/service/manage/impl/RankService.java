package com.master.gm.service.manage.impl;

import com.alibaba.fastjson.JSONObject;
import com.master.gm.BaseService;
import com.master.gm.service.manage.RankServiceIF;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.*;

/**
 * Created by HP on 2018/7/5.
 */
@IocBean
public class RankService extends BaseService implements RankServiceIF {

    @Inject
    private RedisService redisService;

    @Override
    public Map getAllRank(String type,String queryDate) {
        Map allRank = new HashMap();
        List rank22 = JSONObject.parseObject(redisService.get("rank-"+type+"-22-"+queryDate),List.class);
        allRank.put("rank22",rank22);
        List rank24 = JSONObject.parseObject(redisService.get("rank-"+type+"-24-"+queryDate),List.class);
        allRank.put("rank24",rank24);
        return allRank;
    }

}
