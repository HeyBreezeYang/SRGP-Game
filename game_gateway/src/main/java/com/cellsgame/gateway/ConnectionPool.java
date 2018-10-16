package com.cellsgame.gateway;

import com.cellsgame.gateway.core.Connection;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Yang on 16/6/27.
 */
public final class ConnectionPool {
    private static final ConnectionPool Instance = new ConnectionPool();
    private static final ConcurrentHashMap<Integer, Connection> CONNECTIONS = new ConcurrentHashMap<>();

    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        return Instance;
    }

    public void addConnection(Connection connection) {
        CONNECTIONS.putIfAbsent(connection.getSessionId(), connection);
    }

    public void removeConnection(Connection connection) {
        CONNECTIONS.remove(connection.getSessionId());
    }

    public Connection findConnection(int sessionId) {
        return CONNECTIONS.get(sessionId);
    }
}
