package com.cellsgame.game.core.msgproc.process.asm;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

/**
 * @author Aly on 2017-03-27.
 */
class TheUnSafe {
    private static final Logger log = LoggerFactory.getLogger(TheUnSafe.class);
    private static final Unsafe UNSAFE;

    static {
        Unsafe unsafe;
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
            log.debug("sun.misc.Unsafe.theUnsafe: {}", unsafe != null ? "available" : "unavailable");

            // Ensure the unsafe supports all necessary methods to work around the mistake in the latest OpenJDK.
            // https://github.com/netty/netty/issues/1061
            // http://www.mail-archive.com/jdk6-dev@openjdk.java.net/msg00698.html
            try {
                if (unsafe != null) {
                    unsafe.getClass().getDeclaredMethod("copyMemory", Object.class, long.class, Object.class, long.class, long.class);
                    log.debug("sun.misc.Unsafe.copyMemory: available");
                }
            } catch (NoSuchMethodError | NoSuchMethodException t) {
                log.debug("sun.misc.Unsafe.copyMemory: unavailable");
                throw t;
            }
        } catch (Throwable cause) {
            // Unsafe.copyMemory(Object, long, Object, long, long) unavailable.
            unsafe = null;
        }
        UNSAFE = unsafe;
    }

    static Unsafe getUNSAFE() {
        return UNSAFE;
    }
}
