package com.cellsgame.game.core.cfg.valid;

import java.lang.reflect.Field;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.annoation.Check;
import com.cellsgame.game.core.cfg.core.CfgService;
import com.google.common.collect.Table;

/**
 * @author Aly
 */
public abstract class AnnotationChecker implements CfgChecker {
    @Override
    public final boolean isNeedCheck(Field field) {
        Check c = field.getAnnotation(Check.class);
        return null != c && needCheck(c);
    }

    @Override
    public StringBuilder doCheck(Class<? extends BaseCfg> clazz, Field field, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        Map<Class<? extends BaseCfg>, Map<Integer, BaseCfg>> rowMap = allCfgData.rowMap();
        Map<Integer, BaseCfg> slfData = rowMap.get(clazz);
        StringBuilder errorInfo = new StringBuilder();
        try {
            Check c = field.getAnnotation(Check.class);
            // CheckRef
            Map<Integer, BaseCfg> refData = rowMap.get(c.ref());
            if (refData == null) {
                for (Map.Entry<Class<? extends BaseCfg>, Map<Integer, BaseCfg>> mapEntry : rowMap.entrySet()) {
                    if (c.ref().isAssignableFrom(mapEntry.getKey())) {
                        if (refData == null) {
                            refData = GameUtil.createSimpleMap();
                        }
                        refData.putAll(mapEntry.getValue());
                    }
                }
            }
            if (refData == null) {
                throw new RuntimeException("RefData not Load : -->" + c.ref());
            }

            if (null != slfData)
                for (BaseCfg cfg : slfData.values()) {
                    Object obj = CfgService.getFieldValue(field, cfg);
                    checkFieldValue(obj, errorInfo, field, c, cfg, refData);
                }
        } catch (Throwable e) {
            throw new RuntimeException("Field:-->" + field.toString(), e);
        }
        return errorInfo;
    }

    protected abstract void checkFieldValue(Object obj, StringBuilder errorInfo, Field field, Check c, BaseCfg cfg, Map<Integer, BaseCfg> refData);

    /**
     * 检查 注解 是否实际需要 检查
     */
    public abstract boolean needCheck(Check c);
}
