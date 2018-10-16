package com.cellsgame.gateway.message.server;

import java.util.List;

import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.MessageDecoder;
import com.cellsgame.gateway.message.ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by Yang on 16/6/24.
 */
final class ServerMessageDecoder extends MessageDecoder {

    @Override
    protected int packageHeaderSize() {
        return 8;
    }

    public void doDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // message size
        int length = in.readIntLE();
        // message type
        byte messageType = in.readByte();
        // session id
        int sessionId = in.readIntLE();
        //
        int sId = in.readIntLE();
        //
        int rId = in.readIntLE();
        // data length of out message
        int dataLength = length - 13;
        // data
        ByteBuf data = dataLength > 0 ? in.readBytes(dataLength) : null;
        //
        try {
            // out message buffer
            ByteBuf outBuffer = ctx.alloc().buffer(dataLength);
            // data
            if (data != null) outBuffer.writeBytes(data);
            //
            ServerMessage serverMessage = new ServerMessage(Message.Type.get(messageType), sessionId, outBuffer);
            //
            serverMessage.setLastClientMessageId(sId);
            //
            serverMessage.setLastServerMessageId(rId);
            // append message
            out.add(serverMessage);
        } finally {
            if (data != null) ReferenceCountUtil.release(data);
        }
    }
}
