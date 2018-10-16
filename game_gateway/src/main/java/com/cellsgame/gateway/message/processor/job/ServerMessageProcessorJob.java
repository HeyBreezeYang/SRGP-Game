package com.cellsgame.gateway.message.processor.job;

import com.cellsgame.gateway.ConnectionPool;
import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.ServerMessage;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/27.
 * <p>
 * 将服务器数据转发到指定客户机。
 * <p>
 * ServerMessage transfer to Message
 */
public class ServerMessageProcessorJob extends MessageProcessorJob<ServerMessage, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMessageProcessorJob.class);

    public ServerMessageProcessorJob(ServerMessage message) {
        super(message);
    }

    @Override
    protected Void process() throws Throwable {
        Connection connection = ConnectionPool.getInstance().findConnection(message.getForwardChannel());
        // for test TODO
//        if (connection == null) connection = ConnectionPool.getInstance().head();
        // if client channel not found
        LOGGER.info("process gateway message type[{}]", message.getType());
        if (connection == null) {
            // log
            LOGGER.error("client channel [{}] not found", message.getForwardChannel());
        } else {
            // 查看类型
            if (message.getType() == Message.Type.Close) {
                // 直接断开链接
                connection.close();
            } else {
                // 复制新数据
                ByteBuf buf = connection.newBuffer(message.getDataLength() + 8);
                //
                buf.writeIntLE(message.getLastClientMessageId());
                //
                buf.writeIntLE(message.getLastServerMessageId());
                //
                buf.writeBytes(message.getContent());
                // 转发
                connection.sendMessage(new Message(buf));
            }
        }
        // LOG
//        LOGGER.info("process logic server message, channel [{}], cix [{}], six [{}]", message.getForwardChannel(), message.getLastClientMessageId(), message.getLastServerMessageId());
        // discard this message
        message.consume();
        return null;
    }
}
