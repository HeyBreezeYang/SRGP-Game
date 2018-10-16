package com.cellsgame.gateway.message.processor;

import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.processor.job.MessageProcessorJob;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 使用此方式不能保证消息有序列到达。
 *
 * @param <T> 消息类型
 */
public class JavaThreadMessageProcessor<T extends Message> implements MessageProcessor<T, Void> {
    private ExecutorService executor;

    public JavaThreadMessageProcessor(ExecutorService service) {
        this.executor = service;
    }

    @Override
    public Future<Void> submitJob(MessageProcessorJob<T, Void> job) throws Exception {
        return this.executor.submit(job);
    }

    @Override
    public void shutdown() {
        this.executor.shutdown();
    }

    /**
     * 处理器是否完全停止
     */
    @Override
    public boolean isTerminated() {
        return this.executor.isTerminated();
    }
}
