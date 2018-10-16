package com.cellsgame.game.module.chat.cache;

import java.util.Map;

import com.cellsgame.common.util.cmd.CMDResolver;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aly on  2016-09-12.
 */
public class CacheChat {
    public static final String cmdPre = "/cmd";
    private static final Logger log = LoggerFactory.getLogger(CacheChat.class);
    private static CMDResolver cmdResolver = null;

    public static void initCMD(String cmdPkg) {
        CacheChat.cmdResolver = new ChatCMDResolver(cmdPkg);
    }

    public static Map<String, Object> exeCmd(PlayerVO pvo, CMD cmd, String command) {
        if (null == cmdResolver) return null;
        String[] split = command.split(" ");
        try {
            Object[] param = new Object[split.length];
            param[0] = cmd;

            System.arraycopy(split, 1, param, 1, split.length - 1);

            String cmdHead = split[0];
            Object o = cmdResolver.exeCmd(pvo, cmdHead, param);
            if (null != o) {
                //noinspection unchecked
                return (Map<String, Object>) o;
            }
        } catch (Exception e) {
            log.error("执行命令出错: 玩家:[{}]  -->命令:{}", pvo.getName(), command);
        }
        return null;
    }
}
