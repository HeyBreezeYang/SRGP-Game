package com.cellsgame.gateway.message;

import java.util.concurrent.Future;

import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.processor.MessageProcessor;
import com.cellsgame.gateway.message.processor.job.MessageProcessorJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/23
 * <p>
 * 如果不指定消息处理器, 消息的处理会在与连接相同的线程处理, 以保证消息的顺序性。
 */
public abstract class MessageHandler<T extends Message, R> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);
    private MessageProcessor<T, R> processors;

    /**
     * 构建消息处理器。
     *
     * @param service 收到消息之后,将消息分发到线程池的服务
     */
    public MessageHandler(MessageProcessor service) {
        if (service == null) LOGGER.warn("message processor service not set, message will be processed in the connection io thread. ");
        this.processors = service;
    }

    public abstract void handshakeMessage(Connection connection, Object msg);

    /**
     * 连接打开事件。
     *
     * @param connection 打开的连接
     */
    public abstract void connectionOpened(Connection connection);

    /**
     * 连接关闭事件。
     *
     * @param connection 关闭的连接
     */
    public abstract void connectionClosed(Connection connection);

    /**
     * 收到新消息。
     *
     * @param conn 当前连接
     * @param msg  消息
     */
    public abstract void messageArrived(Connection conn, T msg);

    /**
     * 发送数据超时
     *
     * @param connection 连接
     */
    public void writeTimeout(Connection connection) {

    }

    /**
     * 异常捕获
     *
     * @param connection 连接
     * @param throwable  异常
     */
    public void exceptionCaught(Connection connection, Throwable throwable) {
        LOGGER.info("exception caught : ", throwable);
        connection.close();
    }

    protected final Future<R> submitJob(final MessageProcessorJob<T, R> job) {
        if (job == null) return null;
        try {
            if (processors == null) {
                job.getConnection().execute(new Runnable() {
                    @Override
                    public void run() {
                        job.call();
                    }
                });
            } else {
                processors.submitJob(job);
            }
        } catch (Exception e) {
            LOGGER.error("submit a message job error:", e);
        }
        return null;
    }
}
