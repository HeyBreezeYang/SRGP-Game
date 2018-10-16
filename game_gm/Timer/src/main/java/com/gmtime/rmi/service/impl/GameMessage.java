package com.gmtime.rmi.service.impl;

import com.gmdesign.exception.GmException;
import com.gmtime.cache.context.SpringContext;
import com.gmtime.gm.service.other.GameDataServiceIF;
import com.gmtime.rmi.service.RmiResult;

/**
 * Created by DJL on 2017/12/20.
 *
 * @ClassName GM
 * @Description
 */
public class GameMessage implements RmiResult {

    private GameDataServiceIF service=(GameDataServiceIF) SpringContext.getBean("gameDataService");
    private static final int PAY_1=10001;
    private static final int PAY_2=10002;
    private static final int PAY_3=10003;
    @Override
    public Object rmiResult(int code,Object... prams) throws GmException {
        switch (code) {
            case PAY_1:
                return service.queryPayMsg(prams[0].toString(),(long)prams[1],(long)prams[2]);
            case PAY_2:
                return service.queryPayMsg((long)prams[0],(long)prams[1]);
            case PAY_3:
                return service.queryPayMsgForPid(prams[0].toString(),(long)prams[1],(long)prams[2]);
            default:
                return null;
        }
    }
}
