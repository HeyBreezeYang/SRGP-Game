package com.cellsgame.game.core.module.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjusters;
import com.google.common.collect.ImmutableList;

/**
 * @author Aly on  2016-10-24.
 */
public class CSVAdjust<T> extends ModuleConfig<T> {

    private List<CfgAdjuster> adjusters;

    CSVAdjust(ModuleBuilder<T> moduleBuilder) {
        super(moduleBuilder);
    }

    @Override
    void buildTO(Module<T> module) {
        if (null != adjusters)
            module.adjusterMap = ImmutableList.copyOf(adjusters);
        else
            module.adjusterMap = ImmutableList.of();
    }

    public CSVCheck<T> checker() {
        return new CSVCheck<>(moduleBuilder);
    }


    public <C extends BaseCfg> CSVAdjust<T> regCusAdjuster(Class<C> clazz, boolean needAdd2Cache, Consumer<Map<Integer, C>> consumer) {
        return regCusAdjuster(CfgAdjusters.of(clazz, needAdd2Cache, consumer));
    }

    public <C extends BaseCfg> CSVAdjust<T> regCusAdjuster(Class<C> clazz, boolean needAdd2Cache, BiConsumer<ConfigCache, Map<Integer, C>> consumer) {
        return regCusAdjuster(CfgAdjusters.of(clazz, needAdd2Cache, consumer));
    }

    public <C extends BaseCfg> CSVAdjust<T> regCusAdjuster(CfgAdjuster<C> adjuster) {
        Class<C> cfgClass = adjuster.getProType();
        if (!moduleBuilder.isCSVLoaded(cfgClass)) {
            throw new RuntimeException(" 只能对自己模块下的配置 进行处理," + cfgClass);
        } else {
            if (adjusters == null) {
                adjusters = new ArrayList<>();
            }
            adjusters.add(adjuster);
        }
        return this;
    }
}
