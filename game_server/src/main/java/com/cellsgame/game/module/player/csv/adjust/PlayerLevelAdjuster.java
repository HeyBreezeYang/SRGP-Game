package com.cellsgame.game.module.player.csv.adjust;

import java.util.Map;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.player.csv.PlayerLevelConfig;

/**
 * File Description.
 *
 * @author Yang
 */
public class PlayerLevelAdjuster implements CfgAdjuster<PlayerLevelConfig> {
    /**
     * 是否需要添加到全局配置缓存
     */
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<PlayerLevelConfig> getProType() {
        return PlayerLevelConfig.class;
    }

    /**
     * 数据调整 -- 理应只涉及到数据的整理
     *
     * @param allCfg
     * @param map
     */
    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, PlayerLevelConfig> map) {
        PlayerLevelConfig.By_Level = new PlayerLevelConfig[map.size() + 1];
        map.values().forEach(config -> {
            if(PlayerLevelConfig.By_Level[config.getLevel()] != null){
                throw new RuntimeException("player level info error.....id : " + config.getId());
            }
            PlayerLevelConfig.By_Level[config.getLevel()] = config;
        });
    }
}
