package com.cellsgame.game.core.cfg.core;

import java.util.Collection;
import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.util.TriConsumer;
/**
 * @author Aly on  2016-11-01.
 */
class ConfigCacheWithAll implements ConfigCache {
    private Collection<ConfigCache> configCaches;

    private ConfigCache moduleConfigCache;

    ConfigCacheWithAll(ConfigCache moduleConfigCache, Collection<ConfigCache> configCaches) {
        this.moduleConfigCache = moduleConfigCache;
        this.configCaches = configCaches;
    }

    @Override
    public Map<Integer, BaseCfg> get(String csvName, boolean checkParent) {
        Map<Integer, BaseCfg> cfgMap = moduleConfigCache.get(csvName, checkParent);
        if (null == cfgMap)
            for (ConfigCache configCache : configCaches) {
                cfgMap = configCache.get(csvName, checkParent);
                if (null != cfgMap) return cfgMap;
            }
        return cfgMap;
    }

    @Override
    public Map<Integer, BaseCfg> get(Class<? extends BaseCfg> cfgClass) {

        Map<Integer, BaseCfg> cfgMap = moduleConfigCache.get(cfgClass);
        if (null == cfgMap)
            for (ConfigCache configCache : configCaches) {
                cfgMap = configCache.get(cfgClass);
                if (null != cfgMap) return cfgMap;
            }
        return cfgMap;
    }

    @Override
    public Map<Integer, BaseCfg> get(Class<? extends BaseCfg> cfgClass, boolean checkParent) {
        Map<Integer, BaseCfg> cfgMap = moduleConfigCache.get(cfgClass);
        if (null == cfgMap)
            for (ConfigCache configCache : configCaches) {
                cfgMap = configCache.get(cfgClass, checkParent);
                if (null != cfgMap) break;
            }
        return cfgMap;
    }

    @Override
    public Map<Integer, BaseCfg> remove(Class<? extends BaseCfg> cfgClass) {
        return moduleConfigCache.remove(cfgClass);
    }

    @Override
    public Map<Integer, BaseCfg> removeByFileName(String fileName) {
        return moduleConfigCache.removeByFileName(fileName);
    }

    @Override
    public void forByCfgClass(TriConsumer<Class<? extends BaseCfg>, Integer, BaseCfg> consumer, boolean forceAll) {
        moduleConfigCache.forByCfgClass(consumer, forceAll);
    }

    @Override
    public void forByFileName(TriConsumer<String, Integer, BaseCfg> consumer, boolean forceAll) {
        moduleConfigCache.forByFileName(consumer, forceAll);

    }

    @Override
    public <T extends BaseCfg>  T get(Class<T> cfgClass, int cid, boolean checkParent) {
        BaseCfg baseCfg = moduleConfigCache.get(cfgClass, cid, checkParent);
        if (null == baseCfg)
            for (ConfigCache configCach : configCaches) {
                baseCfg = configCach.get(cfgClass, cid, checkParent);
                if (null != baseCfg) 
                	return (T) baseCfg;
            }
        return (T) baseCfg;
    }

    @Override
    public String toString() {
        return "ConfigCacheWithAll{" +
                "moduleConfigCache=" + moduleConfigCache +
                '}';
    }
}
