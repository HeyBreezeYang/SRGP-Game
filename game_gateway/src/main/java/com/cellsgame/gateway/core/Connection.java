package com.cellsgame.gateway.core;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.gateway.IDestructive;
import com.cellsgame.gateway.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * Created by Yang on 16/6/27.
 */
public final class Connection {
    private static final AtomicInteger SESSION_ID_GENERATOR = new AtomicInteger();
    private ChannelHandlerContext channel;
    private int sessionId;
    private IDestructive attachment;

    public Connection(ChannelHandlerContext channel) {
        this.channel = channel;
        this.sessionId = SESSION_ID_GENERATOR.incrementAndGet();
    }

    /**
     * 不需要手动release此消息。
     * <p>
     * 在消息编码时会自动消费此Message
     *
     * @param message 数据消息
     */
    public void sendMessage(Message message) {
        if (isActive()) 
        	channel.writeAndFlush(message);
        else {
        	ByteBuf content = message.getContent();
        	if(content.refCnt()>0)
        		message.consume();
        }
        
    }


    public void sendOriginalMessage(ByteBuf message) {
        if (isActive()) 
        	channel.channel().writeAndFlush(message);
        else if(message.refCnt()>0)
        	message.release();
    }

    public boolean isActive() {
        return channel != null && channel.channel().isActive();
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public void close() {
        if (channel != null) channel.close();
    }

    public void clear() {
        if (attachment != null) attachment.destroy();
        channel = null;
        attachment = null;
    }

    public ByteBuf newBuffer(int capacity) {
        return channel.alloc().buffer(capacity);
    }

    public ByteBuf newBuffer(byte[] data) {
        if (data == null) return null;
        return newBuffer(data.length).writeBytes(data);
    }

    /**
     * 在连接的IO线程执行任务
     *
     * @param runnable 任务
     */
    public Future execute(Runnable runnable) {
        if (runnable == null) return null;
        return channel.executor().submit(runnable);
    }

    /**
     * 提交一个定时任务
     *
     * @param command 任务
     * @param delay   延时
     * @return 任务状态
     */
    public ScheduledFuture schedule(Runnable command, long delay) {
        if (command == null) return null;
        return channel.executor().schedule(command, delay, TimeUnit.SECONDS);
    }

    @Override
    public String toString() {
        return channel == null ? "destroyed channel" : channel.channel().toString();
    }

    public boolean hasSameDest(Connection connection) {
        String a = toString();
        String b = connection.toString();
        return a.substring(a.indexOf("R:")).equals(b.substring(b.indexOf("R:")));
    }

    public String getRemoteAddress() {
        return channel.channel().remoteAddress().toString();
    }

    public <T extends IDestructive> T getAttachment() {
        return (T) attachment;
    }

    public void setAttachment(IDestructive attachment) {
        this.attachment = attachment;
    }

    public String getRemoteIP() {
        InetSocketAddress address = (InetSocketAddress)channel.channel().remoteAddress();
        if(address == null) return  "";
        String clientIP = address.getAddress().getHostAddress();
        return clientIP;
    }
}
