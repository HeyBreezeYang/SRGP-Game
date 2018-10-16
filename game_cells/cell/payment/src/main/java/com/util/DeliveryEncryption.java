package com.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.design.context.PlatformKey;
import com.design.exception.PlatformException;
import com.design.util.DESEncrypt;
import com.design.util.URLTool;


/**
 * Created by DJL on 2017/9/7.
 *
 * @ClassName cells
 * @Description
 */
public class DeliveryEncryption {

    public static String result(Map res){
        return DESEncrypt.encoderByDES(PlatformKey.DELIVERY_KEY,JSON.toJSONString(res));
    }
    private static final String DELIVER_URL="http://172.31.0.6:9895/send/deliver?deliverMsg=";
    public static String activeNotification(String penetration) throws PlatformException {
        return  URLTool.sendMsg(DELIVER_URL.concat(penetration));
    }
}
