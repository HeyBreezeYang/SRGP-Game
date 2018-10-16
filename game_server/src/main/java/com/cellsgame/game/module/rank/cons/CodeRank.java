package com.cellsgame.game.module.rank.cons;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

/**
 * @author Aly on 2017-03-16.
 */
public enum CodeRank implements ICode {
    LIKE_ADD_NUM_MAX(1),                // 点赞次数不足
    LIKE_ADD_ADDED(2),                  // 已结点过了
    LIKE_NOT_LIKE_SELF(3),                  //不能点赞自己
    NOT_LIKE_ADD_RANK_TYPE(4)
    ;
    private int id;

    CodeRank(int id) {
        this.id = id;
    }

    @Override
    public int getModule() {
        return ModuleID.RANK;
    }

    @Override
    public int getCode() {
        return id;
    }


}
