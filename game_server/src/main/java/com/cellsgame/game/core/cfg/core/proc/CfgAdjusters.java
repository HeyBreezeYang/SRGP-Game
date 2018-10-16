package com.cellsgame.game.core.cfg.core.proc;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.core.ConfigCache;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * aly @ 16-11-9.
 */
public final class CfgAdjusters {
    public static <T extends BaseCfg> CfgAdjuster<T> of(Class<T> clazz, boolean needAdd2Cache, Consumer<Map<Integer, T>> consumer) {
        return new SimpleAdjuster<>(clazz, needAdd2Cache, consumer);
    }

    public static <T extends BaseCfg> CfgAdjuster<T> of(Class<T> clazz, boolean needAdd2Cache, BiConsumer<ConfigCache, Map<Integer, T>> consumer) {
        return new SimpleAdjuster2<>(clazz, needAdd2Cache, consumer);
    }

    private static class SimpleAdjuster<T extends BaseCfg> implements CfgAdjuster<T> {
        private boolean needAdd2Cache;
        private Class<T> proType;
        private Consumer<Map<Integer, T>> consumer;

        SimpleAdjuster(Class<T> proType, boolean needAdd2Cache, Consumer<Map<Integer, T>> consumer) {
            this.needAdd2Cache = needAdd2Cache;
            this.proType = proType;
            this.consumer = consumer;
        }

        @Override
        public boolean needAdd2Cache() {
            return needAdd2Cache;
        }

        @Override
        public Class<T> getProType() {
            return proType;
        }

        @Override
        public void doAdjustData(ConfigCache allCfg, Map<Integer, T> map) {
            if (null != consumer) consumer.accept(map);
        }
    }

    private static class SimpleAdjuster2<T extends BaseCfg> implements CfgAdjuster<T> {

        private boolean needAdd2Cache;
        private Class<T> proType;
        private BiConsumer<ConfigCache, Map<Integer, T>> consumer;

        SimpleAdjuster2(Class<T> proType, boolean needAdd2Cache, BiConsumer<ConfigCache, Map<Integer, T>> consumer) {
            this.needAdd2Cache = needAdd2Cache;
            this.proType = proType;
            this.consumer = consumer;
        }

        @Override
        public boolean needAdd2Cache() {
            return needAdd2Cache;
        }

        @Override
        public Class<T> getProType() {
            return proType;
        }

        @Override
        public void doAdjustData(ConfigCache allCfg, Map<Integer, T> map) {
            if (null != consumer) consumer.accept(allCfg, map);
        }
    }
}
