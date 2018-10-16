package com.cellsgame.game.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.sys.msg.CodeSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 多路消息发送器 采用无界线程池进行消息发送
 */
public class MsgUtil {

    //发送给客户端的key
    public static final String TO_CLIENT_COMMAND = "c";
    public static final String TO_CLIENT_CODE = "co";
    public static final String TO_CLIENT_DATA = "d";
    public static Log log = LogFactory.getLog(MsgUtil.class);

    private static Map<String, Object> brmCmdAndReturn(Object ret, int cmd, ICode code) {
        return brmCmdAndReturn(ret, cmd, null != code ? code.get() : -1);
    }


    private static Map<String, Object> brmCmdAndReturn(Object ret, int cmd, int code) {
        Map<String, Object> result = new HashMap<>();
        result.put(TO_CLIENT_CODE, code);
        result.put(TO_CLIENT_COMMAND, cmd);
        if (ret != null)
            result.put(TO_CLIENT_DATA, ret);
        return result;
    }

    public static Map<String, Object> brmAll(Map<?, ?> ret, int cmd, int code) {
        return brmCmdAndReturn(ret, cmd, code);
    }

    public static Map<String, Object> brmAll(int cmd, int code) {
        return brmCmdAndReturn(null, cmd, code);
    }


    public static Map<String, Object> brmAll(Map<?, ?> ret, int cmd, ICode code) {
        return brmCmdAndReturn(ret, cmd, code);
    }


    public static Map<String, Object> brmAll(int cmd, ICode code) {
        return brmCmdAndReturn(null, cmd, code);
    }


    /**
     * 默认Code码为Suc
     */
    public static Map<String, Object> brmAll(CMD cmd) {
        return brmCmdAndReturn(null, cmd.getCmd(), CodeSystem.Suc);
    }
    public static Map<String, Object> brmAll(int cmd) {
        return brmCmdAndReturn(null, cmd, CodeSystem.Suc);
    }

    /**
     * 默认Code码为Suc
     */
    public static Map<String, Object> brmAll(Map ret, CMD cmd) {
        return brmAll(ret, cmd.getCmd());
    }
    public static Map<String, Object> brmAll(Map ret, int cmd) {
        return brmCmdAndReturn(ret, cmd, CodeSystem.Suc);
    }

    /**
     * 默认Code码为Suc
     */
    public static Map<String, Object> brmAll(List<Map> ret, int cmd) {
        return brmCmdAndReturn(ret, cmd, CodeSystem.Suc);
    }

    public static Map<String, Object> dealLoginException(int cmd, LogicException e) {
        String info = e.getMessage();
        log.debug("CMD[" + cmd + "]  ---CODE[" + e.getCode() + "]" + " : " + info);
        return MsgUtil.brmAll(null, cmd, e.getCode());
    }


}