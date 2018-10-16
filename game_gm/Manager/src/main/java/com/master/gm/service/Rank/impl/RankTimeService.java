package com.master.gm.service.Rank.impl;

import com.alibaba.fastjson.JSON;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.SendUtil;
import com.master.bean.dispoly.GameServer;
import com.master.gm.BaseService;
import com.master.gm.service.Rank.RankTimeServiceIF;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by HP on 2018/7/5.
 */
@IocBean
public class RankTimeService extends BaseService implements RankTimeServiceIF {
    @Inject
    private RedisService redisService;
    @Override
    public void getGameRankOutFile(String server,Integer type) throws GmException {

        String url= this.dao.fetch(GameServer.class, Cnd.where("serverID","=",server)).getServerUrl().concat("/getRank");

        //势力排行榜
        GmHashMap orgRes= JSON.parseObject(SendUtil.sendHttpMsg(url,"rankType=1&start=0&end=100"),GmHashMap.class) ;
        String orgKey = "rank-org-"+type+"-"+DateUtil.getCurrentTimeMillisToString(System.currentTimeMillis(),"yyyy-MM-dd");
        System.out.println("势力排行榜key:"+orgKey);
        long orgResult = redisService.append(orgKey,orgRes.get("ret").toString());
        System.out.println("势力排行榜redis存储返回"+orgResult);
        //关卡排行榜
        GmHashMap cardRes= JSON.parseObject(SendUtil.sendHttpMsg(url,"rankType=2&start=0&end=100"),GmHashMap.class) ;
        String cardKey = "rank-card-"+type+"-"+DateUtil.getCurrentTimeMillisToString(System.currentTimeMillis(),"yyyy-MM-dd");
        System.out.println("关卡排行榜key:"+cardKey);
        long cardResult = redisService.append(cardKey,cardRes.get("ret").toString());
        System.out.println("关卡排行榜redis存储返回"+cardResult);
        //亲密排行榜
        GmHashMap colseRes= JSON.parseObject(SendUtil.sendHttpMsg(url,"rankType=3&start=0&end=100"),GmHashMap.class) ;
        String closeKey = "rank-close-"+type+"-"+DateUtil.getCurrentTimeMillisToString(System.currentTimeMillis(),"yyyy-MM-dd");
        System.out.println("亲密排行榜key:"+closeKey);
        long closeResult = redisService.append(closeKey,colseRes.get("ret").toString());
        System.out.println("亲密排行榜redis存储返回"+closeResult);
    }
}
