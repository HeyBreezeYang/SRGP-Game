package com.cellsgame.game.core.cfg.valid.impl.unwarpImpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.annoation.Check;
import com.cellsgame.game.core.cfg.valid.UnWarpedChecker;

/**
 * @author Aly
 */
public class CheckBetween extends UnWarpedChecker {

    @Override
    public void outInfo(StringBuilder errorInfo, Field field, Check c, BaseCfg cfg, Map<Integer, BaseCfg> refData, Object obj) {
        if (null == obj || !(obj instanceof Integer) || c.between()[0] > ((Integer) obj) || ((Integer) obj) > c.between()[1]) {
            String info = String.format("%nError:CfgID:[%s]\t Class:[%s.%s] \t between:[%s]  error value is :[%s]",
                    cfg.getId(), cfg.getClass().getSimpleName(), field.getName(), Arrays.toString(c.between()), obj);
            errorInfo.append(info);
        }
    }

    @Override
    public boolean needCheck(Check c) {
        return c.between().length > 0;
    }
}
