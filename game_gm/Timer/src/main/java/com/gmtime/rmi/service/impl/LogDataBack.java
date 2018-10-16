package com.gmtime.rmi.service.impl;

import com.gmdesign.exception.GmException;
import com.gmtime.cache.context.SpringContext;
import com.gmtime.gm.service.statistice.CountLogServiceIF;
import com.gmtime.rmi.service.RmiResult;

/**
 * Created by DJL on 2016/12/12.
 *
 * @ClassName LogDataBack
 * @Description 重新备份数据
 */


public class LogDataBack implements RmiResult {

    private CountLogServiceIF countLogService = (CountLogServiceIF) SpringContext.getBean("countLogService");

    private static final int COMPUTER_DAY=29;

    private static final int COMPREHENSIVE =10001;
    private static final int LOSS_RATE =10002;
    private static final int LOGIN =10003;
    private static final int CREATE =10004;
    private static final int LTV=10005;
    private static final int PAY=10006;

    private static final int CURRENCY=20001;
    private static final int GET_GEM=20002;
    private static final int DEC_GEM=20003;
    private static final int DUP=20004;
    private static final int P_MSG=20005;

    @Override
    public Object rmiResult(int code,Object... prmas) throws GmException {
        String start=prmas[0].toString();
        String end=prmas[1].toString();
        switch (code){
            case COMPREHENSIVE:
                return setComprehensive(start,end);
            case LOSS_RATE:
                return setLoss(start,end);
            case LOGIN:
                return setLogin(start,end);
            case CREATE:
                return setCreate(start,end);
            case LTV:
                return setLTV(start,end);
            case PAY:
                return setPay(start,end);
            case CURRENCY:
                return setCurrency(start,end);
            case GET_GEM:
                return setGoods(start,end,1);
            case DEC_GEM:
                return setGoods(start,end,2);
            case DUP:
                return setDup(start,end);
            case P_MSG:
                return setPMsg(start,end);
            default:
                return null;
        }
    }

    private boolean setPMsg(String start,String end) throws GmException {
        this.countLogService.manualPlayerMsg(start,end);
        return true;
    }

    private boolean setDup(String start,String end) throws GmException {

        return true;
    }

    private boolean setGoods(String start ,String end,int type)throws GmException {

        return true;
    }
    private boolean setCurrency(String start ,String end)throws GmException {

        return true;
    }
    private boolean setLTV(String start ,String end)throws GmException {
        return countLogService.manualSaveLTV(start,end,COMPUTER_DAY);
    }

    private boolean setPay(String start , String end) throws GmException {
        return countLogService.manualSavePay(start,end);
    }

    private boolean setLogin(String start , String end) throws GmException {
        return countLogService.manualSaveLogin(start,end);
    }
    private boolean setCreate(String start , String end) throws GmException {
        return countLogService.manualSaveCreate(start,end);
    }
    private boolean setLoss(String start,String end) throws GmException {
        return countLogService.manualSaveLoss(start,end,COMPUTER_DAY);
    }

    private boolean setComprehensive(String start,String end)throws GmException {
        return countLogService.manualSaveComprehensive(start,end);
    }

    private boolean setFirstPay(String start,String end) throws GmException {
        return countLogService.manualSaveFirstPay(start,end,COMPUTER_DAY);
    }

    private boolean setSecondPay(String start,String end)throws GmException {
        return countLogService.manualSaveSecondPay(start,end,COMPUTER_DAY);
    }
}
