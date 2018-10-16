package com.cellsgame.game.core.dispatch;

import com.cellsgame.conc.disruptor.DpWorkHandler;
import com.cellsgame.conc.disruptor.SingleDisruptor;
import com.cellsgame.game.core.GameHandler;
import com.cellsgame.game.core.LogHandler;
import com.cellsgame.game.core.RunnableHandler;
import com.cellsgame.game.util.DebugTool;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author Aly on 2017-04-05.
 * 只接受 Runnable
 */
public enum DispatchType {
    GAME(1024, "game_logic", "game_logic_group", ProducerType.MULTI, new BlockingWaitStrategy(), GameHandler.class),
    HTTP(1024, "http_request", "http_request_group", ProducerType.MULTI, new BlockingWaitStrategy(), RunnableHandler.class),
    LOG(1024, "game_log", "GameLogGroup", ProducerType.MULTI, new BlockingWaitStrategy(), LogHandler.class),
    NOTIFY(1024, "notify", "NotifyGroup", ProducerType.MULTI, new BlockingWaitStrategy(), RunnableHandler.class),
    GM_RESPONSE(1024, "gm_response", "GMGroup", ProducerType.MULTI, new BlockingWaitStrategy(), RunnableHandler.class),;
    private SingleDisruptor disruptor;

    DispatchType(int bufferSize, String threadName, String groupName, ProducerType producerType, WaitStrategy waitStrategy, Class<? extends RunnableHandler> handler) {
        try {
            disruptor = new SingleDisruptor(bufferSize,
                    DispatchThreadFactory.create(groupName, threadName, this),
                    producerType, waitStrategy, handler.newInstance());
        } catch (Exception e) {
            DebugTool.throwException("", e);
        }
    }

    DispatchType(int bufferSize, String threadName, String groupName, ProducerType producerType, WaitStrategy waitStrategy, DpWorkHandler... handlers) {
        try {
            disruptor = new SingleDisruptor(bufferSize,
                    DispatchThreadFactory.create(groupName, threadName, this),
                    producerType, waitStrategy, handlers);
        } catch (Exception e) {
            DebugTool.throwException("", e);
        }
    }

    public static void start() {
        for (DispatchType type : DispatchType.values()) {
            type.disruptor.start();
        }
    }

    public static void shutdown() {
        for (DispatchType type : DispatchType.values()) {
            type.disruptor.shutdown();
        }
    }

    public static void tryExeIn(DispatchType type, Runnable run) {
        if (type == null) {
            run.run();
        } else {
            Thread currentThread = Thread.currentThread();
            if (currentThread instanceof DispatchThread) {
                DispatchType curType = ((DispatchThread) currentThread).getDispatchType();
                if (type == curType) {
                    run.run();
                } else {
                    type.dispatch(run);
                }
            } else {
                type.dispatch(run);
            }
        }
    }

    public void dispatch(Runnable runnable) {
        disruptor.publish(runnable);
    }

    public SingleDisruptor getDisruptor() {
        return disruptor;
    }
}
