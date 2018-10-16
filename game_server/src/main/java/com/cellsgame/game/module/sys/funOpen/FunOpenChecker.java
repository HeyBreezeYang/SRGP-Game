package com.cellsgame.game.module.sys.funOpen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.player.PlayerUtil;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Created by alyx on 17-6-13.
 * 功能开启检查器
 */
public class FunOpenChecker {

    private static FunOpenChecker checker;
    private Map<Integer, List<FunType>> cache = new HashMap<>();

    private FunOpenChecker() {
        Map<Integer, List<FunType>> cache = new HashMap<>();
        for (FunType funType : FunType.values()) {
            //
            if (funType.matchers != null && funType.param != null) {
                for (CommandMatcher matcher : funType.matchers) {
                    int moduleIX = matcher.getModuleIX();
                    List<FunType> funTypes = cache.computeIfAbsent(moduleIX, new Function<Integer, List<FunType>>() {
                        @Override
                        public List<FunType> apply(Integer integer) {
                            return new ArrayList<>();
                        }
                    });
                    funTypes.add(funType);
                }
            }
        }
        //
        for (Map.Entry<Integer, List<FunType>> entry : cache.entrySet()) {
            List<FunType> value = entry.getValue();
            entry.setValue(ImmutableList.copyOf(value));
        }
        this.cache = ImmutableMap.copyOf(cache);
    }

    public static FunOpenChecker getChecker() {
        return checker;
    }

    public static void init() {
        checker = new FunOpenChecker();
    }

    /**
     * 检查 命令是否被限制
     */
    public void checkCommandLimit(MessageController controller, int commandID, Map originalClientMsg) throws LogicException {
        int ix = commandID / ModuleID.MODULE_ID_BASE;
        List<FunType> funTypes = cache.get(ix);
        PlayerVO pvo = null;
        if (null == funTypes || funTypes.size() == 0) {
            return;
        }
        for (FunType type : funTypes) {
            boolean match = false;
            for (CommandMatcher matcher : type.matchers) {
                match = matcher.isMatch(commandID, originalClientMsg);
                if (match) break;
            }
            FunOpenParam param = type.param;
            if (match && null != param) {
                if (null == pvo) pvo = PlayerUtil.checkAndGetPlayerVO(controller);
                CodeGeneral.General_FUNCION_NOT_Open.throwIfTrue(!checkPre(param, pvo));
            }
        }
    }

    /**
     * 前置条件是否满足
     */
    public boolean checkPre(FunOpenParam param, PlayerVO pvo) {
        if (null == param) return true;
        switch (param.getCheckType()) {
            case PLAYER_LV:
//                return pvo.getLevel() >= param.getValue();
        }
        return false;
    }

}
