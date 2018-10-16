package com.cellsgame.game.module.func;

/**
 * @author Aly on 2017-06-09.
 */
public interface FightCallback extends ICallback {
    // 错误的情况下也进行消息分发
    @Override
    default boolean dispatchException() {
        return true;
    }
}
