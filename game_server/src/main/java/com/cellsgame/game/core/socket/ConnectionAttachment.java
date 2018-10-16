package com.cellsgame.game.core.socket;

import java.util.Map;

import com.cellsgame.game.context.SessionController;
import com.cellsgame.gateway.IDestructive;
import com.google.common.collect.Maps;

/**
 * Created by Yang on 16/7/13.
 */
public class ConnectionAttachment implements IDestructive {
    private Object id;
    private Map<Integer, SessionController> sessions;

    public ConnectionAttachment(Object id) {
        this.id = id;
        this.sessions = Maps.newHashMap();
    }

    public Object getId() {
        return id;
    }

    public Map<Integer, SessionController> getSessions() {
        return sessions;
    }

    @Override
    public String toString() {
        return String.format("id [%s], connection size [%d]", id, sessions.size());
    }

    @Override
    public void destroy() {
        if (sessions != null) sessions.clear();
        sessions = null;
    }
}
