package com.cellsgame.game.util;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;

/**
 * @author Aly on  2016-08-27.
 */
public class DebugTool {
    public static void throwException(Logger log, String msg, Throwable e) {
        if (isDebug()) {
            if (null != log)
                log.error(msg, e);
            else {
                System.err.println(msg);
                if (null != e) e.printStackTrace();
            }
        } else {
            throw new RuntimeException(msg, e);
        }
    }

    public static void throwException(String msg, Throwable e) {
        throwException(null, msg, e);
    }

    public static void throwException(String msg) {
        throwException(msg, null);
    }

    public static void throwException(Logger log, String msg) {
        throwException(log, msg, null);
    }

    /**
     * windows 跟Mac 系统 默认启动 debug模式
     */
    public static boolean isDebug() {
        return SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_MAC;
    }
}
