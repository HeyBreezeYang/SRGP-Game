package com.manager.service.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;

/**
 * Created by DJL on 2017/6/20.
 *
 * @ClassName BaseService
 * @Description
 */
public abstract class BaseService {
    @Inject
    protected Dao dao;
}
