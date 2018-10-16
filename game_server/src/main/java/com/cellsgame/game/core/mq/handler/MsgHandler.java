package com.cellsgame.game.core.mq.handler;

import com.cellsgame.mq.zmq.ZMQData;
import com.cellsgame.mq.zmq.ZMQMsg;
import com.cellsgame.mq.zmq.ZMQMsgHandler;

/**
 * Created by yfzhang on 2017/8/17.
 */
public class MsgHandler implements ZMQMsgHandler {

    @Override
    public void handleEvent(ZMQMsg msg) {
        ZMQData data = msg.getData();
    }


}
