package com.master.gm.service.count;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;

/**
 * Created by DJL on 2017/10/20.
 *
 * @ClassName GM
 * @Description
 */
public interface AnalysisDataServiceIF {
    GmHashMap queryOnlineNum(String sid,String start,String end)throws GmException;

    GmHashMap queryDetailedLTV(String sid,String start,String end,int sIndex,int eIndex);
}
