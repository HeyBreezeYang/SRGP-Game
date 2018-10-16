package com.cellsgame.game.module.sys.funOpen;

import java.util.Map;

/**
 * Created by alyx on 17-6-14.
 * 可匹配的
 */
@FunctionalInterface
public interface Matchable {
    static MatchType matchAndCheckNext(boolean match) {
        return match ? MatchType.MATCH : MatchType.DIS_MATCH_CHECK_NEXT;
    }

    static MatchType matchAndNotCheckNext(boolean match) {
        return match ? MatchType.MATCH : MatchType.DIS_MATCH_NOT_CHECK_NEXT;
    }

    /**
     * @return true表示匹配  null表示拦截
     */
    MatchType isMatch(int curModuleIX, int command, Map originalClientMsg);

    default Matchable andThen(Matchable after) {
        return (curModuleIX, command, originalClientMsg) -> {
            MatchType match = isMatch(curModuleIX, command, originalClientMsg);
            if (match == MatchType.DIS_MATCH_CHECK_NEXT) {
                return after.isMatch(curModuleIX, command, originalClientMsg);
            }
            return match;
        };
    }

    enum MatchType {
        /**
         * 匹配 需要进行拦截
         */
        MATCH,
        /**
         * 不匹配 需要继续检查下一个
         */
        DIS_MATCH_CHECK_NEXT,
        /**
         * 不匹配 但是不需要继续检查
         */
        DIS_MATCH_NOT_CHECK_NEXT,;
    }
}
