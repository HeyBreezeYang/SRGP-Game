package com.cellsgame.game.module.sys.funOpen;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.util.BoolFunction;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by alyx on 17-6-13.
 * 功能类型
 */
public enum FunType {
    /**
     * 角色  服务器未拦截
     */
    PLAYER(3),

    /**
     * 公会
     *
     * @see com.cellsgame.game.module.guild.ModuleInfo
     */
    GUILD(6),
    /**
     * 每日任务 服务器未拦截
     */
    DAILY_TASK(7),
    /**
     * 好友
     *
     * @see com.cellsgame.game.module.friend.ModuleInfo
     */
    FRIEND(17),
    /**
     * 排行榜
     *
     * @see com.cellsgame.game.module.rank.ModuleInfo
     */
    RANK(18, CommandMatcher.builder(ModuleID.RANK).oneCmd(Command.RANK_GET_RANK_LIST, new BoolFunction<Map>() {
        @Override
        public boolean invoke(Map map) {
            return true;
        }
    }).build()),

    ;
    // 根据配置生成的
    private static Map<FunOpenCheckType, List<FunType>> openFunCache = new EnumMap<>(FunOpenCheckType.class);
    // 拦截的CMD
    CommandMatcher[] matchers;
    // 拦截的参数 配置档配置
    FunOpenParam param;
    /**
     * 开启的时候执行的逻辑
     *
     * @see com.cellsgame.game.core.module.config.ModuleListener#listenModuleOpen(FunType, FunOpenOp2)
     */
    FunOpenOp listenOpen;
    // ID
    private int type;

    FunType(int type) {
        this.type = type;
    }

    FunType(int type, CommandMatcher... matchers) {
        this.type = type;
        this.matchers = matchers;
    }

    public static void initEvtCache() {
        for (FunType funType : FunType.values()) {
            FunOpenParam param = funType.param;
            // 没有配置 或则配置错误
            if (param == null || funType.listenOpen == null) {
                continue;
            }
            List<FunType> types = openFunCache.computeIfAbsent(param.getCheckType(), new Function<FunOpenCheckType, List<FunType>>() {
                @Override
                public List<FunType> apply(FunOpenCheckType funOpenCheckType) {
                    return new ArrayList<>();
                }
            });
            types.add(funType);
        }
    }

    public static List<FunType> getByCheckType(FunOpenCheckType evtType) {
        return openFunCache.get(evtType);
    }

    public int getType() {
        return type;
    }

    public void setCfg(FunOpenParam param) {
        this.param = param;
    }

    /**
     * 优先使用默认值
     */
    public void defaultOrSet(CommandMatcher... matchers) {
        if (this.matchers == null || this.matchers.length == 0) {
            this.matchers = matchers;
        }
    }

    public void setOpenFun(FunOpenOp openFun) {
        listenOpen = openFun;
    }

    public boolean checkPre(PlayerVO pvo) {
        return FunOpenChecker.getChecker().checkPre(param, pvo);
    }
}
