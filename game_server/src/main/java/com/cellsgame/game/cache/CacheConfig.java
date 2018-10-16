package com.cellsgame.game.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cellsgame.common.util.csv.BaseCfg;
import com.google.common.collect.Table;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;

public class CacheConfig {
    private volatile static Map<Class<? extends BaseCfg>, IntObjectHashMap<BaseCfg>> allCfgCache;
    private volatile static int[] rate;

    @SuppressWarnings("unchecked")
    public static <T extends BaseCfg> IntObjectMap<T> getCfgsByClz(Class<T> clz) {
        return (IntObjectMap<T>) allCfgCache.get(clz);
    }

    /**
     * 返回对应的配置数据
     *
     * @throws NullPointerException 如果缓存中不存在 clazz 对应的缓存
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseCfg> T getCfg(Class<T> clazz, int id) {
//        if (null == map) return null;     // 设计的时候就应该知道的
        return (T) allCfgCache.get(clazz).get(id);
    }

    // 不直接赋值 重新构造 减少内存浪费
    public static void cacheAllConfig(Table<Class<? extends BaseCfg>, Integer, BaseCfg> all) {
        // make a copy
        Map<Class<? extends BaseCfg>, IntObjectHashMap<BaseCfg>> cfgs = new ConcurrentHashMap<>(all.size());
        for (Map.Entry<Class<? extends BaseCfg>, Map<Integer, BaseCfg>> entry : all.rowMap().entrySet()) {
            Class<? extends BaseCfg> key = entry.getKey();
            Map<Integer, BaseCfg> value = entry.getValue();

            int size = value.size();
            if (size > 0) {
                IntObjectHashMap<BaseCfg> map = cfgs.get(key);
                if (null == map) {
                    cfgs.put(key, map = new IntObjectHashMap<>(size, 1));
                }
                for (Map.Entry<Integer, BaseCfg> cfgEntry : value.entrySet()) {
                    Integer id = cfgEntry.getKey();
                    BaseCfg cfg = cfgEntry.getValue();
                    map.put(id.intValue(), cfg);
                }
            }
        }
        allCfgCache = cfgs;
    }

    public static int getRate(int idx) {
        if (idx >= rate.length)
            return rate[rate.length - 1];
        if (idx <= 0)
            return 0;
        return rate[idx];
    }


    public static void setRate(int[] rate) {
        CacheConfig.rate = new int[rate.length];
        System.arraycopy(rate, 0, CacheConfig.rate, 0, rate.length);
    }

}
