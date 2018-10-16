package com.cellsgame.gateway;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Yang on 16/7/13.
 */
public class ConnectorAttachment implements IDestructive {
    private Object gameServerId;
    private AtomicInteger managedSession;

    public ConnectorAttachment(Object gameServerId) {
        this.gameServerId = gameServerId;
        this.managedSession = new AtomicInteger(0);
    }

    public ConnectorAttachment(Object gameServerId, int begin) {
        this.gameServerId = gameServerId;
        this.managedSession = new AtomicInteger(begin);
        System.out.println(begin);
    }

    public Object getGameServerId() {
        return gameServerId;
    }

    public int getManagedSession() {
        return managedSession.get();
    }

    public int incrementSession() {
        return managedSession.incrementAndGet();
    }

    public int decrementSession() {
        return managedSession.decrementAndGet();
    }

    @Override
    public String toString() {
        return String.format("server [%d], session [%d]", gameServerId, managedSession.get());
    }

    @Override
    public void destroy() {
        gameServerId = null;
    }
}
