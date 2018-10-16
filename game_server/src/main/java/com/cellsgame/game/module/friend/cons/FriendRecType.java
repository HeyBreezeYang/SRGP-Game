package com.cellsgame.game.module.friend.cons;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.module.sys.cons.SysDateRecType;

public enum FriendRecType implements SysDateRecType {
    /**
     * 好友祝福系统刷新时间
     */
    FRIEND_BLESS_SYS_REST_TIME(1);
    private int code;

    FriendRecType(int code) {
        this.code = code + ModuleID.Friends;
    }

    @Override
    public SysDateRecType getRecType() {
        return this;
    }

    @Override
    public int getCode() {
        return code;
    }
}
