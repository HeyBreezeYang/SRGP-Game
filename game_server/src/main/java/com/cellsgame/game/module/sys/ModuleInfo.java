package com.cellsgame.game.module.sys;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.csv.ActivityCond;
//import com.cellsgame.game.module.att.SimpleAttPair;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.sys.csv.DirtyWordsConfig;
import com.cellsgame.game.module.sys.csv.FunOpenCfg;
import com.cellsgame.game.module.sys.csv.WeightConfig;
import com.cellsgame.game.module.sys.funOpen.FunOpenParam;
import com.cellsgame.game.module.sys.funOpen.FunType;
import com.cellsgame.game.util.DebugTool;
import com.google.gson.reflect.TypeToken;

import java.util.*;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {

    @Override
    public Module getModuleInfo() {
        return Module.createModule(SystemBO.class)
                .jsonHead()
                .regCusJsonType("", Map.class, new TypeToken<HashMap<Object, Object>>() {
                })
                .regCusJsonType("_funcMap", Map.class, new TypeToken<HashMap<Integer, FuncConfig>>() {
                })
                .regCusJsonType("_fiveElement", Map.class, new TypeToken<HashMap<Integer, Integer>>() {
                })
                .regCusJsonType("_monsters", Map.class, new TypeToken<HashMap<Integer, Integer>>() {
                })
                .regCusJsonType("_intmap", Map.class, new TypeToken<HashMap<Integer, Integer>>() {
                })
                .regCusJsonType("_func", FuncConfig.class, new TypeToken<FuncConfig>() {
                })
                .regCusJsonType("_funcAry", FuncConfig[].class, new TypeToken<FuncConfig[]>() {
                })
                .regCusJsonType("_funcs", List.class, new TypeToken<ArrayList<FuncConfig>>() {
                })
                .regCusJsonType("_funcsAry", List[].class, new TypeToken<ArrayList<FuncConfig>[]>() {
                })
                .regCusJsonType("_strs", String[].class, new TypeToken<String[]>() {
                })
                .regCusJsonType("_intAry", int[][].class, new TypeToken<int[][]>() {
                })
                .regCusJsonType("_intAryList", List.class, new TypeToken<List<int[]>>() {
                })
                .regCusJsonType("_date", Date.class, new TypeToken<Date>() {
                })
//                .regCusJsonType("_attpairs", List.class, new TypeToken<List<SimpleAttPair>>() {
//                })
                .regCusJsonType("_lstIntMap", List.class, new TypeToken<List<Map<Integer, Integer>>>() {
                })
                .regCusJsonType("_funOpen", FunOpenParam.class, new TypeToken<FunOpenParam>() {
                })
                .regCusJsonType("_openConds", List.class, new TypeToken<ArrayList<FunOpenParam>>() {
                })
                .regCusJsonType("_actConds", Map.class, new TypeToken<HashMap<Integer, HashMap<Integer, ActivityCond>>>() {
                })
                .regCusJsonType("_actBevs", Map.class, new TypeToken<HashMap<Integer, HashMap<Integer, ActivityBev>>>() {
                })
                .loadCSVFile()
                .load("Weight.csv", WeightConfig.class)
                .load("DirtyWords.csv", DirtyWordsConfig.class)
                .adjustConfig()
//                .regCusAdjuster(FunOpenCfg.class, false, map -> {
//                    Map<Integer, FunType> ca = new HashMap<>();
//                    for (FunType funType : FunType.values()) {
//                        ca.put(funType.getType(), funType);
//                    }
//                    for (FunOpenCfg cfg : map.values()) {
//                        FunType funType = ca.get(cfg.getFunID());
//                        if (null == funType) {
//                            DebugTool.throwException(" 不支持的功能 功能开启类型:" + cfg.getFunID());
//                        } else {
//                            FunOpenParam open = cfg.getOpen();
//                            // 只设置有效参数
//                            if (open.getCheckType() != null) {
//                                funType.setCfg(open);
//                            } else {
//                                DebugTool.throwException("不支持的功能开启 检查类型:" + open.getType());
//                            }
//                        }
//
//                    }
//                    FunType.initEvtCache();
//                })

                .checker()
                .listener().onStartup(SystemBO::init)
                .build();
    }
}