package com.cellsgame.game.core.cfg.base;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.cache.CacheConfig;

/**
 * @author Aly on  2016-07-18.
 */
public interface CfgVO<T extends BaseCfg> {

    default T getCfg() {
        return CacheConfig.getCfg(getCfgClass(), getCid());
    }

    Class<T> getCfgClass();

    Integer getCid();

}
