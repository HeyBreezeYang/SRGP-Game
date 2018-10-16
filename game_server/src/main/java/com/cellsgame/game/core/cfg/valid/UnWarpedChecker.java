package com.cellsgame.game.core.cfg.valid;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.annoation.Check;

/**
 * 自动展开 集合包装
 */
public abstract class UnWarpedChecker extends AnnotationChecker {
    @Override
    protected void checkFieldValue(Object obj, StringBuilder errorInfo, Field field, Check c, BaseCfg cfg, Map<Integer, BaseCfg> refData) {
        if (null == obj) {
            if (c.notNull() || c.size() > 0)
                errorInfo.append(String.format("%nError:CfgID:[%s]\t Class:[%s.%s] \t NotNull: error value is :[NULL]",
                        cfg.getId(), cfg.getClass().getSimpleName(), field.getName()));
            return;
        }
        if (obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            checkSize(len, c, errorInfo, field, cfg);
            for (int i = 0; i < len; i++) {
                Object o = Array.get(obj, i);
                outInfo(errorInfo, field, c, cfg, refData, o);
            }
        } else if (obj instanceof Iterable) {
            Iterable<?> it = (Iterable<?>) obj;
            if (it instanceof Collection) {
                checkSize(((Collection) it).size(), c, errorInfo, field, cfg);
            }
            for (Object o : it) {
                outInfo(errorInfo, field, c, cfg, refData, o);
            }
        } else {
            if (obj instanceof Map)
                checkSize(((Map) obj).size(), c, errorInfo, field, cfg);
            outInfo(errorInfo, field, c, cfg, refData, obj);
        }
    }

    private void checkSize(int size, Check c, StringBuilder errorInfo, Field field, BaseCfg cfg) {
        if (c.size() > 0 && c.size() != size) {
            String info = String.format("%nError:CfgID:[%s]\t Class:[%s.%s] \t size:[%s]  error value is :[%s]",
                    cfg.getId(), cfg.getClass().getSimpleName(), field.getName(), c.size(), size);
            errorInfo.append(info);
        }
    }

    public abstract void outInfo(StringBuilder errorInfo, Field field, Check c, BaseCfg cfg, Map<Integer, BaseCfg> refData, Object obj);
}
