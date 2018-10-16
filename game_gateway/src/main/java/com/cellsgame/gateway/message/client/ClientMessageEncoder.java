package com.cellsgame.gateway.message.client;
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
 * ClientMessageEncoder.java
 * <p/>
 * Author: yangwei
 * <p/>
 * Email: ywengineer@gmail.com
 * <p/>
 * Date: 2015年6月8日 下午12:42:07
 */
public final class ClientMessageEncoder extends MessageToByteEncoder<Message> {

	
	
	private boolean zipOrGzip = true;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageEncoder.class);

    public ClientMessageEncoder() {
    }
    
    public ClientMessageEncoder(boolean zipOrGzip) {
    	this.zipOrGzip  = zipOrGzip;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf buffer) throws Exception {
        // 客户端消息ID
        int lastClientMessageId = message.getContent().readIntLE();
        // 服务器消息ID
        int lastServerMessageId = message.getContent().readIntLE();
        // 数据是否已压缩
        boolean compressed = message.getContent().readBoolean();
        // 真实数据
        byte[] data;
        // 如果没有压缩, 但数据体需要压缩
        if (!compressed && message.getDataLength() >= 100000) {
            // 是否需要压缩
            compressed = true;
            byte[] needZip = message.rawBytes();
            if(zipOrGzip)// 压缩余下所有数据
            	data = ZLibUtil.compress(needZip);
            else 
            	data = customCompress(needZip);
        } else { // 数据已压缩
            // 数据体
            data = message.rawBytes();
        }
        //
        ByteBuf buf = ctx.alloc().buffer(data.length + 8).writeIntLE(lastClientMessageId).writeIntLE(lastServerMessageId).writeBytes(data);
        // 将buff转换成字节数组
        data = Utils.toBytes(buf);
        // length
        buffer.writeIntLE(data.length + 2);
        // encrypt
        buffer.writeBoolean(false);
        // compact
        buffer.writeBoolean(compressed);
        
        // message command
        buffer.writeBytes(data);
        //
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
