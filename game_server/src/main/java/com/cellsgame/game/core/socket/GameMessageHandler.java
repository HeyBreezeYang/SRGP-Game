package com.cellsgame.game.core.socket;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import com.cellsgame.game.context.SessionController;
import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.MessageHandler;
import com.cellsgame.gateway.message.ServerMessage;
import com.cellsgame.gateway.message.processor.MessageProcessor;
import com.cellsgame.gateway.message.processor.job.MessageProcessorJob;
import com.google.common.util.concurrent.AtomicLongMap;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class GameMessageHandler extends MessageHandler<ServerMessage, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMessageHandler.class);
    private static final AtomicLongMap<Object> CONNECTION_SIZE_PER_GATEWAY = AtomicLongMap.create();
    private String serverId;

    /**
     * 创建游戏消息处理器
     *
     * @param serverId 游戏服务器ID
     */
    GameMessageHandler(String serverId) {
        this(serverId, null);
    }

    /**
     * 构建消息处理器。
     *
     * @param service 收到消息之后,将消息分发到线程池的服务
     */
    @SuppressWarnings("rawtypes")
    private GameMessageHandler(String serverId, MessageProcessor service) {
        super(service);
        this.serverId = serverId;
    }

    @Override
    public void handshakeMessage(Connection connection, Object msg) {
        ByteBuf handshakeBuff = connection.newBuffer(4 + getServerId().length() * 8);
        // 长度
        handshakeBuff.writeIntLE(getServerId().length());
        // 数据
        handshakeBuff.writeCharSequence(getServerId(), Charset.forName("UTF-8"));
        // 确认握手成功
        connection.sendOriginalMessage(handshakeBuff);
        // 附加数据
        connection.setAttachment(new ConnectionAttachment(msg));
        //
        CONNECTION_SIZE_PER_GATEWAY.incrementAndGet(msg);
        //
        LOGGER.warn("gateway server [{}] connected", msg);
    }

    /**
     * 连接打开事件。
     *
     * @param connection 打开的连接
     */
    @Override
    public void connectionOpened(Connection connection) {
        //
        LOGGER.info("gateway server connection open");
    }

    /**
     * 连接关闭事件。
     *
     * @param closed 关闭的连接
     */
    @Override
    public void connectionClosed(final Connection closed) {
        // 如果连接不正常
        if (closed.getAttachment() == null) {
            LOGGER.info("gateway closed.getAttachment() == null");
            return;
        }
        //
        LOGGER.info("gateway server [{}] connection lost", ((ConnectionAttachment) closed.getAttachment()).getId());
        // 清除所有当前连接管理的所有Session
        submitJob(new MessageProcessorJob<ServerMessage, Void>(new ServerMessage(null).setConn(closed)) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            protected Void process() throws Throwable {
                // 查看附加数据
                ConnectionAttachment attachment = closed.getAttachment();
                // 查看当前连接数
                long counter = CONNECTION_SIZE_PER_GATEWAY.decrementAndGet(attachment.getId());
                // 是否需要通知连接丢失
                boolean notifyClose = counter <= 0;
                // 当前连接管理的所有Session
                Collection<SessionController> copy = new ArrayList<>(attachment.getSessions().values());
                // 销毁所有Session
                for (SessionController controller : copy) {
                    // 如果需要通知连接丢失
                    if (notifyClose) {
                        // 提交连接丢失消息任务
                        messageArrived(closed, createCloseMessage(closed, controller));
                    } else {
                        // 销毁
                        controller.destroy();
                        // 添加
                        GameMessageProcessorJob.addDeadSession(attachment.getId(), controller);
                    }
                }
                // 如果没有任何正常的连接, 清除网关所有假死会话
                if (notifyClose) {
                    // 通知会话丢失
                    Collection<SessionController> deadControllers = GameMessageProcessorJob.clearDeadSession(attachment.getId()).values();
                    // 遍历所有失效会话
                    for (SessionController controller : deadControllers) {
                        // 会话失效通知
                        messageArrived(closed, createCloseMessage(closed, controller));
                    }
                }
                return null;
            }
        });
    }

    /**
     * 创建会话丢失消息
     *
     * @param connection 管理会话的连接连接
     * @param session    会话
     * @return 会话丢失消息
     */
    private ServerMessage createCloseMessage(Connection connection, SessionController session) {
    	LOGGER.info("create close message");
        return new ServerMessage(Message.Type.Close, session.getChannelId(), null).setConn(connection);
    }

    /**
     * x
     *
     * @param conn 当前连接
     * @param msg  消息
     */
    @Override
    public void messageArrived(Connection conn, ServerMessage msg) {
        LOGGER.info("gateway message arrive, forward to client [{}], gateway [session = [{}], {}]", msg.getForwardChannel(), conn.getSessionId(), conn.getAttachment() == null ? "" : conn.getAttachment());
        GameMessageProcessorJob.process(msg);
    }

    String getServerId() {
        return serverId;
    }
}
