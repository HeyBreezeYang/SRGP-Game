package com.cellsgame.game.core.cfg.core.proc;

import com.cellsgame.common.util.csv.BaseCfg;
import com.google.common.collect.Table;

public interface CheckCfg<T extends BaseCfg> {
    Class<T> cfgClass();

    CharSequence check(Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfg, T cfg);
}
