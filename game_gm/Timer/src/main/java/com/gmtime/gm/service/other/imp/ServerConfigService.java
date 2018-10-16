package com.gmtime.gm.service.other.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gmdesign.exception.GmException;
import com.gmtime.gm.dao.OperateDBIF;
import com.gmtime.gm.dao.bean.DataBaseConfig;
import com.gmtime.gm.service.other.ServerConfigServiceIF;
import com.gmtime.gm.sql.impl.ServerConfigSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by DJL on 2016/12/2.
 *
 * @ClassName ServerConfigService
 * @Description
 */
@Service("serverConfigService")
public class ServerConfigService implements ServerConfigServiceIF {
    private final OperateDBIF operateDB;

    @Autowired
    public ServerConfigService(@Qualifier("operateDB") OperateDBIF operateDB) {
        this.operateDB = operateDB;
    }


    @Override
    public List<String> initChanel() throws GmException {
        List<String> channel=new ArrayList<>();
        List<Map<String,Object>> list=operateDB.queryResultForLocal(ServerConfigSql.QUERY_CHANNEL);
        for (Map<String, Object> aList : list) {
            channel.add(aList.get("cname").toString());
        }
        return channel;
    }

    @Override
    public List<String> initServer() throws GmException {
        List<String> server =new ArrayList<>();
        List<Map<String,Object>> list=operateDB.queryResultForLocal(ServerConfigSql.QUERY_SERVER);
        for (Map<String, Object> aList : list) {
            if (aList != null && aList.size()>0){
                server.add(aList.get("serverID").toString());
            }
        }
        return server;
    }

    @Override
    public List<DataBaseConfig> getDBConfig() throws GmException {
        List<DataBaseConfig> configs = new ArrayList<>();
        List<Map<String, Object>> list = operateDB.queryResultForLocal(ServerConfigSql.QUERY_DB);
        for (Map<String, Object> aList : list) {
            configs.add(new DataBaseConfig(aList));
        }
        return configs;
    }
}
