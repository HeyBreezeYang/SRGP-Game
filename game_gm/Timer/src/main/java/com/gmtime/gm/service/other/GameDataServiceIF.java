package com.gmtime.gm.service.other;

import java.util.List;
import java.util.Map;

import com.gmdesign.exception.GmException;

/**
 * Created by DJL on 2017/12/20.
 *
 * @ClassName GM
 * @Description
 */
public interface GameDataServiceIF {
    List<Map<String,Object>> queryPayMsg(String sid,long start,long end)throws GmException;
    List<Map<String,Object>> queryPayMsg(long start,long end)throws GmException;
    List<Map<String,Object>> queryPayMsgForPid(String pid,long start,long end)throws GmException;

}
