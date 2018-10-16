package com.cellsgame.game.core.module.config;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.core.proc.CheckCfg;
import com.cellsgame.game.core.cfg.core.proc.CheckCfgs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Aly on  2016-11-03.
 */
public class CSVCheck<T> extends ModuleConfig<T> {
    private List<CheckCfg> checkCfg;

    CSVCheck(ModuleBuilder<T> moduleBuilder) {
        super(moduleBuilder);
    }

    @Override
    void buildTO(Module<T> module) {
        if (null != checkCfg)
            module.checkCfgs = ImmutableList.copyOf(checkCfg);
        else
            module.checkCfgs = ImmutableList.of();
    }

    public ModuleListener<T> listener() {
        return new ModuleListener<>(moduleBuilder);
    }

    public <C extends BaseCfg> CSVCheck<T> regCfgChecker(Class<C> clazz, Function<C, CharSequence> function) {
        return regCfgChecker(CheckCfgs.of(clazz, function));
    }

    public <C extends BaseCfg> CSVCheck<T> regCfgChecker(Class<C> clazz, BiFunction<C, Table<Class<? extends BaseCfg>, Integer, BaseCfg>, CharSequence> function) {
        return regCfgChecker(CheckCfgs.of(clazz, function));
    }

    private <C extends BaseCfg> CSVCheck<T> regCfgChecker(CheckCfg<C> adjuster) {
        if (null == adjuster) return this;
        Class<C> cfgClass = adjuster.cfgClass();
        if (!moduleBuilder.isCSVLoaded(cfgClass)) {
            throw new RuntimeException(" 只能调整自己模块下加载的配置," + cfgClass);
        } else {
            if (checkCfg == null) {
                checkCfg = new ArrayList<>();
            }
            checkCfg.add(adjuster);
        }
        return this;
    }
}
