package com.cellsgame.game.core.module.config;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.cache.CacheDisData;
import com.cellsgame.game.core.cfg.core.proc.impl.DiscreteDataAdjuster;
import com.cellsgame.game.core.csv.DisDataConfig;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;

/**
 * @author Aly on  2016-10-24.
 */
public class CSVFileLoader<T> extends ModuleConfig<T> {
    private BiMap<String, Class<? extends BaseCfg>> csvLoadConfig;
    private DiscreteDataAdjuster discreteDataAdjuster;
    private CSVAdjust<T> tcsvAdjust;

    CSVFileLoader(ModuleBuilder<T> moduleBuilder) {
        super(moduleBuilder);
    }

    @Override
    void buildTO(Module<T> module) {
        if (null != csvLoadConfig)
            module.csvLoadConfig = ImmutableBiMap.copyOf(csvLoadConfig);
        else
            module.csvLoadConfig = ImmutableBiMap.of();
    }

    public CSVAdjust<T> adjustConfig() {
        CSVAdjust<T> tcsvAdjust = new CSVAdjust<>(moduleBuilder);
        if (null != discreteDataAdjuster) tcsvAdjust.regCusAdjuster(discreteDataAdjuster);
        this.tcsvAdjust = tcsvAdjust;
        return tcsvAdjust;
    }

    public <C extends Enum & CacheDisData> CSVFileLoader<T> loadDisData(String fileName, Class<C> enumCls) {
        load(fileName, DisDataConfig.class);
        discreteDataAdjuster = new DiscreteDataAdjuster(enumCls);
        return this;
    }

    public CSVFileLoader<T> load(String fileName, Class<? extends BaseCfg> configClass) {
        if (csvLoadConfig == null) {
            csvLoadConfig = HashBiMap.create();
        }
        csvLoadConfig.put(fileName, configClass);
        return this;
    }

    boolean containsCfg(Class<? extends BaseCfg> cfgClass) {
        return csvLoadConfig != null && csvLoadConfig.containsValue(cfgClass);
    }

    @Override
    public Module<T> build() {
        if (null == tcsvAdjust && null != discreteDataAdjuster) {
            adjustConfig();
        }
        return super.build();
    }
}
