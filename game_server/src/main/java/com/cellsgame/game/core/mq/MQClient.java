package com.cellsgame.game.core.mq;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.mq.config.MQConfig;
import com.cellsgame.game.core.mq.handler.MsgHandler;
import com.cellsgame.mq.zmq.ZMQClient;
import com.cellsgame.mq.zmq.ZMQMsg;

import java.util.Map;

/**
 * Created by yfzhang on 2017/8/17.
 */
public class MQClient {

    private static ZMQClient client = new ZMQClient(5000, 5000, -1, -1, -1, -1);

    private static byte[] fightServerId;
    private static byte[] fightGroupId;

    public static void start(){
        MQConfig mqConfig = MQConfig.getConfig();
        fightServerId =mqConfig.getFightServerId().getBytes();
        fightGroupId = mqConfig.getFightGroupId().getBytes();
        System.out.println("connect brokers:"+mqConfig.getBrokers());
        Runtime r = Runtime.getRuntime();
        client.start(new MsgHandler(), 1000, r.availableProcessors(), mqConfig.getBrokers(), mqConfig.getServerId(), mqConfig.getGroupId());
    }

    public static void stop(){
        client.stop();
    }

//    public static void push(FightDTO fightContext, boolean autoFight){
//        client.sendMsg(new ZMQMsg(fightServerId, fightGroupId, new ReqFightMessage(fightContext, autoFight)));
//    }

}
