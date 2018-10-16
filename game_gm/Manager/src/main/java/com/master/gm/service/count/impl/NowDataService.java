package com.master.gm.service.count.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmdesign.exception.GmException;
import com.master.bean.dispoly.GameServer;
import com.master.gm.BaseService;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/8/11.
 *
 * @ClassName GM
 * @Description
 */
@IocBean
public class NowDataService extends BaseService{

    public Map<String,String> allServerUrl()throws GmException{
        Map<String,String> allUrl=new HashMap<>();
        List<GameServer> sidList=this.dao.query(GameServer.class,null);
        for(GameServer server:sidList){
            allUrl.put(server.getServerID(),server.getServerUrl().concat("/onlineSize"));
        }
        return allUrl;
    }
}
