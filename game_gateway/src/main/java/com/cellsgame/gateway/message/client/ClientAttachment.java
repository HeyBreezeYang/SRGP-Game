package com.cellsgame.gateway.message.client;

import com.cellsgame.gateway.IDestructive;
import com.cellsgame.gateway.core.Connection;

public final class ClientAttachment implements IDestructive {
    private String bindGameServerId;
    private Connection bindGameServerConnector;

    public ClientAttachment(String bindGameServerId) {
        this.bindGameServerId = bindGameServerId;
    }

    public void bindGameServer(Connection connection) {
        this.bindGameServerConnector = connection;
    }

    public Connection gameConnector() {
        return bindGameServerConnector;
    }

    public String getGameServerId() {
        return bindGameServerId;
    }

    @Override
    public String toString() {
        return String.format("message to server[%s], connector [%s]",
                bindGameServerId,
                bindGameServerConnector == null ? "inactive" : bindGameServerConnector.toString()
        );
    }

    @Override
    public void destroy() {
        this.bindGameServerConnector = null;
    }
}
