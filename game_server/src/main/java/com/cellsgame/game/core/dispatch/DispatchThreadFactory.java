package com.cellsgame.game.core.dispatch;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.common.util.StringUtil;

/**
 * Created by alyx on 17-6-21.
 * 线程工厂
 */
public class DispatchThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final DispatchType dispatchType;

    private DispatchThreadFactory(String groupName, String name, DispatchType dispatchType) {
        this.dispatchType = dispatchType;
        if (StringUtil.isNotEmpty(groupName))
            this.group = new ThreadGroup(groupName);
        else {
            SecurityManager s = System.getSecurityManager();
            this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }
        namePrefix = "pool-" + name + "-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public static DispatchThreadFactory create(String groupName, String name, DispatchType dispatchType) {
        return new DispatchThreadFactory(groupName, name, dispatchType);
    }

    public Thread newThread(Runnable r) {
        DispatchThread t = new DispatchThread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        t.setDispatchType(dispatchType);
        return t;
    }
}
