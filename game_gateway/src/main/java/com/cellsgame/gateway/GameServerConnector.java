package com.cellsgame.gateway;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.core.Connector;
import com.cellsgame.gateway.message.MessageHandler;
import com.cellsgame.gateway.message.ServerMessage;
import com.cellsgame.gateway.message.processor.MessageProcessor;
import com.cellsgame.gateway.message.processor.job.MessageProcessorJobFactory;
import com.cellsgame.gateway.message.server.ServerMessageCodecFactory;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/29.
 */
public class GameServerConnector extends Connector {
    private static final Logger log = LoggerFactory.getLogger(GameServerConnector.class);
    private String serverId;
    private MessageProcessorJobFactory<ServerMessage, Void> messageProcessorJobFactory;
    private Map<String, InetSocketAddress> reconnectAfterClose = Maps.newConcurrentMap();

    public GameServerConnector(String serverId, Map<String, InetSocketAddress> addressMap) {
        super("Game Connector", new ConcurrentHashMap<>(addressMap));
        setServerId(serverId);
        setCodecFactory(new ServerMessageCodecFactory());
        setHandler(new Handler(null));
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public MessageProcessorJobFactory<ServerMessage, Void> getMessageProcessorJobFactory() {
        return messageProcessorJobFactory;
    }

    public void setMessageProcessorJobFactory(MessageProcessorJobFactory<ServerMessage, Void> messageProcessorJobFactory) {
        this.messageProcessorJobFactory = messageProcessorJobFactory;
    }

    @Override
    protected void doStart() {
        if (StringUtils.isEmpty(serverId)) throw new RuntimeException("server id is not set");
        if (messageProcessorJobFactory == null) throw new RuntimeException("message processor job factory is not set");
        super.doStart();
    }

    @Override
    protected void onAddressRemove(String addressId) {
        super.onAddressRemove(addressId);
        ConnectorGroup connectorGroup = ConnectorGroup.get().get(addressId);
        if (connectorGroup != null) connectorGroup.destroy();
    }

    void addReconnectAfterClose(String key, InetSocketAddress address) {
        reconnectAfterClose.put(key, address);
    }

    private class Handler extends MessageHandler<ServerMessage, Void> {
        /**
         * 构建消息处理器。
         *
         * @param service 收到消息之后,将消息分发到线程池的服务
         */
        public Handler(MessageProcessor service) {
            super(service);
        }

        @Override
        public void connectionOpened(Connection connection) {
            log.debug("[Open] {} : session[{}], gateway server[{}]", getName(), connection.getSessionId(), getServerId());
            ByteBuf handshakeBuff = connection.newBuffer(4 + getServerId().length() * 8);
            // 长度
            handshakeBuff.writeIntLE(getServerId().length());
            // 数据
            handshakeBuff.writeCharSequence(getServerId(), Charset.forName("UTF-8"));
            // shake hands with game server
            connection.sendOriginalMessage(handshakeBuff);
        }

        @Override
        public void handshakeMessage(Connection connection, Object msg) {
            // handshake response, msg is what we send.
            log.debug("[Handshake] {} : handshake response , game server id[{}]", getName(), msg);
            //
            connection.setAttachment(new ConnectorAttachment(msg));
            // 握手成功，将当前连接加入Connector缓冲区
            ConnectorGroup.addConnector(connection);
        }

        @Override
        public void connectionClosed(Connection connection) {
            try {
                log.debug("[Close] {} : connection close : {}", getName(), connection.getSessionId());
                // 连接丢失, 移除缓冲区
                ConnectorGroup connectorGroup = ConnectorGroup.removeConnector(connection);
                // 清除数据
                connection.clear();
                // 如果存在
                if (connectorGroup != null && connectorGroup.size() <= 0 && getReconnectDelay(String.valueOf(connectorGroup.getId())) <= 0) {
                    log.error("all connection of group [{}] was closed", connectorGroup.getId());
                    InetSocketAddress address = reconnectAfterClose.remove(connectorGroup.getId());
                    if (address != null) Main.doConnect(connectorGroup.getId(), address.getHostName(), address.getPort(), 0);
                }
                // 尝试重连
                if (connectorGroup != null) tryReconnect(String.valueOf(connectorGroup.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void messageArrived(Connection conn, ServerMessage msg) {
            log.debug("[Message] {} : message arrive : {}", getName(), msg.getSessionId());
            submitJob(messageProcessorJobFactory.create(conn, msg));
        }
    }
}
