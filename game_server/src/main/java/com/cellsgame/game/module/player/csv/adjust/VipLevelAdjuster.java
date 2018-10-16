package com.cellsgame.game.module.player.csv.adjust;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.player.csv.VipLevelConfig;

import java.util.Map;

/**
 * File Description.
 *
 * @author Yang
 */
public class VipLevelAdjuster implements CfgAdjuster<VipLevelConfig> {
    /**
     * 是否需要添加到全局配置缓存
     */
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<VipLevelConfig> getProType() {
        return VipLevelConfig.class;
    }

    /**
     * 数据调整 -- 理应只涉及到数据的整理
     *
     * @param allCfg
     * @param map
     */
    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, VipLevelConfig> map) {
        VipLevelConfig.Configs = new VipLevelConfig[map.size()];
        for (VipLevelConfig config : map.values()) {
            VipLevelConfig.Configs[config.getLevel()] = config;
        }
    }
}
