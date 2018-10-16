package com.cellsgame;

import com.cellsgame.gateway.message.MessageCodecFactory;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Yang on 16/7/13.
 */
public class CMessageCodecFactory extends MessageCodecFactory {
    @Override
    public ByteToMessageDecoder createDecoder() {
        return new MessageDecoder();
    }

    @Override
    public MessageToByteEncoder createEncoder() {
        return new MessageEncoder();
    }
}
