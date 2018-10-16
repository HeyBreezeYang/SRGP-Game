package com.cellsgame.game.core.cfg.valid.impl;

import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.cache.Enums;
//import com.cellsgame.game.module.att.AttModule;
//import com.cellsgame.game.module.att.SimpleAttPair;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.google.common.collect.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aly on  2016-08-20.
 */
class TypeCheckerConfig {
    private static final Map<Class<?>, TypeChecker<?>> config = new HashMap<>();

    static {
        TypeCheckerConfig.addConfig(FuncConfig.class, (funcConfig, allCfgData) -> {
            SyncFuncType type = Enums.get(SyncFuncType.class, funcConfig.getType());
            if (type == null) {
                return "-- TYPE is null>>>>" + JSONUtils.toJSONString(funcConfig);
            }
            return type.getFunc().checkCfg(funcConfig, allCfgData);   // 功能参数检查
        });
//        TypeCheckerConfig.addConfig(Condition.class, (funcConfig, allCfgData) -> {
//            if (funcConfig.getProperty() == null) {
//                return "-- quest condition property is null>>>>" + JSONUtils.toJSONString(funcConfig);
//            }
//            return null;   // 功能参数检查
//        });
//        TypeCheckerConfig.addConfig(SimpleAttPair.class, (simpleAttPair, allCfgData) -> {
//            if (simpleAttPair.getModuleType() == null) {
//                return "属性指定计算模块 未指定";
//            } else if (simpleAttPair.getModuleType() == AttModule.Hero.EQUIP) {
////                EquipType equipType = Enums.get(EquipType.class, simpleAttPair.getParam());
////                if (null == equipType) {
////                    return "属性计算模块为装备时, 未指定装备位置";
////                }
//            }
//            return null;
//        });
    }

    private static <T> void addConfig(Class<T> clazz, TypeChecker<T> consumer) {
        config.put(clazz, consumer);
    }

    @SuppressWarnings("unchecked")
    static <T> TypeChecker<T> getChecker(Class clazz) {
        return (TypeChecker<T>) config.get(clazz);
    }


    interface TypeChecker<T> {
        /**
         * 返回值不为空 那边表示这个检查是有错误的  需要上层打印输出
         *
         * @see org.apache.commons.lang3.StringUtils#isNotEmpty(CharSequence)
         */
        String getCheckMsg(T t, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData);
    }


}
