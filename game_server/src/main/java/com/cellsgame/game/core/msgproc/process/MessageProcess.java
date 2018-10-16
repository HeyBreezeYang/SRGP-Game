package com.cellsgame.game.core.msgproc.process;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.GameMessage;
import com.cellsgame.game.core.msgproc.process.asm.ModuleWriter;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleAsmInfo;
import com.cellsgame.game.util.DebugTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aly on  2016-07-13.
 */
public class MessageProcess {
    private static final Map<Integer, ILogicDispatcher> DISPATCHERS = GameUtil.createMap();
    private static Logger log = LoggerFactory.getLogger(MessageProcess.class);
    private static List<IListener> listeners = GameUtil.createList();

    public static void load(ModuleAsmInfo info) {
        ModuleWriter writer = new ModuleWriter(info, Command::isCmdCanHaveGameMessage);
        try {
            Class<?> boInterface = ModuleWriter.class.getClassLoader().
                    loadClass(info.getName().replace('/', '.'));
            Class<?> clazz = writer.writeToClass();
            Object bean = SpringBeanFactory.getBean(boInterface);
            Constructor<?> constructor = clazz.getConstructor(boInterface);
            ILogicDispatcher dispatcher = ((ILogicDispatcher) constructor.newInstance(bean));

            int moduleID = dispatcher.getModuleID();
            if (DISPATCHERS.containsKey(moduleID)) {
                throw new RuntimeException(String.format(" moduleID 重复 A:%s  \n\tB:%s",
                        DISPATCHERS.get(moduleID).getClass(), dispatcher.getClass()));
            } else {
                DISPATCHERS.put(moduleID, dispatcher);
                log.info("loadModule  {}  Type:{}", moduleID, boInterface);
            }
        } catch (Exception e) {
            DebugTool.throwException("", e);
        }
    }


    /**
     * 过滤客户端参数
     */
    public static Object[] getParam(int module, int method, Map<?, ?> params) throws LogicException {
        ILogicDispatcher parser = DISPATCHERS.get(module);
        CodeGeneral.General_NotFindProcess.throwIfTrue(parser == null);
        Object[] p = parser.getParam(method, params);
        if (p != null) {
            for (Object o : p)
                CodeGeneral.General_InvokeParamError.throwIfTrue(o == null);
        }
        return p;
    }

    /**
     * 将消息分发到 BO
     */
    public static Map<?, ?> exec(MessageController controller, GameMessage message, Object[] params) throws LogicException {
        if (null != message)
            switch (message.getMessageType()) {
                case GameMessage.MessageType_Open:
                case GameMessage.MessageType_Close:
                    for (IListener listener : listeners) {
                        // 此处注意消息隔离  中间某个报错了 可能中断其他Listener的逻辑
                        listener.exec(controller, message);
                    }
                    break;
                case GameMessage.MessageType_Message:
                    return exec(controller, message, message.getCmd(), params);
                default:
                    log.warn("不支持的消息类型 " + message.getMessageType());
                    return null;
            }
        return null;
    }

    /**
     * 将消息分发到 BO
     */
    public static Map<?, ?> exec(MessageController controller, GameMessage message, int cmd, Object[] params) throws LogicException {
        ILogicDispatcher parser = DISPATCHERS.get(cmd / ModuleID.MODULE_ID_BASE * ModuleID.MODULE_ID_BASE);
        CodeGeneral.General_NotFindProcess.throwIfTrue(parser == null);
        return parser.dispatchLogic(controller, cmd, params, message);
    }

    /**
     * 替换消息处理类
     */
    public static void replaceDispather(ILogicDispatcher dispatcher) {
        ILogicDispatcher put = DISPATCHERS.put(dispatcher.getModuleID(), dispatcher);
        if (null != put) {
            log.warn(" Dispatcher is replaced OLD:{} new:{}", put.getClass(), dispatcher.getClass());
        }
    }

    public static void regListener(IListener iListener) {
        listeners.add(iListener);
    }

    public static void Deprecated(int moduleID) {
        DISPATCHERS.remove(moduleID);
    }
}
