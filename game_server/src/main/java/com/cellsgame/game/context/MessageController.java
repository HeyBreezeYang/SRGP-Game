package com.cellsgame.game.context;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.cellsgame.common.buffer.Helper;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.socket.AttachmentController;
import com.cellsgame.gateway.message.Message;
import com.cellsgame.gateway.message.ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MessageController extends AttachmentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
    private static final int MAX_MESSAGE_POOL_SIZE = 10;
    private SessionController session;
    private int lastClientMessageId = -1;
    private LinkedList<ServerMessage> messagePool;
    private int lastReceivedMessageId = 0;

    public MessageController(SessionController session) {
        this.session = session;
        messagePool = new LinkedList<>();
    }

    /**
     * 查看消息是否是正常消息。
     * <p>
     * 非正常消息将会直接丢弃
     *
     * @param lastClientMessageId 客户端发送的最后一个消息ID
     * @param lastServerMessageId 客户端收到的最后个服务端消息ID
     * @return 是否正常, 即消息是否需要逻辑处理
     */
    public boolean validate(int lastClientMessageId, int lastServerMessageId) {
        // 客户端发送消息是否正常
        boolean valid = this.lastClientMessageId < lastClientMessageId;

        LOGGER.debug("lastClientMessageId:  this:[{}]  clientSend:[{}] ", this.lastClientMessageId, lastClientMessageId);
        // 如果有效
        if (valid) {
            // 更新最后客户端消息ID
            this.lastClientMessageId = Math.max(this.lastClientMessageId, lastClientMessageId);
            // 将收到的服务器消息丢弃
            discardServerMessage(lastServerMessageId);
        }
        // 消息是否需要处理(客户端消息匹配且客户端收到的最后一条消息与服务器最后发送的消息匹配)
        LOGGER.debug("<<<<<<<<validate lastServerMessageId :[{}] validate lastReceivedMessageId :[{}]  validate###controller:{} ", lastServerMessageId, lastReceivedMessageId, session);
        return valid;
    }

    /**
     * 发送没压缩过的数据
     *
     * @param data 数据
     */
    public void sendMessage(byte[] data) {
        sendMessage(false, data);
    }

    /**
     * 发送消息
     *
     * @param compress 数据是否压缩过
     * @param data     数据体
     */
    public void sendMessage(final boolean compress, final byte[] data) {
        if (isActive())
            session.getConnection().execute(new Runnable() {
                @Override
                public void run() {
                    // 新建消息
                    ServerMessage message = new ServerMessage(session.getConnection().newBuffer(data.length + 1).writeBoolean(compress).writeBytes(data));
                    //
                    MessageController.this.sendMessageImpl(message);
                }
            });
    }

    public void sendMessage(final boolean compress, final Map data) {
        if (isActive())
            session.getConnection().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // TODO 协议替换成json格式 luojf945
//                        byte[] bytes = Helper.objectToBytes(data);
                         String co=data.get("co").toString();
                         data.remove("co");


                         int c = Integer.valueOf(data.get("c").toString());
                         data.remove("c");

                         Map dmap = (Map) data.get("d");
                         if(null==dmap){
                             dmap = GameUtil.createSimpleMap();
                         }

                        dmap.put("co",co);
                         byte[] bytes = JSONObject.toJSONString(dmap).
                                 getBytes(Charset.forName("UTF-8"));
                        // 新建消息
                        ServerMessage message = new ServerMessage(session.getConnection().
                                newBuffer(bytes.length + 5).writeBoolean(compress).writeIntLE(c)
                                .writeBytes(bytes));
                        //
                        MessageController.this.sendMessageImpl(message);
                    }catch (Exception e){
                        LOGGER.error("sendMessage objectToBytes :  {} ", data);
                        LOGGER.error("sendMessage error : ", e);
                    }
                }
            });
    }

    /**
     * 向当前消息管理器发送消息.
     *
     * @param message 消息数据
     */
    public void sendMessage(final ServerMessage message) {
        if (isActive()) session.getConnection().execute(() -> sendMessageImpl(message));
    }

    /**
     * 通知网关断开当前链接
     */
    public void sendCloseMesage() {
    	LOGGER.info("send close message");
        if (isActive()) session.getConnection().execute(() -> {
            final ServerMessage msg = new ServerMessage(Message.Type.Close, 0, session.getConnection().newBuffer(1).writeBoolean(true));
            //
            MessageController.this.sendMessageImpl(msg);
        });
    }

    private void sendMessageImpl(ServerMessage message) {
        // 最后发送的消息ID
        message.setLastClientMessageId(lastClientMessageId);
        // 最后收到的消息ID
        message.setLastServerMessageId(++lastReceivedMessageId);
        // 将消息缓冲, 以便客户端消费
        messagePool.offerLast(message);
        // 日志
        if (messagePool.size() >= MAX_MESSAGE_POOL_SIZE)
            LOGGER.warn("message pool size[{}] gte max[{}]", messagePool.size(), MAX_MESSAGE_POOL_SIZE);
        // connection
        LOGGER.debug("lastClientMessageId : [{}] lastReceivedMessageId : [{}]  ##validate#sendMessageImpl :{}", lastClientMessageId, lastReceivedMessageId, session);
        session.sendMessage(message.copy());
    }

    /**
     * 将所有缓存的服务器消息重新发送。
     * <p>
     * 消息还是需要确认
     */
    public void writeAllServerMessage() {
        if (isActive())
            session.getConnection().execute(() -> {
                        ByteBuf buf = session.getConnection().newBuffer(1024);
                        ByteBufUtil.writeUtf8(buf, "p_x_y_");
                        buf.writeIntLE(101);
                        buf.writeIntLE(messagePool.size());
                        for (ServerMessage message : messagePool) {
                            ByteBuf copy = message.getContent().copy();
                            buf.writeIntLE(message.getDataLength() + 8);
                            buf.writeIntLE(message.getLastClientMessageId());
                            buf.writeIntLE(message.getLastServerMessageId());
                            buf.writeBytes(copy);
                            ReferenceCountUtil.release(copy);
                        }
                        // 新建消息
                        ServerMessage messageArray = new ServerMessage(buf);
                        // 重新发送
                        session.sendMessage(messageArray);
                    }
            );
    }

    public List<Map> getAllServerMessage() {
        List<Map> msgList = GameUtil.createList();
        LinkedList<ServerMessage> copyMsgPool = new LinkedList<>();
        synchronized (messagePool) {
            copyMsgPool.addAll(messagePool);
        }
        for (ServerMessage message : copyMsgPool) {
            ByteBuf copy = message.getContent().copy();
            copy.readBoolean();
            byte[] bytes = new byte[copy.readableBytes()];
            copy.readBytes(bytes);

            // TODO 协议替换成json格式 luojf945
            // begin
//            try {
//                Map map = Helper.mapFromBytes(bytes);
//
//                map.put("si", message.getLastServerMessageId());
//                map.put("ci", message.getLastClientMessageId());
//                msgList.add(map);
//            } catch (IOException e) {
//                LOGGER.warn("", e);
//            }
            // end

            Map<String, Object> map = JSON.parseObject(bytes, HashMap.class, Feature.OrderedField);
            map.put("si", message.getLastServerMessageId());
            map.put("ci", message.getLastClientMessageId());

            msgList.add(map);

            ReferenceCountUtil.release(copy);
        }
        return msgList;
    }

    private void discardServerMessage(final int lastServerMessageId) {
        if (isActive())
            session.getConnection().execute(() -> {
                // 查看所有消息
                while (messagePool.size() > 0) {
                    // 第一个消息
                    ServerMessage message = messagePool.peek();
                    // 如果第一个消息还没有被客户端消费, 不再继续丢弃
                    if (message.getLastServerMessageId() > lastServerMessageId) break;
                    // 丢弃并消费
                    messagePool.poll().consume();
                }
            });
    }

    public final MessageController destroy() {
        if (isActive())
            session.getConnection().execute(() -> {
                while (messagePool.size() > 0) messagePool.poll().consume();
                messagePool = null;
                session = null;
                MessageController.super.destroy();
            });
        return this;
    }

    public boolean isActive() {
        return session != null && session.isActive();
    }

    public MessageController newSession(SessionController session) {
        if (this.session != null) this.session.destroy();
        this.session = session;
        return this;
    }

    public SessionController getSession() {
        return session;
    }

    public int getLastClientMessageId() {
        return lastClientMessageId;
    }

    public void copyClientMessageId(MessageController controller) {
        lastClientMessageId = controller.getLastClientMessageId();
    }

}
