package com.gmtime.gm.service.other;

import java.util.List;

import com.gmdesign.exception.GmException;
import com.gmtime.gm.dao.bean.DataBaseConfig;

/**
 * Created by DJL on 2016/12/2.
 *
 * @ClassName ServerConfigServiceIF
 * @Description
 */
public interface ServerConfigServiceIF {
     List<String> initChanel()throws GmException;
     List<String> initServer()throws GmException;
     List<DataBaseConfig> getDBConfig() throws GmException;
}
