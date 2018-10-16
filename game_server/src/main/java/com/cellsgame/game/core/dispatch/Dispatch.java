package com.cellsgame.game.core.dispatch;

import com.cellsgame.conc.disruptor.SingleDisruptor;
import com.cellsgame.game.Bootstrap;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.SessionAttributeKey;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.context.SessionController;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.message.GameMessage;
import com.cellsgame.game.core.msgproc.process.MessageProcess;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.core.socket.CacheMessageController;
import com.cellsgame.game.module.log.cons.LogType;
import com.cellsgame.game.module.player.PlayerUtil;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.sys.funOpen.FunOpenChecker;
import com.cellsgame.game.util.VolatileLongRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Dispatch {
    private static final Logger log = LoggerFactory.getLogger(Dispatch.class);

    public static void init() {
        DispatchType.start();
    }

    public static void destroy() {
        DispatchType.shutdown();
    }

    public static void dispatchGameMessage(SessionController session, int messageType, int cix, int six, byte[] bytes) throws Exception {
        MessageController controller = CacheMessageController.getController(session);
        log.debug("session id : {}, cix :{} , six : {}", session, cix, six);
        // 如果是正常通信消息
        if (messageType == GameMessage.MessageType_Message) {
            // 需要验证消息的正确性
            if (!controller.validate(cix, six)) return;
        }
        // 分发消息
        dispatchGameMessage(controller, messageType, bytes);
    }

    private static void dispatchGameMessage(MessageController controller, int messageType, byte[] bytes) throws Exception {
        SingleDisruptor gameDisruptor = DispatchType.GAME.getDisruptor();
        if (gameDisruptor == null)
            throw new Exception("game logic dispatch not start...");
        Map<?, ?> result = null;
        GameMessage message = null;
        try {
            message = GameMessage.valueOf(messageType, bytes);
            if (message.getMessageType() == GameMessage.MessageType_Message) {
                checkMessageTime(controller);
                checkToken(controller, message);
            }
            if (!Bootstrap.allowPlayerIn.get()) {
                CodeGeneral code = CodeGeneral.General_SERVER_IS_SHUTDOWN;
                result = MsgUtil.brmAll(null, message.getCmd(), code.getCode() + code.getModule());
            } else {
                // 检查功能是否开启
                FunOpenChecker.getChecker()
                        .checkCommandLimit(controller, message.getCmd(), message.getOriginalClientMsg());

                //数据解析
                Object[] params = null;
                if (message.isExecParamsParser()) {
                    params = MessageProcess.getParam(message.getModule(), message.getCmd(), message.getOriginalClientMsg());
                }
                if (messageType != GameMessage.MessageType_Message) {
                    result = MessageProcess.exec(controller, message, params);
                } else {
                    if (message.getCmd() != Command.Player_PI) {
                        log.info("pid:{} recMsg : cmd:{}  param: {}",
                                controller.getAttribute(SessionAttributeKey.PLAYER_ID),
                                message.getCmd(), message.getOriginalClientMsg());
                    }
                    //通用请求将不进入主逻辑线程
                    if (Command.isSyncCmd(message.getCmd())) {
                        result = MessageProcess.exec(controller, message, params);
                    } else {
                        //执行逻辑
                        boolean have = Command.isCmdCanHaveGameMessage(message.getCmd());
                        GameMessage msg = null;
                        if (have) msg = message;
                        gameDisruptor.publish(controller, message.getCmd(), params, msg);
                    }
                }
            }
        } catch (LogicException e) {
            result = MsgUtil.brmAll(null, null == message ? -1 : message.getCmd(), e.getCode());
        } catch (Exception e) {
            log.error("on Exception param:{} ", message == null ? null : message.getOriginalClientMsg(), e);
            result = MsgUtil.brmAll(null, null == message ? -1 : message.getCmd(), CodeGeneral.General_ServerException.getCode());
        }
        if (result != null) {
            Push.multiThreadSend(controller, result);
        }
    }


    private static void checkMessageTime(MessageController controller) throws LogicException {
        if (PlayerUtil.getPlayerVO(controller) != null) {
            VolatileLongRef time = controller.getAttribute(SessionAttributeKey.TIME);
            long currTime = System.currentTimeMillis();
            if (null == time) {
                time = VolatileLongRef.create(currTime);
                controller.setAttribute(SessionAttributeKey.TIME, time);

                // 暂时取消时间校验
//            } else {
//                CodeGeneral.General_MessageFrequently.throwIfTrue(currTime - time.getElem() < 500);
            }
        }
    }

    private static void checkToken(MessageController controller, GameMessage message) throws LogicException {
        // 检测token
        String token = controller.getAttribute(SessionAttributeKey.TOKEN);
        if (token != null) {
            log.debug("checkToken token :  slf:{}  send:{}", token, message.getToken());
            CodeGeneral.General_VerifyTokenFail.throwIfTrue(!token.equals(message.getToken()));
        }
    }

    public static void dispatchGameLogic(MessageController controller, Object message) {
        DispatchType.GAME.getDisruptor().publish(controller, message);
    }

    public static void dispatchGameLogic(CatchRunnable run) {
        DispatchType.GAME.dispatch(run);
    }

    public static void dispatchGameLogic(Runnable run) {
        DispatchType.GAME.dispatch(run);
    }

    public static void dispatchGameLog(CMD cmd, EvtHolder holder, LogType logType, GameEvent event) {
        DispatchType.LOG.getDisruptor().publish(cmd, holder, logType, event);
    }

    public static void dispatchNotify(Runnable run) {
        DispatchType.NOTIFY.dispatch(run);
    }

    public static void dispatchHttpLogic(Runnable run) {
        DispatchType.HTTP.dispatch(run);
    }

    public static void tryDispatch(DispatchType type, Runnable runnable) {
        DispatchType.tryExeIn(type, runnable);
    }
}
