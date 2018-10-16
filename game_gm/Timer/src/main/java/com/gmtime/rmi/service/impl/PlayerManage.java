package com.gmtime.rmi.service.impl;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmtime.cache.CacheTimer;
import com.gmtime.rmi.service.RmiResult;

/**
 * Created by DJL on 2017/11/18.
 *
 * @ClassName GM
 * @Description
 */
public class PlayerManage implements RmiResult {
    private static final int ADD_BAN=10001;
    private static final int ADD_GAG=10002;

    @Override
    public Object rmiResult(int code,Object... prmas) throws GmException {
        switch (code) {
            case ADD_BAN:
                return setControl(prmas[0].toString(),prmas[1].toString(),(long)prmas[2],"ban");
            case ADD_GAG:
                return setControl(prmas[0].toString(),prmas[1].toString(),(long)prmas[2],"gag");
            default:
                return null;
        }
    }
    private boolean setControl(String sid,String name,long time,String key){
        GmHashMap job =new GmHashMap();
        job.put("name",name);
        job.put("server",sid);
        job.put(key,time);
        CacheTimer.getTaskObject("PlayerManageJob").add(job);
        return true;
    }
}
