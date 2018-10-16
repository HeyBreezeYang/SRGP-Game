package com.cellsgame.game.cache;


import com.cellsgame.game.module.activity.cons.ActivityBevType;
import com.cellsgame.game.module.activity.cons.ActivityCondType;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.cons.SysType;
import com.cellsgame.game.module.chat.cons.ChatType;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.depot.cons.DepotType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.func.formula.FormulaType;
import com.cellsgame.game.module.guild.cons.RightLevel;
import com.cellsgame.game.module.player.cons.FuncTimes;
import com.cellsgame.game.module.player.cons.PlayerState;
import com.cellsgame.game.module.quest.cons.BehavType;
import com.cellsgame.game.module.quest.cons.CditType;
import com.cellsgame.game.module.rank.cons.RankType;
import com.cellsgame.game.module.shop.ShopRefreshRule;
import com.cellsgame.game.module.sys.funOpen.FunOpenCheckType;
import com.cellsgame.game.util.DebugTool;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 枚举缓存
 * <p>
 * 使用到的枚举  理应都放到此处加载到内存
 */
public class Enums {
    private static final Map<Class<? extends Enum<?>>, String> config = ImmutableMap
            .<Class<? extends Enum<?>>, String>builder()
            .put(SyncFuncType.class, "type")
            .put(ShopRefreshRule.class, "type")
            .put(CurrencyType.class, "type")
            .put(DepotType.class, "gvoClass")
            .put(RightLevel.class, "id")
            .put(ChatType.class, "value")
            .put(FunOpenCheckType.class, "id")
            .put(ActivityCondType.class, "type")
            .put(ActivityBevType.class, "type")
            .put(ActivityScopeType.class, "type")
            .put(SysType.class, "type")
            .put(PlayerState.class, "type")
            .put(CditType.class,"type")
            .put(BehavType.class,"type")
            .put(FormulaType.class,"id")
            .put(FuncTimes.class, "id")
            .put(RankType.class,"type")
            .build();
    private static final Map<Class<? extends Enum<?>>, Map<Object, Object>> cache;
    private static Logger log = LoggerFactory.getLogger(Enums.class);

    static {
        ImmutableMap.Builder<Class<? extends Enum<?>>, Map<Object, Object>> outBuilder = ImmutableMap.builder();
        for (Entry<Class<? extends Enum<?>>, String> en : config.entrySet()) {
            try {
                Field f = ReflectionUtils.findField(en.getKey(), en.getValue());
                f.setAccessible(true);
                Enum<?>[] es = en.getKey().getEnumConstants();
                Map<Object, Object> map = new HashMap<>();

                for (Enum<?> e : es) {
                    Object val = f.get(e);
                    Object put = map.put(val, e);
                    if (null != put) {
                        DebugTool.throwException("枚举配置错误, 重复的Key:  clazz:" + en.getKey() + " val:" + val, null);
                    }
                }
                outBuilder.put(en.getKey(), ImmutableMap.builder().putAll(map).build());
            } catch (Exception e) {
                log.error("", e);
            }
        }
        cache = outBuilder.build();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T get(Class<T> clazz, Object val) {
        Map<Object, Object> map = cache.get(clazz);
        return map == null ? null : (T) map.get(val);
    }
}