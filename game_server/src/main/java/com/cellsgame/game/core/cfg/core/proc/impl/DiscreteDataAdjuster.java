package com.cellsgame.game.core.cfg.core.proc.impl;

import java.util.Arrays;
import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.cache.CacheDisData;
import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.core.csv.DisDataConfig;
import com.cellsgame.game.util.DebugTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aly on  2016-08-08.
 */
public class DiscreteDataAdjuster implements CfgAdjuster<DisDataConfig> {
    private static Logger log = LoggerFactory.getLogger(DiscreteDataAdjuster.class);

    private Class<? extends Enum> disData;

    public <T extends Enum & CacheDisData> DiscreteDataAdjuster(Class<T> disData) {
        if (CacheDisData.class.isAssignableFrom(disData)) {
            this.disData = disData;
        } else
            throw new RuntimeException(" 离散表调整 必须 实现 接口:CacheDisData");
    }

    @Override
    public void adjustCfg(ConfigCache allCfg) {
        Map<Integer, BaseCfg> cfgMap = allCfg.remove(DisDataConfig.class);
        if (null == cfgMap) {
            throw new RuntimeException("离散表没有加载  DisDataConfig   ->" + allCfg);
        } else {
            Enum[] enumConstants = disData.getEnumConstants();
            for (Enum constant : enumConstants) {
                CacheDisData val = (CacheDisData) constant;
                DisDataConfig ddcfg = (DisDataConfig) cfgMap.get(val.getId());
                if (ddcfg == null) {
                    DebugTool.throwException(log, "DiscreteDataConfig is null , id = " + val.getId());
                } else {
                    int[] data = ddcfg.getData();
                    if (val.needSort()) {
                        Arrays.sort(data);
                    }
                    val.setData(data);
                }
            }
        }
    }

    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<DisDataConfig> getProType() {
        return DisDataConfig.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, DisDataConfig> map) {
    }
}
