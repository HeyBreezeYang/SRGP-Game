package com.cellsgame.game.core.cfg.valid;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;
import com.google.common.collect.Table;

/**
 * @author Aly
 *         配置检查
 */
public interface CfgChecker {

    default StringBuilder doCheck(Map<Class<? extends BaseCfg>, List<Field>> needCheckField, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Class<? extends BaseCfg>, List<Field>> entry : needCheckField.entrySet()) {
            for (Field field : entry.getValue()) {
                if (CfgChecker.this.isNeedCheck(field)) {
                    StringBuilder res = CfgChecker.this.doCheck(entry.getKey(), field, allCfgData);
                    if (res != null && res.length() > 0) {
                        sb.append(res);
                    }
                }
            }
        }
        return sb;
    }

    boolean isNeedCheck(Field field);

    StringBuilder doCheck(Class<? extends BaseCfg> clazz, Field field, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData);


}
