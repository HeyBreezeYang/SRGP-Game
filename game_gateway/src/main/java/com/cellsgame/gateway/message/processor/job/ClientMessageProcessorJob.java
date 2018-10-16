package com.cellsgame.gateway.message.processor.job;

import com.cellsgame.gateway.ConnectorGroup;
import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.GatewayMessageCreator;
import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.ServerMessage;
import com.cellsgame.gateway.message.client.ClientAttachment;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Yang on 16/6/27.
 * <p>
 * 将客户端数据转发到指定服务器。
 * <p>
 * Message transfer to ServerMessage
 */
public class ClientMessageProcessorJob extends MessageProcessorJob<Message, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageProcessorJob.class);
    private static final int DISTRIBUTE_MAX_TRY = 5;

    public ClientMessageProcessorJob(Message message) {
        super(message);
    }

    @Override
    protected Void process() throws Throwable {
        // 查看当前连接附加数据
        ClientAttachment attachment = this.connection.getAttachment();
        try {
            // 如果连接丢失
            if (attachment == null) {
                LOGGER.error("process gateway client message attachment is null. message.getType() : {}", message.getType());
                return null;
            }
            // 如果还没有为当前连接分配逻辑服务器消息转发通道
            if (attachment.gameConnector() == null || !attachment.gameConnector().isActive()) {
                // 分配
                Connection connector = ConnectorGroup.distributeConnector(this.connection);
                // 尝试分配 次数
                int tryTimes = 1;
                // 如果当前分配的通道不可用
                while (connector != null && !connector.isActive()) {
                    // 记录分配次数
                    tryTimes++;
                    // 如果有连接
                    if (ConnectorGroup.hasConnector(attachment.getGameServerId())) {
                        // 重新分配
                        connector = ConnectorGroup.distributeConnector(this.connection);
                    }
                    // 如果达到最大尝试次数
                    if (tryTimes >= DISTRIBUTE_MAX_TRY) connector = null;
                }
                // 记录结果
                attachment.bindGameServer(connector);
            }
            // 如果服务器不存在或者连接失效
            if (attachment.gameConnector() == null || !attachment.gameConnector().isActive()) {
                // 提示逻辑服丢失
                this.connection.sendOriginalMessage(GatewayMessageCreator.create(this.connection, GatewayMessageCreator.GatewayMessage.GAME_SERVER_LOST));
                // 日志
                LOGGER.error("game server [{}] connection not found, please ensure this server is correct.", this.connection.getAttachment() == null ? "None" : this.connection.getAttachment().toString());
            } else {
                LOGGER.info("[BeforeSendMessage] client[{}], [{}]", this.connection.getSessionId(), attachment);
                // 查看是否是连接打开或者关闭
                boolean connectionOpenOrClose = message.getType() == Message.Type.Open || message.getType() == Message.Type.Close;
                // 如果是连接开启或者关闭消息
                int sId = connectionOpenOrClose ? 0 : message.getContent().readIntLE();
                //
                int rId = connectionOpenOrClose ? 0 : message.getContent().readIntLE();
                //
                ByteBuf data = null;
                if(connectionOpenOrClose){
                    String clientIP = message.getConn().getRemoteIP();
                    byte[] bytes = clientIP.getBytes();
                    data = connection.newBuffer(24);
                    data.writeBytes(bytes);
                }else{
                    data = message.getContent().readBytes(message.getContent().readableBytes());
                }
                ServerMessage outMessage = new ServerMessage(message.getType(), message.getSessionId(), data);
                // send id
                outMessage.setLastClientMessageId(sId);
                // receive id
                outMessage.setLastServerMessageId(rId);
                // 转发¬
                attachment.gameConnector().sendMessage(outMessage);
                LOGGER.info("[AfterSendMessage] client[{}], [{}]", this.connection.getSessionId(), attachment);
            }
        } catch (Exception e) {
            LOGGER.error("process gateway client message error, ", e);
        } finally {
            // 默认消息已消费
            message.consume();
            //
            LOGGER.info("process gateway message type[{}]", message.getType());
            if (message.getType() == Message.Type.Close) connection.clear();
        }
        return null;
    }
}
