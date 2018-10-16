package com.cellsgame.game.module.chat.thread;

import com.cellsgame.conc.thread.CustomThreadFactory;
import com.cellsgame.conc.thread.CustomThreadPool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class TaskManager {

    private static CustomThreadFactory cf = new CustomThreadFactory("chatTask");

    private static CustomThreadPool threadPool;

    private static int TASK_LIMIT = 1000;

    public static void init(){
        threadPool = new CustomThreadPool(5,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                cf);
    }

    public static void shutdown(){
        if (threadPool == null || threadPool.isShutdown())
            return;
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(300, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {
        }
    }

    public static void commit(ChatData data){
        int taskSize = threadPool.getQueue().size();
        if(taskSize >= TASK_LIMIT)
            return;
        threadPool.execute(new ChatTask(data));
    }




}
