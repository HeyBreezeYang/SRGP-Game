package com.master.gm.service.manage;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import org.nutz.dao.entity.Record;

import java.util.List;

/**
 * Created by HP on 2018/6/12.
 */
public interface RealTimeMonitoringServiceIF {
    List<Record> getAllServer();

    GmHashMap queryRealTimeMonitoring(String sid, String startDate, String endDate) throws GmException;

    GmHashMap queryOnlineNum(String serverId, String startDate, String endDate) throws GmException;
}
