package com.cellsgame.game.module.player.csv.adjust;

import java.util.Map;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.cache.CacheName;
import com.cellsgame.game.module.player.csv.PlayerNameConfig;

/**
 * File Description.
 *
 * @author Yang
 */
public class PlayerNameAdjuster implements CfgAdjuster<PlayerNameConfig> {
    /**
     * 是否需要添加到全局配置缓存
     */
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<PlayerNameConfig> getProType() {
        return PlayerNameConfig.class;
    }

    /**
     * 数据调整 -- 理应只涉及到数据的整理
     *
     * @param allCfg
     * @param map
     */
    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, PlayerNameConfig> map) {
        CacheName.loadFromConfig(map);
    }
}
