package com.cellsgame.game.core.module.config;

import java.util.HashMap;
import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleBuilder<T> implements Buildable<Module> {

    Map<Class<? extends ModuleConfig>, ModuleConfig> configs = new HashMap<>();

    private int moduleID;
    private Class<T> moduleInterFace;

    ModuleBuilder(int moduleID, Class<T> moduleInterFace) {
        this.moduleID = moduleID;
        this.moduleInterFace = moduleInterFace;
    }

    public CSVHead<T> jsonHead() {
        return new CSVHead<>(this);
    }

    @Override
    public Module<T> build() {
        Module<T> module = new Module<>(moduleID, moduleInterFace);
        for (ModuleConfig config : configs.values()) {
            config.buildTO(module);
        }
        return module;
    }

    boolean isCSVLoaded(Class<? extends BaseCfg> CfgClass) {
        CSVFileLoader config = (CSVFileLoader) configs.get(CSVFileLoader.class);
        return null != config && config.containsCfg(CfgClass);
    }
}
