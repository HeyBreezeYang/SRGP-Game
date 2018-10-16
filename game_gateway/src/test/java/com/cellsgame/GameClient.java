package com.cellsgame;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.cellsgame.common.buffer.WriteBuffer;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.gateway.core.Connection;
import com.cellsgame.gateway.core.Connector;
import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.MessageHandler;
import com.cellsgame.gateway.message.processor.JavaThreadMessageProcessor;
import com.cellsgame.gateway.utils.Utils;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cellsgame.gateway.utils.Utils.sleep;

/**
 * Created by Yang on 16/6/16.
 */
public class GameClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameClient.class);
    private static AtomicInteger i = new AtomicInteger(0);


    /**
     * 英雄升级
     * @param connection
     * @param serverId
     */
    private static void sendHeroshengji(Connection connection,  int serverId){
        Map<String, Object> dataMap = GameUtil.createSimpleMap();
//            dataMap.put("sign", "ab769969465ed9f4ccdf6ecde0f8036d845601257091275746ee9b41dcd414c705a32b5ff18bbcc1aff12faea8bf2434990406540a2e5c5d");
        dataMap.put("hid", 20122201);
        Map<Integer, Integer> goodsMap = GameUtil.createSimpleMap();;
        goodsMap.put(1,1);
        goodsMap.put(1,2);
        dataMap.put("goods", goodsMap);
        byte[] data = JSONObject.toJSONString(dataMap).getBytes(Charset.forName("UTF-8"));

        //byte[] data = wbuf.toByteArray();

        ByteBuf buf = connection.newBuffer(18 + data.length)
                .writeIntLE(14+data.length)
                .writeBoolean(false)
                .writeBoolean(false)
                .writeIntLE(i.incrementAndGet())
                .writeIntLE(serverId)
                .writeIntLE(450004)
                .writeBytes(data);

        Message newMsg = new Message(buf);
        connection.sendMessage(newMsg);
    }




    private static void sendPlayerMessage(Connection connection,  int serverId) {
        try {
           // WriteBuffer wbuf = WriteBuffer.allocate(8);

            //Map<String, Object> map = GameUtil.createSimpleMap();
//            map.put("c", Integer.toString(20003));

            Map<String, Object> dataMap = GameUtil.createSimpleMap();
            dataMap.put("sign", "ab769969465ed9f4ccdf6ecde0f8036d845601257091275746ee9b41dcd414c705a32b5ff18bbcc1aff12faea8bf2434990406540a2e5c5d");
            dataMap.put("serverId", "20180828");

            //dataMap.put("gender", Integer.valueOf(1));

           // map.put("d", dataMap);
           // wbuf.write_table(map);

//            WriteBuffer wbuf =WriteBuffer.allocate(64);
//            Map<String, Object> map = GameUtil.createSimpleMap();
//            map.put("playerName", "test01");
//            map.put("img", Integer.valueOf(1));
//            wbuf.write_table(map);

            byte[] data = JSONObject.toJSONString(dataMap).getBytes(Charset.forName("UTF-8"));

            //byte[] data = wbuf.toByteArray();

            ByteBuf buf = connection.newBuffer(18 + data.length)
                    .writeIntLE(14+data.length)
                    .writeBoolean(false)
                    .writeBoolean(false)
                    .writeIntLE(i.incrementAndGet())
                    .writeIntLE(serverId)
                    .writeIntLE(20001)
                    .writeBytes(data);

            Message newMsg = new Message(buf);
            connection.sendMessage(newMsg);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Map<String, InetSocketAddress> addressMap = Maps.newHashMap();
<<<<<<< .mine
        addressMap.put("a", new InetSocketAddress("192.168.10.193", 9999));
||||||| .r1056
        addressMap.put("a", new InetSocketAddress("192.168.10.115", 9999));
=======
        addressMap.put("a", new InetSocketAddress("192.168.10.232", 9999));
>>>>>>> .r1108

        final Connector connector = new Connector("game client connector", addressMap);
        connector.group(new NioEventLoopGroup(1)).channel(NioSocketChannel.class);
        connector.setCodecFactory(new CMessageCodecFactory());
        connector.setHandler(new MessageHandler(new JavaThreadMessageProcessor(Executors.newFixedThreadPool(1))) {
            @Override
            public void connectionOpened(Connection connection) {
                LOGGER.debug("game connection open : {}", connection.getSessionId());
                ByteBuf buf = connection.newBuffer(4 + 8);
                buf.writeIntLE(8);
                buf.writeCharSequence("20180828", Charset.forName("UTF-8"));
                // handshake
                connection.sendOriginalMessage(buf);
            }

            @Override
            public void handshakeMessage(Connection connection, Object obj) {
                LOGGER.debug("game connection handshake response : {}", obj);
            }

            @Override
            public void connectionClosed(Connection connection) {
                LOGGER.debug("game connection close : {}", connection.getSessionId());
                connector.stop(null);
            }
            private  int bytesToIntLE(byte[] bytes) {
//        return   b[3] & 0xFF |
//                (b[2] & 0xFF) << 8 |
//                (b[1] & 0xFF) << 16 |
//                (b[0] & 0xFF) << 24;
                int value=0;
                value = ((bytes[3] & 0xff)<<24)|
                        ((bytes[2] & 0xff)<<16)|
                        ((bytes[1] & 0xff)<<8)|
                        (bytes[0] & 0xff);
                return value;


            }
            @Override
            public void messageArrived(Connection connection, Message msg) {
                ByteBuf buf=msg.getContent();
                String str;
                if(buf.hasArray()) {
                    // 处理堆缓冲区
                    str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
                }
                else {

                    // 处理直接缓冲区以及复合缓冲区
                    byte[] bytes = new byte[buf.readableBytes()];
                    buf.getBytes(buf.readerIndex(), bytes);
                    str = new String(bytes, 0, buf.readableBytes());
                }

                System.out.println("客户端%%%%%%%%%数据=================="+str);



//        Map<?, ?> data = JSON.parseObject(bytes, 4, bytes.length - 4, Charset.forName("utf-8"), HashMap.class, Feature.OrderedField);


                System.out.println("准备发消息=================="+msg.getContent());
                System.out.println("准备发消息==================");
                LOGGER.debug("game message arrived : {}", connection.getSessionId());
                int clientId = msg.getContent().readIntLE();
                int serverId = msg.getContent().readIntLE();

                sendPlayerMessage(connection, serverId);

                //英雄升级
                //sendHeroshengji(connection, serverId);
                sleep(100000);
            }

            @Override
            public void writeTimeout(Connection connection) {

            }
            /**
             * 异常捕获
             *
             * @param connection 连接
             * @param throwable  异常
             */
            public void exceptionCaught(Connection connection, Throwable throwable) {
                LOGGER.debug("exception caught : ", throwable);
                connection.close();
            }
        });

        connector.start();
    }

}
