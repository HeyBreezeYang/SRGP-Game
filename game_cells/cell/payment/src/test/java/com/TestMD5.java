package com;

import com.alibaba.fastjson.JSONObject;
import com.config.PaymentConfig;
import com.design.util.DateUtil;
import com.design.util.MD5Sign;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 2018/6/8.
 */
public class TestMD5 {
    public static void main(String args[]){
//        34df01422a6d05e869ca767335d0f2bf
//        String str = "{gameId=8670, channelOrderId=2018060821001004770536386402, channelInfo=null, callbackInfo=null, orderStatus=1, bfOrderId=2018060809516166865535,
//        userId=TC_2459, money=1, appId=wslb0404_0001, time=1528422697, cpOrderId=68de4e3b7cd941199fba6bb20d4eccb6, channelId=test_0001}";
//        appId=wslb0404_0001bfOrderId=2018060809516166865535channelId=test_0001channelOrderId=2018060821001004770536386402cpOrderId=68de4e3b7cd941199fba6bb20d4eccb6gameId=8670money=1orderStatus=1time=1528422697userId=TC_2459c9302cf1fc3dac7c123f1a5a37d1914c
        Map<String,Object> map = new HashMap();
        map.put("gameId",8670);
        map.put("channelOrderId","2018060821001004770536386402");
        map.put("channelInfo",null);
        map.put("callbackInfo","");
        map.put("orderStatus",1);
        map.put("bfOrderId","2018060809516166865535");
        map.put("userId","TC_2459");
        map.put("money",1);
        map.put("appId","wslb0404_0001");
        map.put("time",1528422697);
        map.put("cpOrderId","68de4e3b7cd941199fba6bb20d4eccb6");
        map.put("channelId","test_0001");
        String md5Str = MD5Sign.getSign(map, PaymentConfig.APP_SECRET);
        System.out.println("md5Str:"+md5Str);

        long now = System.currentTimeMillis() / 1000l;
        long daySecond = 60 * 60 * 24;
        long dayTime = now - (now + 8 * 3600) % daySecond;
        System.out.println("dayTime:"+dayTime);

//        float ft = 2/3f;
//        System.out.println(ft);
        int scale = 4;//设置位数
        int roundingMode  =  4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        BigDecimal   bd  =   new  BigDecimal((double)2/3);
        bd = bd.setScale(scale,roundingMode);
        float ft =  bd.floatValue();
        System.out.println(ft);

        try {
            System.out.println(DateUtil.getStringToCurrentTimeMillis("2018-06-15","yyyy-MM-dd")+86399999);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
