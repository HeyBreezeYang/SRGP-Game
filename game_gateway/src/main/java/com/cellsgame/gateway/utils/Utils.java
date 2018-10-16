package com.cellsgame.gateway.utils;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/23.
 */
public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
//            LOGGER.error("thread sleep interrupted", e);
        }
    }

    public static int getInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            LOGGER.error("transfer string to int error,", e);
        }
        return 0;
    }

    public static boolean getBoolean(String str) {
        return Boolean.parseBoolean(str);
    }

    public static byte[] toBytes(ByteBuf buf) {
        if (buf == null) return null;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        ReferenceCountUtil.release(buf);
        return data;
    }

    public static boolean isAvailable(String host, int port) {
        Socket client = null;
        try {
            client = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            client.connect(socketAddress, 5000);
            return true;
        } catch (Throwable e) {
            return false;
        } finally {
            closeQuietly(client);
        }
    }

    public static String getString(Object object) {
        return String.valueOf(object);
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (Throwable throwable) {
            LOGGER.error("close a closeable stream channel error:", throwable);
        }
    }

    public static String getAppDir() {
        return System.getProperty("user.dir");
    }

    public static void printProperties() {
        Set<String> keys = System.getProperties().stringPropertyNames();
        for (String key : keys) {
            LOGGER.debug(String.format("%s = %s", key, System.getProperty(key)));
        }
    }
}