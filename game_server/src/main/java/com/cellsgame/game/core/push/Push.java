package com.cellsgame.game.core.push;

import com.cellsgame.common.buffer.Helper;
import com.cellsgame.game.cons.SessionAttributeKey;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.module.player.bo.impl.PlayerBOImpl;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * todo 增加批量发送接口 减少消息的重复编码
 */
public class Push {
    private static final Logger log = LoggerFactory.getLogger(Push.class);
    private static volatile boolean working = true;

    public static void multiThreadSend(MessageController controller, Map<?, ?> result) {
        send(controller, false, result, null);
    }

    public static void multiThreadSend(MessageController controller, Map<?, ?> result, Runnable run) {
        send(controller, false, result, run);
    }

    public static void multiThreadSend(MessageController controller, byte[] result) {
        send(controller, false, result, null);
    }

    public static void multiThreadSend(MessageController controller, boolean compress, byte[] result) {
        send(controller, compress, result, null);
    }

    public static void multiThreadSend(PlayerVO vo, Map<String, Object> ret) {
        MessageController controller = vo.getMessageController();
        if (null != controller) {
            multiThreadSend(controller, ret);
        }
    }

    private static void send(MessageController controller, boolean compressed, Object result, Runnable run) {
        if (controller == null)
            return;
        if (working) {
            if (PlayerBOImpl.PI_Message != result) {
                log.debug("pid:{} ret:{}", controller.getAttribute(SessionAttributeKey.PLAYER_ID), result);
            }
            if( result instanceof Map){
                controller.sendMessage(compressed, (Map)result);
            }else if( result instanceof  byte[]){
                controller.sendMessage(compressed, (byte[])result);
            }
            if(run != null){
                run.run();
            }
        } else
            log.info("Push sendPool closed:  msgNotSend:{}", result);
    }

}
