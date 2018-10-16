package com.cellsgame.gateway.core;

import java.util.concurrent.CountDownLatch;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server extends Service {
    private static final int DEFAULT_BOSS_THREADS;
    private static final Logger LOG = LoggerFactory.getLogger(Server.class);
    private volatile boolean running = false;
    private ServerBootstrap bootstrap;
    private int bossThreads;
    private String host;
    private int port;

    static {
        DEFAULT_BOSS_THREADS = Math.max(1, SystemPropertyUtil.getInt("thread.boss", 1));
    }

    public Server(String name) {
        super(name);
    }

    public void port(int port) {
        this.port = port;
    }

    public void host(String host) {
        this.host = host;
    }

    @Override
    protected Server initBootstrap() {
        if (bootstrap == null) bootstrap = new ServerBootstrap();
        bootstrap.group(DEFAULT_USE_EPOLL ? new EpollEventLoopGroup(getBossThreads(), newThreadFactory(bossThreadName)) : new NioEventLoopGroup(getBossThreads(), newThreadFactory(bossThreadName)),
                DEFAULT_USE_EPOLL ? new EpollEventLoopGroup(getWorkerThreads(), newThreadFactory(workerThreadName)) : new NioEventLoopGroup(getWorkerThreads(), newThreadFactory(workerThreadName)))
                .channel(DEFAULT_USE_EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, false) // close TCP keep alive, using app ping
                .option(ChannelOption.TCP_NODELAY, true) // close tcp Nagle
//                .option(ChannelOption.SO_LINGER, 0)// gt 0 = check timeout, then close as step
                .childOption(ChannelOption.SO_KEEPALIVE, false)
                .childOption(ChannelOption.TCP_NODELAY, true)
//                .childOption(ChannelOption.SO_LINGER, 0)
                .childHandler(createInitializer());
        if (DEFAULT_USE_EPOLL)
            bootstrap.childOption(EpollChannelOption.SO_REUSEPORT, true).childOption(EpollChannelOption.TCP_FASTOPEN, 1);
        return this;
    }

    protected ChannelInitializer<SocketChannel> createInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                doInitChannelTimeout(ch);
                doInitChannelCodec(ch);
            }
        };
    }

    ;

    @Override
    protected void doStart() {
        if (running) return;
        running = true;
        ChannelFuture channelFuture = this.host == null ? bootstrap.bind(this.port) : bootstrap.bind(this.host, this.port);
        channelFuture.addListener(new GenericFutureListener<Future<Void>>() {
                                                  @Override
                                                  public void operationComplete(Future<Void> future) throws Exception {
                                                      getLogger().debug(Server.this.logMessage(String.format("server status : %s, listening at : %d", (future.isSuccess() ? "running" : "not ready"), port)));
                                                      if (!future.isSuccess()) Server.this.stop(null);
                                                  }
                                              }
        );
    }

    @Override
    public EventLoopGroup getBossGroup() {
        if (!isInitialized()) throw new RuntimeException("server not initialized");
        return bootstrap.config().group();
    }

    @Override
    public EventLoopGroup getIoWorkerGroup() {
        if (!isInitialized()) throw new RuntimeException("server not initialized");
        return bootstrap.config().childGroup();
    }

    public void stop(final Runnable runnable) {
        if (!running) return;
        running = false;
        final CountDownLatch latch = new CountDownLatch(2);
        bootstrap.config().group().shutdownGracefully().addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(io.netty.util.concurrent.Future future) throws Exception {
                latch.countDown();
                getLogger().debug(logMessage(String.format("boss thread status : %s", future.isSuccess() ? "shutdown" : "running")));
            }
        });
        bootstrap.config().childGroup().shutdownGracefully().addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                latch.countDown();
                getLogger().debug(logMessage(String.format("worker thread status : %s", future.isSuccess() ? "shutdown" : "running")));
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
        super.stop(runnable);
        getLogger().debug(Server.this.logMessage("server stopped"));
    }

    public Logger getLogger() {
        return LOG;
    }

    @Override
    protected boolean trafficLog() {
        return true;
    }

    public int getBossThreads() {
        return Math.max(bossThreads, DEFAULT_BOSS_THREADS);
    }

    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }

}
