package com.cellsgame.game.core.module.i;

/**
 * Aly @2016-11-23.
 */
public interface SysListener<T> {
    void onStartup(T bean);

    void onShutdown(T bean);
}
