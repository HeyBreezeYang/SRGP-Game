package com.cellsgame.game.core.message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.cellsgame.common.buffer.Helper;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.excption.LogicException;

public class GameMessage {
    public final static String MESSAGE_CODE = "co";
    public final static int MessageType_Open = 0;
    public final static int MessageType_Close = 1;
    public final static int MessageType_Message = 2;
    private final static String MESSAGE_CMD = "c";
    private final static String MESSAGE_DATA = "d";
    private final static String MESSAGE_TOKEN = "t";
    private final static int REQUEST_FACTOR = 10000;

    private int cmd;

    private int module;

    private Map<?, ?> OriginalClientMsg;        // 客户端Data中的消息

    private String token;

    private int messageType;

    public static GameMessage valueOf(byte[] bytes) throws LogicException, IOException {
        System.out.println("====byte ------ "+bytes.toString());
        System.out.println("进入value of 方法");
        // TODO 协议替换成json格式 luojf945
//        Map<?, ?> data = Helper.mapFromBytes(bytes);
        Integer cmd = bytesToIntLE(bytes);
        byte[] newBytes = new byte[bytes.length-4];


        System.arraycopy(bytes, 4, newBytes, 0, bytes.length-4);

        Map<?, ?> data = JSON.parseObject(newBytes, HashMap.class, Feature.OrderedField);
       /* GameMessage msg = valueOf(data);*/
        System.out.println("请求数据==========进入GameMessage line37");
        GameMessage msg= new GameMessage();
        msg.cmd=cmd;
        msg.module=(cmd / REQUEST_FACTOR * REQUEST_FACTOR);
        msg.setToken(String.valueOf(data.get(GameMessage.MESSAGE_TOKEN)));
        msg.OriginalClientMsg = data;
        msg.messageType = GameMessage.MessageType_Message;
        return msg;
    }

    private static int bytesToIntLE(byte[] bytes) {
        return ((bytes[3] & 0xff)<<24)|
                ((bytes[2] & 0xff)<<16)|
                ((bytes[1] & 0xff)<<8)|
                (bytes[0] & 0xff);
    }

    public static GameMessage valueOf(int messageType, byte[] bytes) throws LogicException, IOException {
        if (messageType == GameMessage.MessageType_Message) {
            CodeGeneral.General_InvokeParamError.throwIfTrue(null == bytes);
            return valueOf(bytes);
        } else {
            GameMessage msg = new GameMessage();
            msg.messageType = (messageType);
            return msg;
        }
    }

    private static GameMessage valueOf(Map<?, ?> message) {
//        log.debug(" clientFull msg:{}", message);
        GameMessage request = new GameMessage();
        if (message.containsKey(GameMessage.MESSAGE_CMD)) {
            Object cmd = message.get(GameMessage.MESSAGE_CMD);
            int cmdV;
            if (cmd instanceof Integer) {
                cmdV = (Integer) cmd;
            } else {
                cmdV = Integer.parseInt(cmd.toString());
            }
            request.cmd = (cmdV);
        } else
            throw new IllegalArgumentException("Request.valueOf() params error....");

        request.module = (request.getCmd() / REQUEST_FACTOR * REQUEST_FACTOR);
        request.setToken(String.valueOf(message.get(GameMessage.MESSAGE_TOKEN)));
        if (message.containsKey(GameMessage.MESSAGE_DATA))
            request.OriginalClientMsg = (Map<?, ?>) message.get(GameMessage.MESSAGE_DATA);
        return request;
    }

    public int getModule() {
        return module;
    }

    public Map<?, ?> getOriginalClientMsg() {
        return OriginalClientMsg;
    }

    public int getCmd() {
        return cmd;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isExecParamsParser() {
        return getMessageType() == GameMessage.MessageType_Message;
    }

    // TODO test json to map
    public static void main(String args[]) throws IOException {
        String jsonStr = "{\"id\":1,\"name\":\"jb51\",\"email\":\"admin@jb51.net\",\"interest\":[\"wordpress\",\"php\"]}";

        Map<?, ?> data = JSON.parseObject(jsonStr.getBytes(), HashMap.class, Feature.OrderedField);


        List<String> list = (List<String>)data.get("interest");
        System.out.println(data);
    }
}
