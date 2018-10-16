package com.config;

/**
 * Created by DJL on 2018/3/5.
 *
 * @ClassName base
 * @Description
 */
public interface PaymentConfig {
    int ALI_TYPE=1;
    int IOS_TYPE=3;
    int GOOGLE_TYPE=4;
    int FUN_TYPE=5;
    int YING_HUN_TYPE=6;
    String APP_SECRET="c9302cf1fc3dac7c123f1a5a37d1914c";
    String PRIVATEKEY="callthegameservicedeliveryinterfacekey";
    String CURRENCY = "RMB";


    String FUN_PAY_URL="http://mainaland.payment.raink.com.cn";//"http://payment-beta.funcell123.com";

}
