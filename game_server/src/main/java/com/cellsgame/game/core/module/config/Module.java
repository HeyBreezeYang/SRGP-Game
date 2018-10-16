package com.cellsgame.game.core.module.config;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.common.util.csv.TypeProvider;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.core.cfg.core.proc.CheckCfg;
import com.cellsgame.game.core.module.i.SysListener;
import com.cellsgame.game.core.module.load.ModuleLoader;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;

/**
 * @author Aly on  2016-10-24.
 */
public class Module<T> {


    //----------------------------
    private final int moduleID;
    private final Class<T> moduleInterFace;
    TypeProvider jsonTypeProvider;
    ImmutableBiMap<String, Class<? extends BaseCfg>> csvLoadConfig;
    ImmutableList<CfgAdjuster> adjusterMap;
    ImmutableList<CheckCfg> checkCfgs;
    SysListener<T> listener;

    Module(int moduleID, Class<T> moduleInterFace) {
        this.moduleID = moduleID;
        this.moduleInterFace = moduleInterFace;
    }

    public static <T> ModuleBuilder<T> createModule(Class<T> moduleInterFace) {
        if (moduleInterFace.isInterface()) {
            AModule module = moduleInterFace.getAnnotation(AModule.class);
            if (null == module) {
                throw new RuntimeException(" 模块必须包含AModule 注解:" + moduleInterFace);
            }
            int moduleID = module.value();
            Module oldModule = ModuleLoader.moduleMap.get(moduleID);
            if (oldModule != null) {
                throw new RuntimeException(" 已经加载了模块:" + moduleID + "  new:" + moduleInterFace + "  old:" + oldModule.getModuleInterFace());
            }
            return new ModuleBuilder<>(moduleID, moduleInterFace);
        }
        throw new RuntimeException(" 模块加载必须为接口:" + moduleInterFace);
    }

    public int getModuleID() {
        return moduleID;
    }

    public Class<T> getModuleInterFace() {
        return moduleInterFace;
    }

    public TypeProvider getJsonTypeProvider() {
        return jsonTypeProvider;
    }

    public ImmutableBiMap<String, Class<? extends BaseCfg>> getCsvLoadConfig() {
        return csvLoadConfig;
    }

    public ImmutableList<CfgAdjuster> getAdjusterMap() {
        return adjusterMap;
    }

    public ImmutableList<CheckCfg> getCheckCfgs() {
        return checkCfgs;
    }

    public SysListener<T> getListener() {
        return listener;
    }
}
