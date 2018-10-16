package com.manager.module;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

/**
 * Created by DJL on 2017/6/20.
 *
 * @ClassName manage
 * @Description
 */
public class PlatformManageSetup implements Setup {


    @Override
    public void init(NutConfig nutConfig) {
        Ioc ioc = nutConfig.getIoc();
        Dao dao = ioc.get(Dao.class);
        Daos.createTablesInPackage(dao, "com.manager.bean", false);

    }

    @Override
    public void destroy(NutConfig nc) {

    }
}
