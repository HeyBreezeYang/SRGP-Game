package com.cellsgame.game.context;

import com.cellsgame.game.core.socket.AttachmentController;
import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.ServerMessage;

public final class SessionController extends AttachmentController {
    private Connection connection;
    private volatile int channelId;

    public SessionController(Connection connection, int channelId) {
        this.connection = connection;
        this.channelId = channelId;
    }

    public SessionController destroy() {
        super.destroy();
        connection = null;
        return this;
    }

    public int getChannelId() {
        return this.channelId;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public SessionController bindConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public boolean isActive() {
        return this.connection != null && this.connection.isActive();
    }

    public void sendMessage(final byte[] data) {
        if (!isActive()) return;
        connection.execute(new Runnable() {
            @Override
            public void run() {
                // 新建消息
                ServerMessage message = new ServerMessage(connection.newBuffer(data.length).writeBytes(data));
                //
                SessionController.this.sendMessage(message);
            }
        });
    }

    /**
     * 向当前消息管理器发送消息.
     *
     * @param message 消息数据
     */
    void sendMessage(ServerMessage message) {
        if (!isActive()) return;
        // 消息需要轩发的通道
        message.setForwardChannel(channelId);
        //connection
        connection.sendMessage(message);
    }

    @Override
    public String toString() {
        return "SessionController{" + channelId + '}';
    }
}
