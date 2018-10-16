package com.cellsgame.gateway.message.server;

import com.cellsgame.gateway.message.MessageCodecFactory;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Yang on 16/6/24.
 */
public final class ServerMessageCodecFactory extends MessageCodecFactory {
    @Override
    public ByteToMessageDecoder createDecoder() {
        return new ServerMessageDecoder();
    }

    @Override
    public MessageToByteEncoder createEncoder() {
        return new ServerMessageEncoder();
    }
}
