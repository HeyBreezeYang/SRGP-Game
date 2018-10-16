package com.cellsgame;

import com.cellsgame.common.util.zlib.ZLibUtil;
import com.cellsgame.gateway.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.Charset;
import java.util.List;

/**
 * ClientMessageDecoder.java
 * <p/>
 * Author: yangwei
 * <p/>
 * Email: ywengineer@gmail.com
 * <p/>
 * Date: 2015年6月8日 下午12:42:13
 */
final class MessageDecoder extends com.cellsgame.gateway.message.MessageDecoder {

    MessageDecoder() {
    }

    @Override
    protected int packageHeaderSize() {
        return 14;
    }

    @Override
    public void doDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // message size
        int length = in.readIntLE();
        // encrypted message
        boolean isJM = in.readBoolean();
        // compacted message
        boolean isYS = in.readBoolean();
        // 客户端消息ID
        int lastClientMessageId = in.readIntLE();
        // 服务器消息ID
        int lastServerMessageId = in.readIntLE();
        // data of out message
        byte[] data = new byte[length - 10];
        // data
        in.readBytes(data);
        // 如果需要解压
        if (isYS) {
            // 解压
            data = ZLibUtil.decompress(data);
        }
        // append message
        out.add(new Message(ctx.alloc().buffer(data.length + 8).writeIntLE(lastClientMessageId).writeIntLE(lastServerMessageId).writeBytes(data)));
    }

}
