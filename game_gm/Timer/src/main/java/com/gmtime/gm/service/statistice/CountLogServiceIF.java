package com.gmtime.gm.service.statistice;

import com.gmdesign.exception.GmException;
import com.gmtime.gm.sql.SqlIF;

import java.util.List;

/**
 * Created by DJL on 2016/10/26.
 *
 * @ClassName CountLogServiceIF
 * @Description 对记录的原数据做需求分析后存入数据库
 */
public interface CountLogServiceIF {
    void saveAllLogResult(String time) throws GmException;
    void saveSnapshot(String time) throws GmException;
    void saveNoonBoss(String time) throws GmException ;
    void saveNightBoss(String time) throws GmException ;
    void saveShop(String time)throws GmException;
    void saveMonetary(String time)throws GmException;
    void saveCheckpoint(String time)throws GmException;
    long savePlayerMsg(String date,String hour,long lastPayLogTime)throws GmException;

    void manualPlayerMsg(String start,String end)throws GmException;
    boolean manualSaveComprehensive(String start,String end)throws GmException;
    boolean manualSaveLoss(String start,String end,int day)throws GmException;
    boolean manualSaveLTV(String start,String end,int day)throws GmException;
    boolean manualSaveCreate(String start,String end)throws GmException;
    boolean manualSavePay(String start,String end)throws GmException;
    boolean manualSaveLogin(String start,String end)throws GmException;

    boolean manualSaveFirstPay(String start,String end,int day)throws GmException;
    boolean manualSaveSecondPay(String start,String end,int day)throws GmException;
}
