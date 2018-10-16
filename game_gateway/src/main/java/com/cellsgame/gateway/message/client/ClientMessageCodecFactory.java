package com.cellsgame.gateway.message.client;

import com.cellsgame.gateway.message.MessageCodecFactory;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Yang on 16/6/24.
 */
public final class ClientMessageCodecFactory extends MessageCodecFactory {
	private boolean zipOrCustom = true;
	
	public ClientMessageCodecFactory(){}
	
	public ClientMessageCodecFactory(boolean zipOrCustom){this.zipOrCustom = zipOrCustom;}
	
	
    @Override
    public ByteToMessageDecoder createDecoder() {
        return new ClientMessageDecoder(zipOrCustom);
    }

    @Override
    public MessageToByteEncoder createEncoder() {
        return new ClientMessageEncoder(zipOrCustom);
    }
}
