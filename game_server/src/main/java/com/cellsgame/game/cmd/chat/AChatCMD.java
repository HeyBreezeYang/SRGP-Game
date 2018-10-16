package com.cellsgame.game.cmd.chat;

import java.util.Arrays;
import java.util.Map;

import com.cellsgame.common.util.cmd.Command;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author Aly on  2016-09-12.
 */
abstract class AChatCMD implements Command {
    @Override
    public final Object execute(Object src, Object command, Object[] parm) throws Exception {
        CMD cmd = ((CMD) parm[0]);
        parm[0] = command;
        return execute(((PlayerVO) src), cmd, ((String) command), Arrays.copyOf(parm, parm.length, String[].class));
    }

    public abstract Map execute(PlayerVO src, CMD cmd, String command, String[] parm) throws Exception;

}
