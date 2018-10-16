package com.cellsgame.gateway.message.processor.job;

import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.Message;

/**
 * Created by Yang on 16/6/29.
 */
public interface MessageProcessorJobFactory<T extends Message, S> {

    /**
     * 创建消息处理器任务
     *
     * @param connection 连接
     * @return 消息处理任务
     */
    MessageProcessorJob<T, S> create(Connection connection, T message);
}