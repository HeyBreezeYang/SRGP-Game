package com.cellsgame.gateway.message.processor;

import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.processor.job.MessageProcessorJob;

import java.util.concurrent.Future;

/**
 * 提交一个消息处理任务时, 如果有同步需求,需要子类自己实现
 */
public interface MessageProcessor<M extends Message, R> {
    /**
     * 将Job提交到处理器
     *
     * @param job 需要处理的任务
     * @return 提交结果
     * @throws Exception 异常
     */
    Future<R> submitJob(MessageProcessorJob<M, R> job) throws Exception;

    /**
     * 关闭处理理,不再接受新的Job
     */
    void shutdown();

    /**
     * 处理器是否完全停止
     */
    boolean isTerminated();
}
