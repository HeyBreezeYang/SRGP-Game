package com.cellsgame.game.core.cfg.core.proc;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.core.ConfigCache;

import java.util.Map;

/**
 * @author Aly on  2016-08-08.
 *         配置调整器
 *         数据调整 跟参照调整 只提供意义上的数据 结构 并无实际的使用限制
 *         需要强制规范代码为: adjustCfg 只处理数据上的调整 包括对配置数据的增加删除等
 *         resetRef 在所有数据调整之后  理应只包含对配置之间的引用调整
 */
public interface CfgAdjuster<T extends BaseCfg> {
    /**
     * 数据调整
     */
    @SuppressWarnings("unchecked")
    default void adjustCfg(ConfigCache allCfg) {
        Map<Integer, BaseCfg> config = allCfg.get(getProType());
        if (null != config) {
            Map<Integer, T> opMap = (Map<Integer, T>) config;
            doAdjustData(allCfg, opMap);
            if (!needAdd2Cache()) {
                allCfg.remove(getProType());
            }
        }
    }

    /**
     * 是否需要添加到全局配置缓存
     */
    boolean needAdd2Cache();

    /**
     * 参照调整
     */
    default void resetRef(ConfigCache allCfg, T baseCfg){}

    Class<T> getProType();

    /**
     * 数据调整 -- 理应只涉及到数据的整理
     */
    void doAdjustData(ConfigCache allCfg, Map<Integer, T> map);
}
