package com.cellsgame.gateway.message.processor.job;

import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Created by Yang on 16/6/27.
 */
public abstract class MessageProcessorJob<T extends Message, S extends Object> implements Callable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessorJob.class);
    protected T message;
    protected Connection connection;

    public MessageProcessorJob(T message) {
        this.message = message;
        this.connection = message.getConn();
    }

    @Override
    public final S call() {
        try {
            LOGGER.debug("process message in thread : {}", Thread.currentThread().getName());
            return process();
        } catch (Throwable e) {
            LOGGER.error("process message job error: ", e);
        } finally {
        }
        return null;
    }

    protected abstract S process() throws Throwable;

    public Connection getConnection() {
        return this.connection;
    }
}
