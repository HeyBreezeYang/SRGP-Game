package com.master.module;

import com.master.filter.GmFilter;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

/**
 * Created by DJL on 2017/3/2.
 *
 * @ClassName GmModule
 * @Description 主模块
 */
@IocBy(type = ComboIocProvider.class,args={"*js", "ioc/", "*anno", "com.master", "*tx", "*async","*jedis","*quartz"})
@Ok("json:full")
@Fail("http:500")
@Encoding(input = "UTF-8", output = "UTF-8")
@SetupBy(value = GmSetup.class)
@Filters({@By(type = GmFilter.class)})
@Modules
public class GmModule {
    public static final String BASE_URL="/system";
    public static final String TIMER_URL=BASE_URL+"/timer";
    public static final String QUERY_URL=BASE_URL+"/query";
    public static final String PLAYER_URL=BASE_URL+"/player";
    public static final String REPORT_URL=BASE_URL+"/report";
    public static final String CUSTOMER_URL=BASE_URL+"/customer";
    public static final String NOTICE_URL=BASE_URL+"/notice";
    public static final String FILE_URL=BASE_URL+"/load";
    public static final String ACTIVITY_URL=BASE_URL+"/activity";
    public static final String CODE_URL=BASE_URL+"/code";
    public static final String RESET_URL=BASE_URL+"/gm";
    public static final String MAIL_URL=BASE_URL+"/mail";
    public static final String SERVER_URL=BASE_URL+"/server";
    public static final String ANALYSIS_URL=BASE_URL+"/analysis";
    public static final String SHOP_URL=BASE_URL+"/shop";
    public static final String EXC_URL=BASE_URL+"/exchange";
    public static final String REAL_TIME_MONITORING=BASE_URL+"/realTimeMonitoring";
    public static final String INSIDE_ACCOUNT=BASE_URL+"/insideAccount";
    public static final String ITEMS_MANAGE=BASE_URL+"/itemsManage";
    public static final String RANK=BASE_URL+"/rank";
    public static final String ORDER=BASE_URL+"/order";
    public static final String IP_WHITE_LIST=BASE_URL+"/ipWhiteList";
    public static final String RETENTION_RATES=BASE_URL+"/retentionRates";
    public static final String LTV=BASE_URL+"/LTV";
}
