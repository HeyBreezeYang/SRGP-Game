package com.cellsgame.game.module.sys.funOpen;

import java.util.Arrays;
import java.util.Map;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.util.BoolFunction;

import static com.cellsgame.game.module.sys.funOpen.Matchable.matchAndCheckNext;
import static com.cellsgame.game.module.sys.funOpen.Matchable.matchAndNotCheckNext;

/**
 * Created by alyx on 17-6-13.
 * 功能检查
 */
public abstract class CommandMatcher {
    // 私有初始化
    private CommandMatcher() {
    }

    public static Builder builder(int ModuleID) {
        return new Builder(ModuleID);
    }

    public abstract int getModuleIX();

    /**
     * 是否匹配
     */
    public abstract boolean isMatch(int command, Map originalClientMsg);

    public static class Builder {
        private Matchable matchable;
        private int moduleIX;

        Builder(int moduleID) {
            this.moduleIX = moduleID / ModuleID.MODULE_ID_BASE;
        }


        public Builder allCmd() {
            andThen(new Matchable() {
                @Override
                public MatchType isMatch(int curModuleIX, int command, Map originalClientMsg) {
                    boolean match = (command / ModuleID.MODULE_ID_BASE) == curModuleIX;
                    return matchAndCheckNext(match);
                }
            });
            return this;
        }

        Builder someCmd(int... commands) {
            for (int i : commands) checkCmdInThisModule(i);
            Arrays.sort(commands);
            andThen(new Matchable() {
                @Override
                public MatchType isMatch(int curModuleIX, int command, Map originalClientMsg) {
                    int i = Arrays.binarySearch(commands, command);
                    return matchAndCheckNext(i >= 0);
                }
            });
            return this;
        }

        Builder exceptCmd(int... commands) {
            Arrays.sort(commands);
            for (int command : commands) checkCmdInThisModule(command);

            for (int i : commands) checkCmdInThisModule(i);
            Arrays.sort(commands);
            andThen(new Matchable() {
                @Override
                public MatchType isMatch(int curModuleIX, int command, Map originalClientMsg) {
                    int i = Arrays.binarySearch(commands, command);
                    return matchAndCheckNext(i < 0);
                }
            });
            return this;
        }

        Builder oneCmd(int command) {
            return oneCmd(command, null);
        }

        Builder oneCmd(int command, BoolFunction<Map> clientRowMsg) {
            checkCmdInThisModule(command);
            andThen(new Matchable() {
                @Override
                public MatchType isMatch(int curModuleIX, int command1, Map originalClientMsg) {
                    if (command == command1) {
                        return matchAndNotCheckNext(clientRowMsg == null || clientRowMsg.invoke(originalClientMsg));
                    }
                    return Matchable.MatchType.DIS_MATCH_CHECK_NEXT;
                }
            });
            return this;
        }


        public CommandMatcher build() {
            return new CommandMatcherImpl(moduleIX, matchable);
        }

        // 工具方法
        private void checkCmdInThisModule(int command) {
            if (command / ModuleID.MODULE_ID_BASE != moduleIX)
                throw new RuntimeException("错误的配置 必须配置在统一Module下");
        }

        // 工具方法
        private void andThen(Matchable andThen) {
            if (this.matchable == null) this.matchable = andThen;
            matchable = matchable.andThen(andThen);
        }
    }

    private static class CommandMatcherImpl extends CommandMatcher {
        private int moduleIX;
        private Matchable matchable;

        CommandMatcherImpl(int moduleIX, Matchable matchable) {
            this.moduleIX = moduleIX;
            this.matchable = matchable;
        }

        @Override
        public int getModuleIX() {
            return moduleIX;
        }

        @Override
        public boolean isMatch(int command, Map originalClientMsg) {
            if (matchable == null) {
                return false;
            }
            Matchable.MatchType match = matchable.isMatch(moduleIX, command, originalClientMsg);
            return Matchable.MatchType.MATCH == match;
        }
    }
}
