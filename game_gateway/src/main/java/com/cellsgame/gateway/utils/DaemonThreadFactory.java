package com.cellsgame.gateway.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Yang on 16/6/27.
 */
public class DaemonThreadFactory implements ThreadFactory {
    private AtomicInteger threadNo = new AtomicInteger(1);
    private final String nameStart;
    private final String nameEnd = "]";

    public DaemonThreadFactory(String poolName) {
        nameStart = "[" + poolName + "-";
    }

    public Thread newThread(Runnable r) {
        String threadName = nameStart + threadNo.getAndIncrement() + nameEnd;
        Thread newThread = new Thread(r, threadName);
        newThread.setDaemon(true);
        if (newThread.getPriority() != Thread.NORM_PRIORITY) {
            newThread.setPriority(Thread.NORM_PRIORITY);
        }
        return newThread;
    }
}
