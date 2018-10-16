package com.cellsgame.game.core.cfg.valid.impl;

import java.lang.reflect.Field;
import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.annoation.Check;
import com.cellsgame.game.core.cfg.core.CfgService;
import com.cellsgame.game.core.cfg.valid.CfgChecker;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * aly @ 16-11-15.
 */
public class CheckNull implements CfgChecker {
    private static final Logger log = LoggerFactory.getLogger(CheckNull.class);

    @Override
    public boolean isNeedCheck(Field field) {
        Check c = field.getAnnotation(Check.class);
        return null != c && c.notNull();
    }

    @Override
    public StringBuilder doCheck(Class<? extends BaseCfg> clazz, Field field, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        StringBuilder sb = null;
        Map<Integer, BaseCfg> cfgData = allCfgData.row(clazz);
        if (null != cfgData) {
            for (BaseCfg cfg : cfgData.values()) {
                try {
                    Object item = CfgService.getFieldValue(field, cfg);
                    if (null == item) {
                        String info = String.format("%nError:CfgID:[%s]\t Class:[%s.%s] \t error value is :[null]",
                                cfg.getId(), cfg.getClass().getSimpleName(), field.getName());
                        if (null == sb) sb = new StringBuilder();
                        sb.append(info);
                    }
                } catch (IllegalAccessException e) {
                    log.error("", e);
                }
            }

        }
        return sb;
    }

}
