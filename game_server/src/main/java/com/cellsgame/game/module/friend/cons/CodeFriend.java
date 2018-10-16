package com.cellsgame.game.module.friend.cons;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

/**
 * @author Aly @2017-02-07.
 */
public enum CodeFriend implements ICode {
    BAD_REQ(1),                 // 请求错误
    MAX_FRIEND_LIST_SIZE(2),    // 自己好友列表达到最大
    MAX_FRIEND_LIST_SIZE_OTHER(3),  // 别人好友列表达到最大
    MAX_BLACK_LIST_SIZE(4),     // 黑名单达到最大
    IN_MY_BLACK_LIST(5),        // 在黑名单中
    NOT_ALLOWED_ADD(6),         // 不允许添加
    NOT_FRIEND(7),              // 不是好友
    Max_BLESS_NUM(8),           // 达到最大祝福次数
    BLESS_NOT_AT_GET_PRIZE_TIME(9), // 还没到领奖时间
    Max_BLESS_GET_PRIZE_TIMES(10), //达到最大的领奖次数
    BLESS_GET_PRIZE_IN_CD(11),//领奖冷却中
    IN_MY_FRIEND_LIST(12),        // 在好友列表中
    ;
    private int code;

    CodeFriend(int code) {
        this.code = code;
    }

    @Override
    public int getModule() {
        return ModuleID.Friends;
    }

    @Override
    public int getCode() {
        return code;
    }
}
