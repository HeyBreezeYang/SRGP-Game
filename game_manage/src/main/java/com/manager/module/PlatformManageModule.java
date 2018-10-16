package com.manager.module;

import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

/**
 * Created by DJL on 2017/6/20.
 *
 * @ClassName PlatformManageModule
 * @Description 主模块
 */
@IocBy(type = ComboIocProvider.class,args={ "*js","ioc/", "*anno", "com.manager", "*tx", "*async"})
@Ok("json:full")
@Fail("http:500")
@Encoding(input = "UTF-8", output = "UTF-8")
@SetupBy(value = PlatformManageSetup.class)
@Modules
public class PlatformManageModule {
    private static final String BASE_URL="/system";
    public static final String LOGIN="/platform";
    public static final String MAIN_MENU=BASE_URL+"/main";

    public static final String PLATFORM=MAIN_MENU+"/platform";
    public static final String LOG=MAIN_MENU+"";
    public static final String MENU=MAIN_MENU+"/menu";
    public static final String USER=MAIN_MENU+"/user";

}
