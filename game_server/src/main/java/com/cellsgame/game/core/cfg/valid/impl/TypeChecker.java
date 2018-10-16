package com.cellsgame.game.core.cfg.valid.impl;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.core.CfgService;
import com.cellsgame.game.core.cfg.valid.CfgChecker;
import com.google.common.collect.Table;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aly
 */
public class TypeChecker implements CfgChecker {
    private static Logger log = LoggerFactory.getLogger(TypeChecker.class);

    @Override
    public boolean isNeedCheck(Field field) {
        return true;
    }

    @Override
    public StringBuilder doCheck(Class<? extends BaseCfg> clazz, Field field, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        StringBuilder sb = new StringBuilder();
        List<CheckValue> checkProc = getCheckProc(field.getGenericType());
        if (null != checkProc && checkProc.size() > 0) {
            // 找到了
            Map<Integer, BaseCfg> cfgData = allCfgData.row(clazz);
            if (null == cfgData)
                return null;
            for (BaseCfg cfg : cfgData.values()) {
                StringBuilder msg = new StringBuilder();
                try {
                    Object item = CfgService.getFieldValue(field, cfg);
                    doProc(item, checkProc, 0, msg, allCfgData);
                } catch (IllegalAccessException e) {
                    log.error("", e);
                }
                if (msg.length() > 0) {
                    sb.append(String.format("%nError:CfgID:[%s]\t Class:[%s.%s] \t error msg is :[%s]",
                            cfg.getId(), field.getDeclaringClass().getSimpleName(), field.getName(), msg));
                }
            }
        }
        return sb;
    }

    private List<CheckValue> getCheckProc(Type fc) {
        if (fc == null) return null;
        List<CheckValue> proc = GameUtil.createList();
        if (fc instanceof Class) {
            TypeCheckerConfig.TypeChecker<Object> checker = TypeCheckerConfig.getChecker((Class) fc);
            if (checker != null) {
                proc.add(CheckValue.of(CheckProc.RAW, checker));
            }
        } else if (fc instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) fc;
            if (pt.getRawType() == List.class) {
                addIfFind(proc, CheckProc.LIST, pt.getActualTypeArguments()[0]);
            } else if (pt.getRawType() == Map.class) {
                // key
                addIfFind(proc, CheckProc.MAP_KEY, pt.getActualTypeArguments()[0]);
                // vale
                addIfFind(proc, CheckProc.MAP_VALUE, pt.getActualTypeArguments()[1]);
            }
        }
        return proc;
    }

    private void addIfFind(List<CheckValue> proc, CheckProc ch, Type type) {
        List<CheckValue> sub = getCheckProc(type);
        if (null != sub && sub.size() > 0 && sub.get(sub.size() - 1).proc == CheckProc.RAW) {
            proc.add(CheckValue.of(ch, null));
            proc.addAll(sub);
        }
    }

    private void doProc(Object o, List<CheckValue> procs, int ix, StringBuilder msg, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        if (procs == null || procs.size() <= ix || o == null)
            return;
        CheckValue procValue = procs.get(ix);
        ix += 1;
        switch (procValue.proc) {
            case RAW:
                String checkMsg = procValue.checker.getCheckMsg(o, allCfgData);
                if (StringUtils.isNotEmpty(checkMsg))
                    msg.append(o.getClass().getSimpleName()).append(checkMsg);
                break;
            case LIST:
                for (Object ob : ((List) o)) {
                    doProc(ob, procs, ix, msg, allCfgData);
                }
                break;
            case MAP_VALUE:
                for (Object ob : ((Map) o).values()) {
                    doProc(ob, procs, ix, msg, allCfgData);
                }
                break;
            case MAP_KEY:
                for (Object ob : ((Map) o).keySet()) {
                    doProc(ob, procs, ix, msg, allCfgData);
                }
                break;
        }
    }


    // 根据key进行数据检查
    private enum CheckProc {
        RAW, LIST, MAP_VALUE, MAP_KEY
        // 检查类型
    }

    private static class CheckValue {
        private CheckProc proc;
        private TypeCheckerConfig.TypeChecker<Object> checker;

        public static CheckValue of(CheckProc proc, TypeCheckerConfig.TypeChecker<Object> checker) {
            CheckValue checkValue = new CheckValue();
            checkValue.proc = proc;
            checkValue.checker = checker;
            return checkValue;
        }
    }
}
