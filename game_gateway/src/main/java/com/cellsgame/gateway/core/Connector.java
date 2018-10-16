package com.cellsgame.gateway.core;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/27.
 */
public class Connector extends Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(Connector.class);
    private static final int DEFAULT_RECONNECT_DELAY = 1;
    private volatile boolean stopped = false;
    private Bootstrap bootstrap;
    private Map<String, InetSocketAddress> addresses;
    private Map<String, Integer> reconnectDelaySetting = Maps.newConcurrentMap();
    private EventLoopGroup group;
    private Class<? extends Channel> channel;

    public Connector(String name, Map<String, InetSocketAddress> addresses) {
        super(name);
        this.addresses = addresses;
    }


    @Override
    protected void doStart() {
        if (stopped) throw new RuntimeException("connect stopped, can not connect to any host");
        for (Map.Entry<String, InetSocketAddress> addressEntry : addresses.entrySet()) {
            connect(addressEntry.getKey(), addressEntry.getValue());
        }
    }

    /**
     * 手动/动态连接到指定服务
     *
     * @param addressId 描述
     * @param address   地址
     */
    public void connect(final String addressId, final InetSocketAddress address) {
        // 如果不包括目标地址
        if (!containsAddressId(addressId)) {
            // 将其添加到目标地址池
            addresses.put(addressId, address);
            //
            reconnectDelaySetting.put(addressId, DEFAULT_RECONNECT_DELAY);
            //
            LOGGER.error("connect to a new address[key={}, dest={}]", addressId, address);
        }
        // 开始连接
        bootstrap.connect(address).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                LOGGER.debug("[{}] = ip [{}], port[{}], connected status : {}", addressId, address.getAddress(), address.getPort(), future.isSuccess());
                // 如果连接失败 // 自动重连
                if (!future.isSuccess()) tryReconnect(addressId);
            }
        });
    }

    @Override
    protected Connector initBootstrap() {
        if (channel == null) throw new NullPointerException("Socket Channel Class not set");
        if (group == null) throw new NullPointerException("socket channel group not set");
        if (bootstrap == null) bootstrap = new Bootstrap();
        bootstrap.channel(channel)
                .group(group)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_LINGER, 0)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        doInitChannelTimeout(ch);
                        doInitChannelCodec(ch);
                    }
                });
        return this;
    }

    /**
     * 删除连接
     *
     * @param addressId 连接ID
     * @return 连接地址
     */
    public InetSocketAddress closeConnector(String addressId) {
        InetSocketAddress address = addresses.remove(addressId);
        reconnectDelaySetting.remove(addressId);
        onAddressRemove(addressId);
        return address;
    }

    public InetSocketAddress getAddress(String addressId) {
        return addresses.get(addressId);
    }

    protected void onAddressRemove(String addressId) {
    }

    public Connector group(EventLoopGroup group) {
        this.group = group;
        return this;
    }

    public Connector channel(Class<? extends Channel> channelClass) {
        if (channelClass == null) {
            throw new NullPointerException("channelClass");
        }
        this.channel = channelClass;
        return this;
    }

    @Override
    public EventLoopGroup getBossGroup() {
        if (this.group == null) throw new NullPointerException("group not set");
        return this.group;
    }

    @Override
    public EventLoopGroup getIoWorkerGroup() {
        throw new RuntimeException("no io worker group for connector");
    }

    public final ScheduledFuture schedule(Runnable job, long seconds) {
        //
        if (stopped) return null;
        return bootstrap.config().group().schedule(job, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void stop(final Runnable runnable) {
        if (stopped) return;
        stopped = true;
        final CountDownLatch latch = new CountDownLatch(1);
        //noinspection unchecked
        bootstrap.config().group().shutdownGracefully().addListener(new GenericFutureListener() {
            /**
             * Invoked when the operation associated with the {@link Future} has been completed.
             *
             * @param future the source {@link Future} which called this callback
             */
            @Override
            public void operationComplete(Future future) throws Exception {
                latch.countDown();
                LOGGER.debug(logMessage(String.format("connector thread status : %s", future.isSuccess() ? "shutdown" : "running")));
            }
        });

        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }
        Connector.super.stop(runnable);
        bootstrap = null;
        LOGGER.debug(Connector.this.logMessage("connector stopped"));
    }

    protected void tryReconnect(final String addressId) {
        // if need tryReconnect
        if (getReconnectDelay(addressId) > 0) {
            // 获取连接地址
            final InetSocketAddress address = addresses.get(addressId);
            // 如果不需要连接
            if (address == null) return;
            // tryReconnect log
            LOGGER.debug("[{}, ip={}, port={}] lost connection, retry after {} seconds", addressId, address.getAddress(), address.getPort(), getReconnectDelay(addressId));
            // schedule retry job
            ScheduledFuture reconnectFuture = schedule(new Runnable() {
                @Override
                public void run() {
                    Connector.this.connect(addressId, address);
                }
            }, getReconnectDelay(addressId));
            // event listener
            if (reconnectFuture != null) reconnectFuture.addListener(new GenericFutureListener<Future<?>>() {
                @Override
                public void operationComplete(Future<?> future) throws Exception {
                    if (!future.isSuccess()) {
                        LOGGER.error("schedule task run error, : ", future.cause());
                    }
                }
            });
        }
    }

    public boolean containsAddressId(String key) {
        return addresses.containsKey(key);
    }

    public Set<String> allServerAddressId() {
        return Sets.newHashSet(addresses.keySet());
    }

    public boolean isStopped() {
        return stopped;
    }

    public int getReconnectDelay(String addressId) {
        return StringUtils.isEmpty(addressId) ? 0 : reconnectDelaySetting.getOrDefault(addressId, 0);
    }
}
