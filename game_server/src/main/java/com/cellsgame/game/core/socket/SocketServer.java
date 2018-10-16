package com.cellsgame.game.core.socket;

import java.util.concurrent.CountDownLatch;

import com.cellsgame.gateway.core.Server;
import com.cellsgame.gateway.message.server.ServerMessageCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/27.
 * <p>
 * 程序入口
 */
public class SocketServer {
    private static final Logger log = LoggerFactory.getLogger(SocketServer.class);
    private static Server server;
    private static volatile boolean running = false;

    public static void start(String serverId, String host, int port) {
        if (running) return;
        running = true;
        server = new Server("Game Server");
        server.host(host);
        server.port(port);
        server.setCodecFactory(new ServerMessageCodecFactory());
        server.setHandler(new GameMessageHandler(serverId));
        server.start();
    }

    public static void stop() {
        if (!running) return;
        log.warn("game server is stopping, please wait.");
        running = false;
        final CountDownLatch latch = new CountDownLatch(1);
        server.stop(latch::countDown);
        try {
            latch.await();
        } catch (InterruptedException ignored) {
            log.warn("", ignored);
        } finally {
            server = null;
            log.warn("game server application totally closed");
        }
    }
}
