package com.cellsgame.gateway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.cmd.ConsoleCmdRunner;
import com.cellsgame.gateway.core.Server;
import com.cellsgame.gateway.core.Service;
import com.cellsgame.gateway.core.WebSocketServer;
import com.cellsgame.gateway.message.client.ClientMessageCodecFactory;
import com.cellsgame.gateway.message.client.ClientMessageHandler;
import com.cellsgame.gateway.utils.FileWatchdog;
import com.cellsgame.gateway.utils.Utils;
import com.google.common.collect.Maps;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yang on 16/6/27.
 * <p>
 * 网关程序入口
 */
public class Main {
//	TODO commented by luojf945
//	static{
//		String libPath = System.getProperty("BROTLI_JNI_LIBRARY");
//		if(libPath!=null)
//			System.load(new java.io.File(libPath).getAbsolutePath());
//		else{
//			File f = new File("lib/brotli_jni.so");
//			if(f.exists()){
//				System.out.println("load default brotli_jni.so: lib/brotli_jni.so");
//				System.load(f.getAbsolutePath());
//			}
//		}
//	}
	
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final DelayQueue<TryJob> QUEUE = new DelayQueue<>();
    private static JobScanner scanner;
    private static ResourceBundle config;
    private static Server gateway;
    private static String gatewayId;
    private static GameServerConnector connector;
    private static String configFile;
    private static AtomicBoolean running = new AtomicBoolean(false);
    private static int connectorSize = 1;
    private static FileWatchdog watchdog;


    public static void main(String[] args) {
        // 配置文件地址
        config = ResourceBundle.getBundle("config");
        // 逻辑服配置文件
        configFile = Main.class.getResource("/servers.properties").getFile();
        // 网关服务器ID
        gatewayId = config.getString("gateway.id");

        Map<String, String> serverIdMap = getServerIdMapping(Main.class.getResource("/serverIdMapping.properties").getFile());

        log.info("1. begin to Start gateway");
        boolean isWebSocket = args != null && args.length > 0 && args[0].equals("ws");
        gateway = isWebSocket ? new WebSocketServer("Gateway Server") : new Server("Gateway Server");
        gateway.port(Utils.getInt(config.getString("gateway.port")));
        gateway.setCodecFactory(new ClientMessageCodecFactory(!isWebSocket));//由于客户端无法正常解压zlib，websocket不使用zlib而使用自定义压缩方式
        gateway.setHandler(new ClientMessageHandler(serverIdMap));
        gateway.setReadTimeout(Math.max(0, Utils.getInt(config.getString("server.config.seconds.read.timeout"))));
        gateway.start();


        log.info("2. begin to Start watchdog");
        // 开启游戏服务器连接
        startConnector();

        log.info("3. begin to Start JobScanner");
        scanner = new JobScanner();
        // running
        running.set(true);
        // start delay job scanner
        scanner.start();

        log.info("4. begin to Start Console");
        // Console
        ConsoleCmdRunner runner = new ConsoleCmdRunner(running, "", Main::stop);
        runner.insertCMD("st", params -> status());
        runner.insertCMD("ri", Main::ri);
        runner.insertCMD("ic", Main::ic);
        runner.run();
    }


    private static Map<String,String> getServerIdMapping(String mappingFileName){
        Map<String, String> mapping = new HashMap<>();
        FileInputStream stream = null;
        try {
        	stream = new FileInputStream(mappingFileName);
        	InputStreamReader reader = new InputStreamReader(stream,"UTF-8");
//        	BufferedReader br = new BufferedReader(reader);
//        	String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//            br.close();
//            reader.close();
            
            Properties properties = new Properties();
            properties.load(reader);
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                mapping.put(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) Utils.closeQuietly(stream);
        }
        return mapping;
    }

