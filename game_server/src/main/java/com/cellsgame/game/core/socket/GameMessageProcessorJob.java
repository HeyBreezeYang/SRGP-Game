package com.cellsgame.game.core.socket;

import java.util.Collections;
import java.util.Map;

import com.cellsgame.game.cons.SessionAttributeKey;
import com.cellsgame.game.context.SessionController;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.message.GameMessage;
import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.ServerMessage;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GameMessageProcessorJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMessageProcessorJob.class);
    private static final Table<Object, Object, SessionController> DEAD_SESSIONS = HashBasedTable.create();

    private GameMessageProcessorJob() {
    }

    public static void process(ServerMessage message) {
        System.out.println("process gateway message, msgId =  " + message.getLastClientMessageId());
        try {
            Connection connection = message.getConn();
            // 连接附加数据
            ConnectionAttachment attachment = connection.getAttachment();
            // 如果连接失效(只会在连接丢失的时候)
            if (attachment == null || attachment.getSessions() == null) return;
            // 当前连接管理的会话
            SessionController controller = attachment.getSessions().get(message.getForwardChannel());
            // 查看消息类型
            switch (message.getType()) {
                case Open:// 连接打开
                    // 新建并添加
                    attachment.getSessions().put(message.getForwardChannel(), controller = new SessionController(connection, message.getForwardChannel()));
                    // 数据
                    byte[] clientIp = new byte[message.getContent().readableBytes()];
                    // 提取数据
                    message.getContent().readBytes(clientIp);
                    controller.getAttributes().put(SessionAttributeKey.CLIENT_IP, new String(clientIp));
                    // 连接消息
                    Dispatch.dispatchGameMessage(controller, GameMessage.MessageType_Open, 0, 0, null);
                    break;
                case Close:
                    // 如果会话是当前连接所管理
                    if (controller != null) {
                        // 删除会话
                        controller = attachment.getSessions().remove(message.getForwardChannel());
                    } else {
                        // 如果不受当前连接管理, 可能是假死会话
                        controller = take(attachment.getId(), message.getForwardChannel());
                    }
                    // 如果会话存在, 通知会话关闭
                    if (controller != null) Dispatch.dispatchGameMessage(controller.destroy(), GameMessage.MessageType_Close, 0, 0, null);
                    break;
                case Message:
                    // log
                    LOGGER.debug("process gateway message, msgId = {}, ", message.getLastClientMessageId());
                    // 如果会话不存在,
                    if (controller == null) {
                        //查看是否可以重新分配
                        controller = take(attachment.getId(), message.getForwardChannel());
                        // 如果可以重新分配
                        if (controller != null) {
                            //
                            LOGGER.debug("session [{}] rebind to connection [{}]", message.getForwardChannel(), connection.getSessionId());
                            // 分配
                            controller.bindConnection(connection);
                            // 加入管理列表
                            attachment.getSessions().put(controller.getChannelId(), controller);
                        }
                    }
                    // 如果不存在
                    if (controller != null && controller.isActive()) {
                        // 数据
                        byte[] data = new byte[message.getContent().readableBytes()];
                        // 提取数据
                        message.getContent().readBytes(data);
                        // 分发消息
                        Dispatch.dispatchGameMessage(controller, GameMessage.MessageType_Message, message.getLastClientMessageId(), message.getLastServerMessageId(), data);
                    } else {
                        LOGGER.warn("gateway[{}] 's managed channel [{}] not found", attachment.getId(), message.getForwardChannel());
                    }
                    break;
            }
        } catch (Throwable throwable) {
            LOGGER.error("process gateway message error", throwable);
        } finally {
            message.consume();
        }
    }

    static void addDeadSession(Object gateway, SessionController session) {
        synchronized (DEAD_SESSIONS) {
            DEAD_SESSIONS.put(gateway, session.getChannelId(), session);
        }
    }

    static Map<Object, SessionController> clearDeadSession(Object gateway) {
        synchronized (DEAD_SESSIONS) {
            Map<Object, SessionController> ori = DEAD_SESSIONS.row(gateway);
            if (ori.size() == 0) return Collections.emptyMap();
            Map<Object, SessionController> copy = Maps.newHashMap(ori);
            ori.clear();
            return copy;
        }
    }

    static SessionController take(Object gateway, Object channel) {
        synchronized (DEAD_SESSIONS) {
            return DEAD_SESSIONS.remove(gateway, channel);
        }
    }
}
