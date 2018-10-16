package com.gmtime.gm.service.other.imp;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.gmdesign.exception.GmException;
import com.gmtime.cache.GmDataConnectionCache;
import com.gmtime.gm.dao.OperateDBIF;
import com.gmtime.gm.service.other.GameDataServiceIF;
import com.gmtime.gm.sql.impl.PlatformSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by DJL on 2017/12/20.
 *
 * @ClassName GM
 * @Description
 */
@Service("gameDataService")
public class GameDataService implements GameDataServiceIF {

    private final OperateDBIF operateDB;

    @Autowired
    public GameDataService(@Qualifier("operateDB") OperateDBIF operateDB) {
        this.operateDB = operateDB;
    }

    @Override
    public List<Map<String,Object>> queryPayMsg(String sid,long start,long end) throws GmException {
        Connection pay=null;
        try {
            pay=  GmDataConnectionCache.getConnection("platform_deliver");
            return this.operateDB.findOtherDB(pay,PlatformSql.QUERY_PAY_DATA_1,sid,start,end);
        }finally {
            GmDataConnectionCache.closeConnection(pay);
        }
    }

    @Override
    public List<Map<String,Object>> queryPayMsg(long start,long end) throws GmException {
        Connection pay=null;
        try {
            pay=  GmDataConnectionCache.getConnection("platform_deliver");
            return this.operateDB.findOtherDB(pay,PlatformSql.QUERY_PAY_DATA_2,start,end);
        }finally {
            GmDataConnectionCache.closeConnection(pay);
        }
    }

    @Override
    public List<Map<String,Object>> queryPayMsgForPid(String pid,long start,long end) throws GmException {
        Connection pay=null;
        try {
            pay=  GmDataConnectionCache.getConnection("platform_deliver");
            return this.operateDB.findOtherDB(pay,PlatformSql.QUERY_PAY_DATA_3,pid,start,end);
        }finally {
            GmDataConnectionCache.closeConnection(pay);
        }
    }
}
