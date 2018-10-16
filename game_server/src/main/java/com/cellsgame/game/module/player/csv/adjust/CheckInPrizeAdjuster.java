package com.cellsgame.game.module.player.csv.adjust;

import java.util.Map;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.player.cache.CachePrizes;
import com.cellsgame.game.module.player.csv.CheckInPrizeConfig;

/**
 * File Description.
 *
 * @author Yang
 */
public class CheckInPrizeAdjuster implements CfgAdjuster<CheckInPrizeConfig> {
    /**
     * 是否需要添加到全局配置缓存
     */
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<CheckInPrizeConfig> getProType() {
        return CheckInPrizeConfig.class;
    }

    /**
     * 数据调整 -- 理应只涉及到数据的整理
     *
     * @param allCfg
     * @param map
     */
    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, CheckInPrizeConfig> map) {
        map.values().forEach(v -> CachePrizes.CHECK_IN_PRIZE_CONFIG.put(v.getDays(), v));
    }
}
