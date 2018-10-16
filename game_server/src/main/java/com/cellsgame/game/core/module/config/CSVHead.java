package com.cellsgame.game.core.module.config;

import java.util.HashMap;
import java.util.Map;

import com.cellsgame.common.util.csv.TypeProvider;
import com.cellsgame.game.core.cfg.core.TypeProviderWithGlobal;
import com.google.gson.reflect.TypeToken;

/**
 * @author Aly on  2016-10-24.
 */
public class CSVHead<T> extends ModuleConfig<T> {
    private Map<String, CusJsonTypeConfig> cusJonConfig;

    CSVHead(ModuleBuilder<T> moduleBuilder) {
        super(moduleBuilder);
    }

    @Override
    void buildTO(Module<T> module) {
        if (cusJonConfig != null) {
            TypeProvider typeProvider = new TypeProvider();
            for (CusJsonTypeConfig config : cusJonConfig.values()) {
                typeProvider.regCusJsonType(config.typeName, config.setParamClassType, config.type);
            }
            module.jsonTypeProvider = typeProvider;
        }
    }


    /**
     * 注册本模块下的自定义json表头
     * <p>
     * 根据目前的执行逻辑 子模块的自定义模块会覆盖 全局模块的表头
     *
     * @see TypeProviderWithGlobal
     */
    public CSVHead<T> regCusJsonType(String typeName, Class<?> setParamClassType, TypeToken<?> typeToken) {
        if (!setParamClassType.isAssignableFrom(typeToken.getRawType())) {
            throw new RuntimeException("参数类型不匹配" + setParamClassType + "-->" + typeToken);
        }
        if (cusJonConfig == null) {
            cusJonConfig = new HashMap<>();
        }
        if (cusJonConfig.containsKey(typeName)) {
            throw new RuntimeException(" 重复的TypeName: [" + typeName + "]");
        } else {
            CusJsonTypeConfig value = new CusJsonTypeConfig();
            value.type = typeToken;
            value.setParamClassType = setParamClassType;
            value.typeName = typeName;
            cusJonConfig.put(typeName, value);
        }
        return this;
    }


    public CSVFileLoader<T> loadCSVFile() {
        return new CSVFileLoader<>(moduleBuilder);
    }

    private static class CusJsonTypeConfig {
        private String typeName;
        private Class<?> setParamClassType;
        private TypeToken<?> type;
    }

}
