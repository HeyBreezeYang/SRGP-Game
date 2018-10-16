package com.manager.module.log;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import com.manager.module.PlatformManageModule;
/**
 * Created by DJL on 2017/6/20.
 *
 * @ClassName LogModule
 * @Description 管理日志
 */
@IocBean
@Ok("json:full")
@At(PlatformManageModule.LOG)
@Fail("http:500")
public class LogModule {
}
