package com.cellsgame.game.core.cfg.base;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.orm.DBVO;

/**
 * @author Aly on  2016-07-18.
 */
public abstract class CfgSaveVO<T extends BaseCfg> extends DBVO implements CfgVO<T> {
    private Class<T> cfgClass;
    private T cfg;

    public CfgSaveVO(Class<T> cfgClass) {
        this.cfgClass = cfgClass;
    }

    public CfgSaveVO(Class<T> cfgClass, int cid) {
        this(cfgClass);
        setCid(cid);
    }

    public CfgSaveVO(Class<T> cfgClass, T cfg) {
        this(cfgClass, cfg == null ? 0 : cfg.getId());
        this.cfg = cfg;
    }

    @Override
    public Class<T> getCfgClass() {
        return cfgClass;
    }

    @Override
    public T getCfg() {
        if (null == cfg)
            cfg = CacheConfig.getCfg(cfgClass, getCid());
        return cfg;
    }
}
