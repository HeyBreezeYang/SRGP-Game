package com.cellsgame.game.core.cfg.core;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cellsgame.common.util.FileEx;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.common.util.csv.CsvConfigReader;
import com.cellsgame.common.util.csv.TypeProvider;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.core.cfg.core.proc.CheckCfg;
import com.cellsgame.game.core.cfg.valid.CfgCheckerManager;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.util.DebugTool;
import com.google.common.base.Charsets;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CfgService {

    /**
     * csv 文件路径
     */
    private static String basePath = FileEx.getAppRoot() + File.separator + "gameConfig" + File.separator;

    private static Logger log = LoggerFactory.getLogger(CfgService.class);

    public static Object getFieldValue(Field field, Object object) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        Object value = field.get(object);
        field.setAccessible(accessible);
        return value;
    }

    public static void loadAllCfg(int globalModuleID, boolean loadInMain, Map<Integer, Module<?>> moduleMap) throws Exception {
        // 从文件加载
        Collection<ConfigCache> allCfgData = loadCfg(globalModuleID, moduleMap);

        Table<Class<? extends BaseCfg>, Integer, BaseCfg> allConfigWithRemoved = HashBasedTable.create();
        Table<Class<? extends BaseCfg>, Integer, BaseCfg> allConfig = HashBasedTable.create();

        for (ConfigCache cache : allCfgData) {
            cache.forByCfgClass(allConfigWithRemoved::put, true);
            cache.forByCfgClass(allConfig::put, false);
        }

        StringBuilder msg = checkAllCfg(moduleMap, allConfigWithRemoved);
        if (msg.length() > 0) {
            DebugTool.throwException(log, msg.toString());
        }

        if (loadInMain) {
            Dispatch.dispatchGameLogic(() -> CacheConfig.cacheAllConfig(allConfig));
        } else
            CacheConfig.cacheAllConfig(allConfig);
    }

    @SuppressWarnings("unchecked")
    private static StringBuilder checkAllCfg(Map<Integer, Module<?>> moduleMap, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        StringBuilder msg = new StringBuilder();
        try {
            for (Module<?> module : moduleMap.values()) {
                ImmutableList<CheckCfg> checkCfgs = module.getCheckCfgs();
                if (null == checkCfgs) continue;
                for (CheckCfg checkCfg : checkCfgs) {
                    Map<Integer, BaseCfg> cfgMap = allCfgData.rowMap().get(checkCfg.cfgClass());
                    if (null != cfgMap) {
                        for (BaseCfg baseCfg : cfgMap.values()) {
                            CharSequence check = checkCfg.check(allCfgData, baseCfg);
                            if (StringUtils.isNotEmpty(check)) {
                                msg.append("CFG:[")
                                        .append(baseCfg.getClass().getSimpleName())
                                        .append("] CID: [")
                                        .append(baseCfg.getId())
                                        .append("]")
                                        .append(check).append("\n");
                            }
                        }
                    }
                }
            }
            // 检查CSV
            StringBuilder checkRes = CfgCheckerManager.getManager().checkAllCfg(allCfgData);
            msg.append(checkRes);
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        return msg;
    }

    @SuppressWarnings("unchecked")
    private static Collection<ConfigCache> loadCfg(int globalModuleID, Map<Integer, Module<?>> moduleMap) {
        Module globalModule = moduleMap.get(globalModuleID);
        TypeProvider globalType = globalModule == null ? null : globalModule.getJsonTypeProvider();

        Map<String, Class<? extends BaseCfg>> fileNameCfgCache = new HashMap<>();
        Map<Module<Object>, ConfigCache> moduleCfgCacheMap = new HashMap<>();
        //
        for (Module module : moduleMap.values()) {
            if (null == module.getCsvLoadConfig()) continue;
            ConfigCache moduleCfgCache = CfgService.loadCfg(fileNameCfgCache.keySet(), globalType, module);
            moduleCfgCacheMap.put(module, moduleCfgCache);
            fileNameCfgCache.putAll(module.getCsvLoadConfig());
        }
        log.info("ALL Module cfg loaded");


        // 数据调整
        ArrayList<ConfigCache> allCaches = Lists.newArrayList(moduleCfgCacheMap.values());
        for (Map.Entry<Module<Object>, ConfigCache> entry : moduleCfgCacheMap.entrySet()) {
            ConfigCacheWithAll allCfg = new ConfigCacheWithAll(entry.getValue(), allCaches);
            ImmutableList<CfgAdjuster> adjusters = entry.getKey().getAdjusterMap();
            if (adjusters != null) {
                for (CfgAdjuster adjuster : adjusters) {
                    try {
                        adjuster.adjustCfg(allCfg);
                    } catch (Throwable e) {
                        DebugTool.throwException("adjuster:" + adjuster.getProType(), e);
                    }
                }
            }
        }

        // 参照调整
        for (Map.Entry<Module<Object>, ConfigCache> entry : moduleCfgCacheMap.entrySet()) {
            ConfigCacheWithAll allCfg = new ConfigCacheWithAll(entry.getValue(), allCaches);
            ImmutableList<CfgAdjuster> adjusterMap = entry.getKey().getAdjusterMap();
            if (adjusterMap != null) {
                for (CfgAdjuster adjuster : adjusterMap) {
                    Map<Integer, BaseCfg> map = allCfg.get(adjuster.getProType());
                    if (null != map) {
                        for (BaseCfg baseCfg : map.values()) {
                            adjuster.resetRef(allCfg, baseCfg);
                        }
                    }
                }
            }
        }
        return moduleCfgCacheMap.values();
    }


    /**
     * 从文件加载所有配置数据
     */
    private static ConfigCache loadCfg(Collection<String> loadedFileName, TypeProvider globalType, Module<Object> module) {
        CsvConfigReader reader = new CsvConfigReader(basePath, Charsets.UTF_8, new TypeProviderWithGlobal(module, globalType));
        reader.setDebug(DebugTool.isDebug());

        ConfigCacheWithModule moduleLoadCache = new ConfigCacheWithModule(module);

        for (Map.Entry<String, Class<? extends BaseCfg>> entry : module.getCsvLoadConfig().entrySet()) {
            // 基础Map 加载
            String csvName = entry.getKey();
            Class<? extends BaseCfg> cfgClass = entry.getValue();
            if (!loadedFileName.contains(csvName)) {
                try {
                    // Load
                    reader.loadCfg(cfgClass, csvName, (id, baseCfg) -> {
                        try {
                            if (moduleLoadCache.put(csvName, id, baseCfg) != null) {
                                String format = String.format(" 配置档ID 重复  cfgClass:[%s] csv:[%s]  ID:%s", cfgClass, csvName, baseCfg.getId());
                                DebugTool.throwException(log, format, null);
                            }
                        } catch (Exception e) {
                            String msg = String.format("Error:  cfgClass:[%s] csv:[%s]  ID:%s ,e", cfgClass, csvName, baseCfg.getId());
                            DebugTool.throwException(log, msg, e);
                        }
                    });

                    Map<Integer, BaseCfg> cfgMap = moduleLoadCache.get(csvName, false);
                    // checkSize
                    log.info(String.format("loader [{%20s}]   csv:[{%20s}]  size:[{%10s}] end", cfgClass.getSimpleName(), csvName, cfgMap == null ? 0 : cfgMap.size()));
                } catch (Exception e) {
                    DebugTool.throwException(log, "", e);
                }
            } else {
                log.info(" CSV  Name:[{}] 重复加载", csvName);
            }
        }
        return moduleLoadCache;
    }
}
