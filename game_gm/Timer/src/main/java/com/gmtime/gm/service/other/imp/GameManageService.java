package com.gmtime.gm.service.other.imp;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.SendUtil;
import com.gmtime.gm.dao.OperateDBIF;
import com.gmtime.gm.service.other.GameManageServiceIF;
import com.gmtime.gm.sql.impl.ServerConfigSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by DJL on 2016/11/23.
 *
 * @ClassName gm
 * @Description
 */
@Service("gameManageService")
public class GameManageService implements GameManageServiceIF {
    private final OperateDBIF operateDB;

    @Autowired
    public GameManageService(@Qualifier("operateDB") OperateDBIF operateDB) {
        this.operateDB = operateDB;
    }



    @Override
    public Map relieve(String name,String sid,int method) throws GmException {
        String param="/resetPlayerState?playerName="+name+"&state="+method;
        String url=this.operateDB.queryResultForLocal(ServerConfigSql.QUERY_PARAMETER_TWO,sid).get(0).get("prams").toString();
        try {
            Map res= JSON.parseObject(SendUtil.sendHttpMsg(url.concat(param),null),Map.class);
//            this.operateDB.OperateTable(BackSql.DEL_BAN,sid,name,method);
            return  res;
        } catch (Exception e) {
            throw new GmException(e.getMessage());
        }
    }
}