    private static void startConnector() {
        watchdog = new FileWatchdog(configFile) {
            @Override
            protected void doOnChange() {
                FileInputStream stream = null;
                try {
                    stream = new FileInputStream(filename);
                    Properties properties = new Properties();
                    properties.load(stream);
                    doConnect(properties);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) Utils.closeQuietly(stream);
                }
            }
        };
        watchdog.setDelay(Utils.getInt(config.getString("server.config.watch.seconds")) * 1000);
        watchdog.start();
        log.info("file watch dog is running. {}", configFile);
    }

    private static void doConnect(Properties data) {
        log.error("logic server configuration was changed, latest size of server = {}", data == null ? 0 : data.size());
        // 如果没有数据
        if (data == null || data.size() <= 0) return;
        // 如果连接器不存在
        if (connector == null) createConnector();
        ////////////////////////////////////////////////////////////////////////////////
        // 不在检测是否可用
        QUEUE.removeIf(job -> {
            // 不在当前配置
            boolean delete = !data.containsKey(job.jobKey);
            // 删除
            if (delete) log.error("all connection of group [{}] was closed", job.jobKey);
            else if (!job.getServerAddress().equals(data.getProperty(String.valueOf(job.jobKey)))) {// 如果地址有变
                // IP:PORT
                String[] addressData = String.valueOf(data.get(String.valueOf(job.jobKey))).split(":");
                if (addressData.length == 2) {
                    job.host = addressData[0];
                    job.port = Utils.getInt(addressData[1]);
                }
            }
            return delete;
        });
        ////////////////////////////////////////////////////////////////////////////////
        // 当前已存在的连接，但不在最新的连接列表
        Set<String> notInCurrent = connector.allServerAddressId();
        notInCurrent.removeAll(data.keySet());
        // 将不在最新列表的关闭
        notInCurrent.forEach(connector::closeConnector);
        ////////////////////////////////////////////////////////////////////////////////
        // 查看所有需要连接的地址
        for (Object addressId : data.keySet()) {
            // IP:PORT
            String[] addressData = String.valueOf(data.get(addressId)).split(":");
            //
            doConnect(addressId, addressData[0], Utils.getInt(addressData[1]), 0);
        }
    }

    static void doConnect(Object destKey, String host, int port, int tryTimes) {
        //
        String addressId = String.valueOf(destKey);
        //
        InetSocketAddress dest = new InetSocketAddress(host, port);
        // 检查当前地址ID,如果不存在,进行连接
        if (!connector.containsAddressId(addressId)) {
            // 检测目标地址是否可连
            if (Utils.isAvailable(host, port)) {
                // 查看连接数
                for (int i = 0; i < connectorSize; i++) {
                    // 执行连接
                    connector.connect(addressId, dest);
                }
            } else {
                TryJob job = new TryJob(addressId, host, port, tryTimes);
                QUEUE.offer(job);
                log.error("dest[id={}, host={}, port={}] is unreachable, try connect after {} seconds automatically", addressId, host, port, job.getDelaySeconds());
            }
        } else if (!dest.equals(connector.getAddress(addressId))) {// 如果当前连接地址与最新地址不相同
            // 关闭当前连接
            connector.closeConnector(addressId);
            // 完全关闭之后, 重新连接
            connector.addReconnectAfterClose(addressId, dest);
        }
    }

    private static void createConnector() {
        connector = new GameServerConnector(gatewayId, Maps.newHashMap());
        connector.channel(Service.DEFAULT_USE_EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
//                .group(Service.DEFAULT_USE_EPOLL ? new EpollEventLoopGroup(connector.getWorkerThreads()) : new NioEventLoopGroup(connector.getWorkerThreads()))
                .group(gateway.getIoWorkerGroup());
        // 连接数
        connectorSize = Utils.getInt(config.getString("server.connector.size"));
        // 如果不是线程池, 用默认配置
        connectorSize = Math.max(4, connectorSize);
        connector.setMessageProcessorJobFactory(new GameMessageProcessorJobFactory());
        connector.start();
        log.debug("size of connector is : {}", connectorSize);
    }

    private static void ic(String[] params) {
        try {
            if (params.length <= 1) {
                System.out.println("Usage: ic serverKey\n" +
                        "e.g: ic syw");
            } else {
                QUEUE.removeIf(job -> {
                    if (job.jobKey.equals(params[1])) {
                        job.tryTimes = 0;
                        job.run();
                        return true;
                    }
                    return false;
                });
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void ri(String[] params) {
        try {
            if (params.length <= 1) {
                ConnectorGroup.get().values().forEach(ConnectorGroup::destroy);
            } else {
                ConnectorGroup group = ConnectorGroup.get().get(params[1]);
                if (group != null) group.destroy();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void status() {
        System.out.println("---------------------- status ----------------------");
        for (Map.Entry<Object, ConnectorGroup> groupEntry : ConnectorGroup.get().entrySet()) {
            Object key = groupEntry.getKey();
            ConnectorGroup group = groupEntry.getValue();
            InetSocketAddress address = connector.getAddress(String.valueOf(key));
            System.out.println(String.format("%s 's connection size is : %d, status : %s, address : %s", String.valueOf(key), group.size(), group.size() == connectorSize ? "Normal" : "Abnormal", address == null ? "unknown" : address.toString()));
        }
        System.out.println("---------------------- status ----------------------");
        System.out.println("---------------------- Delay Queue Status ----------------------");
        QUEUE.forEach(System.out::println);
        System.out.println("---------------------- Delay Queue Status ----------------------");
    }

    private static void stop() {
        log.warn("gateway application is stopping");
        final CountDownLatch latch = new CountDownLatch(2);
        if (watchdog != null) watchdog.interrupt();
        if (scanner != null) scanner.interrupt();
        watchdog = null;
        scanner = null;
        gateway.stop(latch::countDown);
        connector.stop(latch::countDown);
        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }
        gateway = null;
        connector = null;
        log.warn("gateway application totally closed");
    }

    private static class JobScanner extends Thread {
        JobScanner() {
            super(JobScanner.class.getSimpleName());
        }

        @Override
        public void run() {
            while (running.get()) {
                try {
                    TryJob job = QUEUE.poll();
                    if (job == null) {
                        Utils.sleep(5);
                        continue;
                    }
                    job.run();
                } catch (Throwable e) {
                    log.error(e.getMessage());
                }
            }
            log.info("delay queue scanner was stopped.");
        }
    }

    private static class TryJob implements Delayed, Runnable {
        private String host;
        private int port;
        private int tryTimes;
        private Object jobKey;
        private long delay;

        TryJob(Object jobKey, String host, int port, int tryTimes) {
            this.jobKey = jobKey;
            this.host = host;
            this.port = port;
            this.tryTimes = tryTimes;
            this.delay = (long) (System.currentTimeMillis() + Math.min(180000, 5000 * Math.pow(2, tryTimes)));
        }

        @Override
        public void run() {
            doConnect(jobKey, host, port, tryTimes + 1);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(delay - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            if (o instanceof TryJob) return (int) (this.delay - ((TryJob) o).delay);
            return 0;
        }

        String getServerAddress() {
            return host + ":" + port;
        }

        long getDelaySeconds() {
            return (delay - System.currentTimeMillis()) / 1000;
        }

        @Override
        public String toString() {
            return "TryJob{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", tryTimes=" + tryTimes +
                    ", jobKey=" + jobKey +
                    ", delay=" + DateUtil.getStringDate(delay) +
                    '}';
        }
    }
}
