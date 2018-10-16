package com.cellsgame.gateway.message.client;

import com.cellsgame.common.util.zlib.ZLibUtil;
import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.MessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

import org.brotli.wrapper.dec.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClientMessageDecoder.java
 * <p/>
 * Author: yangwei
 * <p/>
 * Email: ywengineer@gmail.com
 * <p/>
 * Date: 2015年6月8日 下午12:42:13
 */
final class ClientMessageDecoder extends MessageDecoder {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageDecoder.class);
	private boolean zipOrCustom = true;
	
	
    ClientMessageDecoder() {
    }
    
    ClientMessageDecoder(boolean zipOrCustom) {
    	this.zipOrCustom = zipOrCustom;
    }

    @Override
    protected int packageHeaderSize() {
        return 14;
    }

    public void doDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // message size
        int length = in.readIntLE();
        // encrypted message
        boolean isJM = in.readBoolean();
        // compacted message
        boolean compressed = in.readBoolean();
        // 客户端消息ID
        int lastClientMessageId = in.readIntLE();
        // 服务器消息ID
        int lastServerMessageId = in.readIntLE();
        // data of out message
        byte[] data = new byte[length - 10];
        LOGGER.info("doDecode length = {},isJM = {}, compressed = {}, lastClientMessageId = {}, lastServerMessageId = {}",length,isJM,compressed,lastClientMessageId,lastServerMessageId);
        // data
        in.readBytes(data);
        // 如果需要解压
        if (compressed) {
            // 解压
        	if(zipOrCustom)
        		data = ZLibUtil.decompress(data);
        	else
        		data = customDecompress(data);
        }
        // append message
        out.add(new Message(ctx.alloc().buffer(data.length + 8).writeIntLE(lastClientMessageId).writeIntLE(lastServerMessageId).writeBytes(data)));
    }

    
    /** 
     * 数据解压缩 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */  
    public static byte[] customDecompress(byte[] data) throws Exception {  
    	data = Decoder.decompress(data);
    	return data;  
    }


    
}
