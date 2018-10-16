package com.cellsgame.gateway.message.client;

import java.util.HashMap;
import java.util.Map;

import com.cellsgame.gateway.ConnectionPool;
import com.cellsgame.gateway.ConnectorGroup;
import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.GatewayMessageCreator;
import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.MessageHandler;
import com.cellsgame.gateway.message.processor.MessageProcessor;
import com.cellsgame.gateway.message.processor.job.ClientMessageProcessorJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/24.
 */
public class ClientMessageHandler extends MessageHandler<Message, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageHandler.class);

    private Map<String,String> serverIdMapping = new HashMap<>();

    public ClientMessageHandler(Map<String,String> serverIdMapping) {
        this(null, serverIdMapping);
    }

    /**
     * 构建消息处理器。
     *
     * @param service 收到消息之后,将消息分发到线程池的服务
     */
    public ClientMessageHandler(MessageProcessor service,Map<String,String> serverIdMapping) {
        super(service);
        this.serverIdMapping = serverIdMapping;
    }


    @Override
    public void handshakeMessage(Connection connection, Object msg) {
        if (serverIdMapping != null && serverIdMapping.size() > 0)
            msg = serverIdMapping.getOrDefault(msg, msg.toString());
        // 如果逻辑服务器连接不存在
        if (!ConnectorGroup.hasConnector(msg)) {
            // 提示逻辑服丢失
            connection.sendOriginalMessage(GatewayMessageCreator.create(connection, GatewayMessageCreator.GatewayMessage.GAME_SERVER_LOST));
            // 日志
            LOGGER.error("game server [{}] connection not found, connection will be close, please ensure this server is is correct.", msg);
        } else {
            // 确认握手成功
            connection.sendOriginalMessage(GatewayMessageCreator.create(connection, GatewayMessageCreator.GatewayMessage.SUCCESS));
            // 附加数据
            connection.setAttachment(new ClientAttachment(java.lang.String.valueOf(msg)));
            // 加入缓存
            ConnectionPool.getInstance().addConnection(connection);
            // 握手成功,通知游戏服务器
            submitJob(new ClientMessageProcessorJob(new Message(Message.Type.Open, connection, null)));
            //
            LOGGER.info("[ConnectionOpen] client connect, all message will transfer to server [{}]", msg);
        }
    }

    @Override
    public void connectionOpened(Connection connection) {
        LOGGER.debug("connection opened, id = {}", connection.getSessionId());
    }

    @Override
    public void connectionClosed(Connection connection) {
        LOGGER.info("connection closed, id = {}", connection.getSessionId());
        ConnectionPool.getInstance().removeConnection(connection);
        // 连接断开, 通知游戏服务器
        submitJob(new ClientMessageProcessorJob(new Message(Message.Type.Close, connection, null)));
    }

    @Override
    public void messageArrived(Connection conn, Message msg) {
        LOGGER.info("[MessageArrived] message arrived, id = {}, {}", msg.getSessionId(), conn.getAttachment() == null ? "None" : conn.getAttachment());
        msg.setConn(conn);
        submitJob(new ClientMessageProcessorJob(msg));
    }

    @Override
    public void writeTimeout(Connection connection) {
        LOGGER.warn("connection writeTimeout, id = {}", connection.getSessionId());
    }
}
