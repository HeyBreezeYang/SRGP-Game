package com.master.module;

import com.master.cache.GmDocConfig;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.integration.quartz.NutQuartzCronJobFactory;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import java.util.List;

/**
 * Created by DJL on 2017/3/2.
 *
 * @ClassName GmSetup
 * @Description
 */
public class GmSetup implements Setup{
    @Override
    public void init(NutConfig nutConfig) {
        System.out.println("qurtz初始化");
        Ioc ioc = nutConfig.getIoc();
        Dao dao = ioc.get(Dao.class);
//        Daos.createTablesInPackage(dao, "com.master.bean", false);
//        GmDocConfig.initFile();

        // 触发quartz 工厂,将扫描job任务
        ioc.get(NutQuartzCronJobFactory.class);
       /* String[] names = ioc.getNames();
        for (String s:names) {
            System.out.println(s);
        }*/
    }



    @Override
    public void destroy(NutConfig nutConfig) {
    }
}
