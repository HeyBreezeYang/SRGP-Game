package com.cellsgame.gateway;

import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.ServerMessage;
import com.cellsgame.gateway.message.processor.job.MessageProcessorJob;
import com.cellsgame.gateway.message.processor.job.MessageProcessorJobFactory;
import com.cellsgame.gateway.message.processor.job.ServerMessageProcessorJob;

/**
 * Created by Yang on 16/7/5.
 */
public class GameMessageProcessorJobFactory implements MessageProcessorJobFactory<ServerMessage, Void> {
    /**
     * 创建消息处理器任务
     *
     * @param connection 连接
     * @param message
     * @return 消息处理任务
     */
    @Override
    public MessageProcessorJob<ServerMessage, Void> create(Connection connection, ServerMessage message) {
        return new ServerMessageProcessorJob(message);
    }
}
