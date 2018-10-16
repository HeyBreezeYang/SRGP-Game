package com.master.gm.service.pay;

import com.gmdesign.exception.GmException;

/**
 * Created by DJL on 2017/9/30.
 *
 * @ClassName PayServiceIF
 * @Description 付费相关
 */
public interface PayServiceIF {

    String getPlayerFirstPay(String sid,String pid)throws GmException;
}
