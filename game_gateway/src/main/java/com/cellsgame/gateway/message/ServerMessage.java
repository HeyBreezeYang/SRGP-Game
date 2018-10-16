package com.cellsgame.gateway.message;

import com.cellsgame.gateway.core.Connection;
import io.netty.buffer.ByteBuf;

/**
 * Created by Yang on 16/6/27.
 */
public final class ServerMessage extends Message {
    /**
     *
     */
    private static final long serialVersionUID = -5489078160352713381L;
    private int forwardChannel;
    private int lastClientMessageId;
    private int lastServerMessageId;

    public ServerMessage(ByteBuf content) {
        this(0, content);
    }

    public ServerMessage(int forwardChannel, ByteBuf content) {
        this(Type.Message, forwardChannel, content);
    }

    public ServerMessage(Type type, int forwardChannel, ByteBuf data) {
        super(type, null, data);
        setForwardChannel(forwardChannel);
    }

    public ServerMessage setConn(Connection conn) {
        super.setConn(conn);
        return this;
    }

    public int getForwardChannel() {
        return forwardChannel;
    }

    public void setForwardChannel(int forwardChannel) {
        this.forwardChannel = forwardChannel;
    }

    public int getLastClientMessageId() {
        return lastClientMessageId;
    }

    public void setLastClientMessageId(int lastClientMessageId) {
        this.lastClientMessageId = lastClientMessageId;
    }

    public int getLastServerMessageId() {
        return lastServerMessageId;
    }

    public void setLastServerMessageId(int lastServerMessageId) {
        this.lastServerMessageId = lastServerMessageId;
    }

    public ServerMessage copy() {
        ServerMessage message = new ServerMessage(this.getType(), getForwardChannel(), getContent().copy());
        message.setLastClientMessageId(getLastClientMessageId());
        message.setLastServerMessageId(getLastServerMessageId());
        return message;
    }
}
