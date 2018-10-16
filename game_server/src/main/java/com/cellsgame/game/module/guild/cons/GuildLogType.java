package com.cellsgame.game.module.guild.cons;

/**
 * @author Aly on 2017-03-09.
 */
public enum GuildLogType {
    /**
     * 加入
     */
    JoinGuild(1),
    /**
     * 修改权限
     */
    ChangeRight(2),
    /**
     * 离开
     */
    MyOutGuild(3),
    /**
     * 开除
     */
    OutGuild(4),
    /**
     * 工会升级
     */
    UpLevel(5),
    /**
     * BOSS开启
     */
    OpenBoss(6),
    /**
     * BOSS击杀
     */
    KillBoss(7),

    ;

    private final int id;

    GuildLogType(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }
}
