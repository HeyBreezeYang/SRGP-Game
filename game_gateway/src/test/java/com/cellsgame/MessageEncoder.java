package com.cellsgame;

import com.cellsgame.common.util.zlib.ZLibUtil;
import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;
import org.brotli.wrapper.enc.Encoder;
import org.brotli.wrapper.enc.Encoder.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MessageEncoder.java
 * <p/>
 * Author: luojf945
 * <p/>
 */
public final class MessageEncoder extends MessageToByteEncoder<Message> {

	
	
	private boolean zipOrGzip = true;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoder.class);

    public MessageEncoder() {
    }
    
    public MessageEncoder(boolean zipOrGzip) {
    	this.zipOrGzip  = zipOrGzip;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf buffer) throws Exception {
    	int length = message.getContent().readIntLE();
    	buffer.writeIntLE(length);
    	buffer.writeBoolean(message.getContent().readBoolean());
    	buffer.writeBoolean(message.getContent().readBoolean());
    	buffer.writeIntLE(message.getContent().readIntLE());
    	buffer.writeIntLE(message.getContent().readIntLE());
    	
    	byte[] data = new byte[length-10];
    	message.getContent().readBytes(data);
    	buffer.writeBytes(data);
    	message.consume();
    }
    
    
    
    /** 
     * 数据压缩 
     *  
     * @param data 
     * @return 
     * @throws IOException 
     * @throws Exception 
     */  
    public static byte[] customCompress(byte[] data) throws IOException{
	    Parameters params = new Encoder.Parameters().setQuality(4);
		byte[] ret = Encoder.compress(data,params);
		return ret;
    }
    


    @Override
    protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, Message msg, boolean preferDirect) throws Exception {
        int size = msg.getContent().readableBytes() + 6;
        if (preferDirect) {
            return ctx.alloc().ioBuffer(size);
        } else {
            return ctx.alloc().heapBuffer(size);
        }
    }
    

	public boolean isZipOrGzip() {
		return zipOrGzip;
	}

	public void setZipOrGzip(boolean zipOrGzip) {
		this.zipOrGzip = zipOrGzip;
	}
}
