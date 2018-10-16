package com.cellsgame.gateway.message.server;

import com.cellsgame.gateway.message.ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/24.
 */
final class ServerMessageEncoder extends MessageToByteEncoder<ServerMessage> {
    private static final Logger log = LoggerFactory.getLogger(ServerMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerMessage message, ByteBuf buffer) throws Exception {
        // real data length
        int len = message.getDataLength();
        // length
        buffer.writeIntLE(len + 13);
        // message type
        buffer.writeByte(message.getType().ordinal());
        // session
        buffer.writeIntLE(message.getForwardChannel());
        // last send message id
        buffer.writeIntLE(message.getLastClientMessageId());
        // last received message id
        buffer.writeIntLE(message.getLastServerMessageId());
        // message command
        if (len > 0) buffer.writeBytes(message.getContent());
        //
        message.consume();
        // 日志
        log.info("[ServerMessageEncoder] encode gateway message, length = {}", len);
    }

    @Override
    protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, ServerMessage msg, boolean preferDirect) throws Exception {
        int size = msg.getDataLength() + 17;
        if (preferDirect) {
            return ctx.alloc().ioBuffer(size);
        } else {
            return ctx.alloc().heapBuffer(size);
        }
    }
}
