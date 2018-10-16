package com.cellsgame.game.core.cfg.core.proc;

import com.cellsgame.common.util.csv.BaseCfg;
import com.google.common.collect.Table;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class CheckCfgs {

    public static <T extends BaseCfg> CheckCfg<T> of(Class<T> clazz, Function<T, CharSequence> function) {
        if (null == function) return null;
        return of(clazz, (t, allCfg) -> function.apply(t));
    }

    public static <T extends BaseCfg> CheckCfg<T> of(Class<T> clazz, BiFunction<T, Table<Class<? extends BaseCfg>, Integer, BaseCfg>, CharSequence> function) {
        if (null == function) return null;
        return new SimpleCheckCfg<>(clazz, function);
    }

    private static class SimpleCheckCfg<T extends BaseCfg> implements CheckCfg<T> {
        private Class<T> clazz;
        private BiFunction<T, Table<Class<? extends BaseCfg>, Integer, BaseCfg>, CharSequence> function;

        SimpleCheckCfg(Class<T> clazz, BiFunction<T, Table<Class<? extends BaseCfg>, Integer, BaseCfg>, CharSequence> function) {
            this.clazz = clazz;
            this.function = function;
        }

        @Override
        public Class<T> cfgClass() {
            return clazz;
        }

        @Override
        public CharSequence check(Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfg, T cfg) {
            if (null != function) return function.apply(cfg, allCfg);
            return null;
        }
    }

}
