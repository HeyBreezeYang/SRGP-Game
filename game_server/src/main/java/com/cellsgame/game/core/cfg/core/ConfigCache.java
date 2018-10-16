package com.cellsgame.game.core.cfg.core;

import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.util.TriConsumer;

/**
 * @author Aly on  2016-11-01.
 */
public interface ConfigCache {
    Map<Integer, BaseCfg> get(String csvName, boolean checkParent);

    Map<Integer, BaseCfg> get(Class<? extends BaseCfg> cfgClass);

    Map<Integer, BaseCfg> get(Class<? extends BaseCfg> cfgClass, boolean checkParent);

    Map<Integer, BaseCfg> remove(Class<? extends BaseCfg> cfgClass);

    Map<Integer, BaseCfg> removeByFileName(String fileName);

    void forByCfgClass(TriConsumer<Class<? extends BaseCfg>, Integer, BaseCfg> consumer, boolean forceAll);

    void forByFileName(TriConsumer<String, Integer, BaseCfg> consumer, boolean forceAll);

    <T extends BaseCfg> T get(Class<T> cfgClass, int cid, boolean checkParent);
}
