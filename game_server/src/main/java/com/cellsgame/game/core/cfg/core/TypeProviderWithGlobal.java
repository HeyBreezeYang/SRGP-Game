package com.cellsgame.game.core.cfg.core;

import java.lang.reflect.Type;

import com.cellsgame.common.util.csv.TypeProvider;
import com.cellsgame.game.core.module.config.Module;

/**
 * @author Aly on  2016-10-24.
 */
public class TypeProviderWithGlobal extends TypeProvider {
    private TypeProvider globalType;
    private TypeProvider thisModuleType;

    TypeProviderWithGlobal(Module module, TypeProvider globalType) {
        thisModuleType = module.getJsonTypeProvider();
        this.globalType = globalType;
    }

    @Override
    public Class getSetMethodTypeByTypeName(String typeName) {
        Class setMethodTypeByTypeName = thisModuleType == null ? null : thisModuleType.getSetMethodTypeByTypeName(typeName);
        if (null == setMethodTypeByTypeName && null != globalType) {
            return globalType.getSetMethodTypeByTypeName(typeName);
        }
        return setMethodTypeByTypeName;
    }

    @Override
    public Type getJsonTypeByTypeName(String typeName) {
        Type jsonTypeByTypeName = thisModuleType == null ? null : thisModuleType.getJsonTypeByTypeName(typeName);
        if (null == jsonTypeByTypeName && null != globalType) {
            return globalType.getJsonTypeByTypeName(typeName);
        }
        return jsonTypeByTypeName;
    }
}
