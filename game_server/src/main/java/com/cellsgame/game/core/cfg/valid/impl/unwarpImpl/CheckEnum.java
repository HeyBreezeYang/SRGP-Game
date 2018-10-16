package com.cellsgame.game.core.cfg.valid.impl.unwarpImpl;

import java.lang.reflect.Field;
import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.core.cfg.annoation.Check;
import com.cellsgame.game.core.cfg.valid.UnWarpedChecker;

/**
 * @author Aly
 */
public class CheckEnum extends UnWarpedChecker {
    @Override
    public void outInfo(StringBuilder errorInfo, Field field, Check c, BaseCfg cfg, Map<Integer, BaseCfg> refData, Object obj) {
        if (null == obj || !(obj instanceof Integer) || null == Enums.get(c.Enum(), obj)) {
            String info = String.format("%nError:CfgID:[%s]\t Class:[%s.%s] \t enum:[%s]  error value is :[%s]",
                    cfg.getId(), cfg.getClass().getSimpleName(), field.getName(), c.Enum(), obj);
            errorInfo.append(info);
        }
    }

    @Override
    public boolean needCheck(Check c) {
        return c.Enum() != Enum.class;
    }
}
