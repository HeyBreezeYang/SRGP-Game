package com.cellsgame.game.cache;

/**
 * Created by yfzhang on 2017/8/10.
 */
public class CacheServerState {

    public static volatile int STATE = 0;

    public static boolean isClose() {
        return STATE == 4;
    }
}
