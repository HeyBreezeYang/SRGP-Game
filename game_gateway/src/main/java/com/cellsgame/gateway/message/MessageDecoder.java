package com.cellsgame.gateway.message;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/27.
 */
public abstract class MessageDecoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger(MessageDecoder.class);
    private boolean handshake = false;

    private boolean decodable(ByteBuf in) {
        // length + encrypt + compact + server
        if (in.readableBytes() < packageHeaderSize()) return false;
        // current position.
        int position = in.readerIndex();
        // read an number represent the size of message.
        int length = in.readIntLE();
        // if size less than and equals zero or the size of this message is not
        // enough.
        if (length <= 0 || in.readableBytes() < length) {
            // reset position to original.
            in.readerIndex(position);
            // need more data.
            return false;
        }
        // the data of this message is enough.
        in.readerIndex(position);
        // decode message.
        return true;
    }

    /**
     * 每一个消息的包头大小。
     *
     * @return 包头大小
     */
    protected abstract int packageHeaderSize();

    private boolean doHandshake(ByteBuf in, List<Object> out) {
        // 如果不够
        if (in.readableBytes() < 4) return false;
        // 当前位置
        int readPosition = in.readerIndex();
        // 长度
        int length = in.readIntLE();
        // 如果数据不够
        if (in.readableBytes() < length) {
            // 重新标记数据读取位置
            in.readerIndex(readPosition);
            // 握手失败
            return false;
        }
        // 握手数据
        out.add(in.readCharSequence(length, Charset.forName("UTF-8")));
//        System.out.println(String.format("hand shake message length = %d, data = %s", length, out.get(out.size() - 1)));
        // 握手成功
        return true;
    }

    public abstract void doDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception;

    @Override
    protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 如果没有握手,执行握手
        if (!handshake) handshake = doHandshake(in, out);
        // 如果握手成功,才能进行消息解析
        if (handshake) {
            log.debug("[MessageDecoder-Handshake] before decode message, bytes = {}", in.readableBytes());
            // 查看是否有完整消息到达
            while (decodable(in)) {
                // 解析消息
                doDecode(ctx, in, out);
            }
            log.debug("[MessageDecoder-Handshake] after decode message, bytes = {}, message size = {}", in.readableBytes(), out.size());
            // 丢弃已读数据
            if (out.size() > 0) in.discardReadBytes();
        }
    }
}
