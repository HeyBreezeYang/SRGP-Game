package com.cellsgame.game.core.cfg.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.util.TriConsumer;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author Aly on  2016-10-27.
 */
class ConfigCacheWithModule implements ConfigCache {
    private Table<String, Integer, BaseCfg> configByFileName;
    private BiMap<String, Class<? extends BaseCfg>> fileNameBiMap;
    private Set<String> removed = new HashSet<>();
    private int moduleID;
    private ConfigCache parent;

    ConfigCacheWithModule(Module<Object> module) {
        this.moduleID = module.getModuleID();
        fileNameBiMap = module.getCsvLoadConfig();
    }

    public void setParent(ConfigCache parent) {
        this.parent = parent;
    }

    public BaseCfg put(String csvName, Integer id, BaseCfg baseCfg) {
        if (configByFileName == null) {
            configByFileName = HashBasedTable.create();
        }
        return configByFileName.put(csvName, id, baseCfg);
    }

    @Override
    public Map<Integer, BaseCfg> get(String csvName, boolean checkParent) {
        if (configByFileName != null) {
            Map<Integer, BaseCfg> row = configByFileName.row(csvName);
            if (null != row) {
                if (removed.contains(csvName)) {
                    return null;
                }
            } else {
                if (checkParent && null != parent)
                    row = parent.get(csvName, true);
            }
            return row;
        }
        return null;
    }

    @Override
    public Map<Integer, BaseCfg> get(Class<? extends BaseCfg> cfgClass) {
        return get(cfgClass, true);
    }

    @Override
    public Map<Integer, BaseCfg> get(Class<? extends BaseCfg> cfgClass, boolean checkParent) {
        String fileName = fileNameBiMap.inverse().get(cfgClass);
        if (null != fileName) {
            return get(fileName, checkParent);
        } else if (checkParent && null != parent) {
            return parent.get(cfgClass, true);
        }
        return null;
    }

    /**
     * 只能移除本module的
     */
    @Override
    public Map<Integer, BaseCfg> remove(Class<? extends BaseCfg> cfgClass) {
        String fileName = fileNameBiMap.inverse().get(cfgClass);
        if (null != fileName) {
            return removeByFileName(fileName);
        }
        return null;
    }

    @Override
    public Map<Integer, BaseCfg> removeByFileName(String fileName) {
        if (null != configByFileName) {
            Map<Integer, BaseCfg> cfgMap = configByFileName.rowMap().get(fileName);
            if (null != cfgMap) {
                removed.add(fileName);
            }
            return cfgMap;
        }
        return null;
    }

    @Override
    public void forByCfgClass(TriConsumer<Class<? extends BaseCfg>, Integer, BaseCfg> consumer, boolean forceAll) {
        forByFileName(new TriConsumer<String, Integer, BaseCfg>() {
            @Override
            public void accept(String s, Integer integer, BaseCfg baseCfg) {
                consumer.accept(fileNameBiMap.get(s), integer, baseCfg);
            }
        }, forceAll);
    }

    @Override
    public void forByFileName(TriConsumer<String, Integer, BaseCfg> consumer, boolean forceAll) {
        if (configByFileName == null) return;
        for (Map.Entry<String, Map<Integer, BaseCfg>> entry : configByFileName.rowMap().entrySet()) {
            String fileName = entry.getKey();
            if (!forceAll && removed.contains(fileName)) {
                continue;
            }
            for (Map.Entry<Integer, BaseCfg> cfgEntry : entry.getValue().entrySet()) {
                consumer.accept(fileName, cfgEntry.getKey(), cfgEntry.getValue());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseCfg> T get(Class<T> cfgClass, int cid, boolean checkParent) {
        String fileName = fileNameBiMap.inverse().get(cfgClass);
        if (null != fileName) {
            Map<Integer, BaseCfg> cfgMap = get(fileName, checkParent);
            if (null != cfgMap)
                return (T) cfgMap.get(cid);
        } else if (checkParent && null != parent) {
            return parent.get(cfgClass, cid, true);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ConfigCacheWithModule{" +
                "moduleID=" + moduleID +
                '}';
    }
}
