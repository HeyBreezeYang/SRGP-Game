package com.cellsgame.gateway.message;

import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * Created by Yang on 16/7/11.
 */
public class GatewayMessageCreator {

    /**
     * 为指定网络连接创建网关提示消息
     *
     * @param connection 连接
     * @param message    消息码
     * @return ByteBuf
     */
    public static ByteBuf create(Connection connection, GatewayMessage message) {
        ByteBuf buf = connection.newBuffer(24);
        ByteBufUtil.writeUtf8(buf, "p_x_y_");
//        buf.writeBytes("p_x_y_".getBytes());
        buf.writeIntLE(100).writeIntLE(message.getCode());
        // 原始数据
        byte[] data = Utils.toBytes(buf);
        // 重新分配
        buf = connection.newBuffer(data.length + 4);
        // 长度
        buf.writeIntLE(data.length);
        // 数据
        buf.writeBytes(data);
        // 返回
        return buf;
    }

    public enum GatewayMessage {
        SUCCESS(0),
        GAME_SERVER_LOST(100001);

        private int code;

        GatewayMessage(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }
}
