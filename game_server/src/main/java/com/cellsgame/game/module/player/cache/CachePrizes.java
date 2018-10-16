package com.cellsgame.game.module.player.cache;

import java.util.Map;

import com.cellsgame.game.module.player.csv.CheckInPrizeConfig;
import com.cellsgame.game.module.player.csv.LoginPrizeConfig;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

/**
 * File Description.
 *
 * @author Yang
 */
public class CachePrizes {
    public static final Map<Integer, LoginPrizeConfig> LOGIN_PRIZE_CONFIG_MAP = Maps.newHashMap();
    public static final Map<Integer, CheckInPrizeConfig> CHECK_IN_PRIZE_CONFIG = Maps.newHashMap();
}
