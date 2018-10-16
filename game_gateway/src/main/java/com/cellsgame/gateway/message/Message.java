package com.cellsgame.gateway.message;

import java.io.Serializable;

import com.cellsgame.gateway.core.Connection;
import io.netty.buffer.ByteBuf;

/**
 * Created by Yang on 16/6/24.
 * <p>
 * 在使用完消息之后, 如果确定此消息的content不需要再使用, 请手动将此对象consume掉。
 */
public class Message implements Serializable {
    /**
     *
     */
    private Connection conn;
    private ByteBuf content;
    private Type type;


    public Message(ByteBuf content) {
        this(Type.Message, null, content);
    }

    public Message(Type type, Connection source, ByteBuf content) {
        setType(type);
        setContent(content);
        setConn(source);
    }


    public ByteBuf getContent() {
        return content;
    }

    public int getDataLength() {
        return content == null ? 0 : content.readableBytes();
    }


    public void setContent(ByteBuf obj) {
        content = obj;
    }

    /**
     * @return the sessionId of connection
     */
    public int getSessionId() {
        return conn.getSessionId();
    }

    public void consume() {
        if (content != null && content.refCnt() > 0){
        	content.release();	
        }
     
        content = null;
        conn = null;
    }

    public ServerMessage.Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Connection getConn() {
        return conn;
    }

    public Message setConn(Connection conn) {
        this.conn = conn;
        return this;
    }

    public final byte[] rawBytes() {
        byte[] data = new byte[getDataLength()];
        //
        content.readBytes(data);
        //
        return data;
    }

    public enum Type {
        Open, Close, Message;

        public static Type get(int ordinal) {
            return ordinal == 0 ? Open : (ordinal == 1 ? Close : Message);
        }
    }
}
