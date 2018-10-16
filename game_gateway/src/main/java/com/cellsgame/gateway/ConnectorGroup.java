package com.cellsgame.gateway;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.message.client.ClientAttachment;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConnectorGroup implements IDestructive {
    private static final Logger log = LoggerFactory.getLogger(ConnectorGroup.class);
    private static final Map<Object, ConnectorGroup> GROUPS = new ConcurrentHashMap<>();
    private static final Comparator<Connection> Sorter = new Comparator<Connection>() {
        @Override
        public int compare(Connection o1, Connection o2) {
            ConnectorAttachment o1a = o1.getAttachment();
            ConnectorAttachment o2a = o2.getAttachment();
            return o1a.getManagedSession() - o2a.getManagedSession();
        }
    };
    private Object id;
    private List<Connection> connections;

    public ConnectorGroup(Object id) {
        this.id = id;
        connections = Lists.newCopyOnWriteArrayList();
    }

    static synchronized void addConnector(Connection connector) {
        ConnectorAttachment attachment = connector.getAttachment();
        ConnectorGroup group = GROUPS.get(attachment.getGameServerId());
        if (group == null) group = new ConnectorGroup(attachment.getGameServerId());
        group.addConnection(connector);
        GROUPS.put(group.getId(), group);
    }

    static ConnectorGroup removeConnector(Connection connector) {
        ConnectorAttachment attachment = connector.getAttachment();
        if (null == attachment) return null;
        ConnectorGroup group = GROUPS.get(attachment.getGameServerId());
        if (group == null) return null;
        return group.removeConnection(connector);
    }

    public static Connection distributeConnector(Connection connection) {
        try {
            ClientAttachment attachment = connection.getAttachment();
            ConnectorGroup group = GROUPS.get(attachment.getGameServerId());
            if (group == null || group.size() == 0) return null;
            return group.getBinding();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasConnector(Object groupId) {
        ConnectorGroup group = GROUPS.get(groupId);
        return group != null && group.size() > 0;
    }

    public static Map<Object, ConnectorGroup> get() {
        return Collections.unmodifiableMap(GROUPS);
    }

    public Object getId() {
        return this.id;
    }

    void addConnection(Connection connection) {
        if (connections.size() > 0) {
            Connection dest = connections.get(0);
            if (!connection.hasSameDest(dest)) {
                log.warn("detect connection destination[{}] is not as same as current[{}], connection will be discarded", connection.toString(), dest.toString());
                return;
            }
        }
        connections.add(connection);
    }

    ConnectorGroup removeConnection(Connection connection) {
        connections.remove(connection);
        return this;
    }

    /**
     * 动态最少连接数。
     *
     * @return 最少连接的Connector
     */
    Connection getBinding() {
        if (connections.size() == 0) return null;
        // 查看
        Connection best = Collections.min(connections, Sorter);
        // 如果有效, 增加数量
        if (best.isActive() && best.getAttachment() != null)
            ((ConnectorAttachment) best.getAttachment()).incrementSession();
        // 随机分配, 可能出现不平均的情况
        return best;
    }

    int size() {
        return connections.size();
    }

    @Override
    public void destroy() {
        List<Connection> all = new ArrayList<>(connections);
        all.forEach(Connection::close);
    }
}
