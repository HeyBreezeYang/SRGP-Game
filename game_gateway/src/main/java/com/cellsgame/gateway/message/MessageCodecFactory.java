package com.cellsgame.gateway.message;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Yang on 16/6/24.
 */
public abstract class MessageCodecFactory {
    public abstract ByteToMessageDecoder createDecoder();

    public abstract MessageToByteEncoder createEncoder();
}
