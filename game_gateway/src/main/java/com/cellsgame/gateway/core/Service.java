package com.cellsgame.gateway.core;

import java.util.concurrent.ThreadFactory;

import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.MessageCodecFactory;
import com.cellsgame.gateway.message.MessageHandler;
import com.cellsgame.gateway.message.handler.MinuteAvgTrafficHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.*;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.SystemPropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/27.
 */
public abstract class Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    public static final boolean DEFAULT_USE_EPOLL;
    public static final int DEFAULT_WORKER_THREADS;
    private MessageCodecFactory codecFactory;
    private com.cellsgame.gateway.message.MessageHandler handler;
    private volatile boolean initialized = false;
    protected String bossThreadName;
    protected String workerThreadName;
    private int workerThreads;
    private int readTimeout;
    private int writeTimeout;
    private int pingTime;
    private String name;

    static {
        DEFAULT_USE_EPOLL = SystemPropertyUtil.getBoolean("io.epoll", false);
        DEFAULT_WORKER_THREADS = Math.max(1, SystemPropertyUtil.getInt("thread.worker", Runtime.getRuntime().availableProcessors() * 2));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Service is use e poll [{}]", DEFAULT_USE_EPOLL);
            LOGGER.debug("Service io worker thread number [{}]", DEFAULT_WORKER_THREADS);
        }
    }

    public Service(String name) {
        if (StringUtils.isEmpty(name)) throw new RuntimeException("not set name of server ");
        this.name = name;
        bossThreadName = String.format("[%s] %s", name, "boss-thread");
        workerThreadName = String.format("[%s] %s", name, "io-worker-thread");
    }


    private Service initialize() {
        if (handler == null) throw new RuntimeException("message handler not set");
        if (codecFactory == null) throw new RuntimeException("message codec factory not set");
        if (initialized) return this;
        initialized = true;
        return initBootstrap();
    }

    protected abstract Service initBootstrap();


    protected void doInitChannelTimeout(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        if (Math.max(readTimeout, pingTime) > 0)
            pipeline.addLast("idle", new IdleStateHandler(Math.max(0, readTimeout), Math.max(0, pingTime), 0));
        if (writeTimeout > 0)
            pipeline.addLast("write", new WriteTimeoutHandler(Math.max(0, writeTimeout)));
    }


    protected final void doInitChannelCodec(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        if (trafficLog()) pipeline.addLast(new MinuteAvgTrafficHandler());
        pipeline.addLast("encoder", codecFactory.createEncoder());
        pipeline.addLast("decoder", codecFactory.createDecoder());
        pipeline.addLast("message", new Handler());
    }

    protected boolean trafficLog() {
        return false;
    }

    public final void start() {
        if (!initialized) initialize();
        doStart();
    }

    /**
     * start server real logic
     */
    protected abstract void doStart();

    public void stop(Runnable callback) {
        codecFactory = null;
        handler = null;
        if (callback != null) callback.run();
    }

    public abstract EventLoopGroup getBossGroup();

    public abstract EventLoopGroup getIoWorkerGroup();

    protected ThreadFactory newThreadFactory(String poolName) {
        return new DefaultThreadFactory(poolName);
    }

    public MessageCodecFactory getCodecFactory() {
        return codecFactory;
    }

    protected String logMessage(String message) {
        return String.format("[%s] %s", name, message);
    }

    public void setCodecFactory(MessageCodecFactory codecFactory) {
        this.codecFactory = codecFactory;
    }

    public MessageHandler getHandler() {
        return handler;
    }

    public void setHandler(MessageHandler handler) {
        this.handler = handler;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public int getWorkerThreads() {
        return Math.max(workerThreads, DEFAULT_WORKER_THREADS);
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getPingTime() {
        return pingTime;
    }

    public void setPingTime(int pingTime) {
        this.pingTime = pingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    private class Handler extends ChannelInboundHandlerAdapter {

        final AttributeKey<Connection> KEY_CONN = AttributeKey.valueOf("KEY_CONN");

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Connection connection = new Connection(ctx);
            ctx.channel().attr(KEY_CONN).set(connection);
            handler.connectionOpened(connection);
        }


        @Override
        public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
            handler.connectionClosed(ctx.channel().attr(KEY_CONN).getAndRemove());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if (cause instanceof WriteTimeoutException) {
                handler.writeTimeout(ctx.channel().attr(KEY_CONN).get());
            } else {
                handler.exceptionCaught(ctx.channel().attr(KEY_CONN).get(), cause);
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Connection connection = ctx.channel().attr(KEY_CONN).get();
            // 如果是指定格式消息
            if (msg instanceof Message) {
                Message message = (Message) msg;
                message.setConn(connection);
                handler.messageArrived(connection, message);
            } else {
                handler.handshakeMessage(connection, msg);
            }
        }


        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                LOGGER.info("userEventTriggered idle state[{}]", e.state());
                if (e.state() == IdleState.READER_IDLE) {
                    ctx.channel().attr(KEY_CONN).get().close();
                } else if (e.state() == IdleState.WRITER_IDLE) {
//                    ctx.writeAndFlush(ctx.alloc().buffer().writeByte(100));
                }
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
