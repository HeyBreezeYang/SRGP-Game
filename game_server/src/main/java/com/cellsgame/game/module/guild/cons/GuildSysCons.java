package com.cellsgame.game.module.guild.cons;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.module.sys.cons.SysDateRecType;

/**
 * Created by alyx on 17-6-26.
 * 公会系统常量记录
 */
public enum GuildSysCons implements SysDateRecType {
    GUILD_REF_DATA_TIME(1);

    private final int id;

    GuildSysCons(int id) {
        this.id = ModuleID.Guild + id;
    }

    @Override
    public GuildSysCons getRecType() {
        return this;
    }

    @Override
    public int getCode() {
        return id;
    }
}
