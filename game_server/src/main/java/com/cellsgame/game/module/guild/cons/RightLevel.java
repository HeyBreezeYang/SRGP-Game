package com.cellsgame.game.module.guild.cons;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限等级
 */
public enum RightLevel {
    Level3(101, Checker.include(GuildRight.DONATE,
            GuildRight.JOIN_GUILD_BOSS, GuildRight.CHAT, GuildRight.OUT_GUILD,
            GuildRight.USE_GUILD_SHOP)),
    Level2(102, Checker.include(RightLevel.Level3, GuildRight.APPROVAL_JOIN_GUILD, GuildRight.TRE_OPEN_TASK_GUILD_BOSS)),

    Level1(103, Checker.exclude(GuildRight.MODIFY_GUILD_NAME,
            GuildRight.DISSOLUTION_GUILD, GuildRight.CHG_OWNER)),
    Level0(104, Checker.exclude(GuildRight.OUT_GUILD)),

    //
    ;

    private Checker checker;
    private int id;

    RightLevel(int id, Checker checker) {
        this.checker = checker;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * @return false 表示没有该权限
     */
    public boolean haveRight(GuildRight right) {
        return checker.haveRight(right);
    }

    private static class Checker {
        private Set<GuildRight> rights;
        private boolean contain;
        private RightLevel parent;

        private static Checker include(RightLevel parent, GuildRight... rights) {
            return getChecker(parent, rights, true);
        }

        private static Checker include(GuildRight... rights) {
            return getChecker(null, rights, true);
        }

        private static Checker exclude(GuildRight... rights) {
            return getChecker(null, rights, false);
        }

        private static Checker getChecker(RightLevel parent, GuildRight[] rights, boolean contains) {
            Checker checker = new Checker();
            checker.parent = parent;
            checker.rights = new HashSet<>(rights.length);
            Collections.addAll(checker.rights, rights);
            checker.contain = contains;
            return checker;
        }

        public boolean haveRight(GuildRight right) {
            boolean have;
            if (contain) {
                have = rights.contains(right);
            } else {
                have = !rights.contains(right);
            }
            if (!have && null != parent) {
                have = parent.haveRight(right);
            }
            return have;
        }
    }
}
