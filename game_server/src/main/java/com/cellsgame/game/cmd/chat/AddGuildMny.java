package com.cellsgame.game.cmd.chat;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author Aly on 2017-03-13.
 */
public class AddGuildMny extends AChatCMD {
    @Override
    public Object getName() {
        return "addgmny";
    }

    @Override
    public Map execute(PlayerVO src, CMD cmd, String command, String[] parm) throws Exception {
        Map<Object, Object> ret = GameUtil.createSimpleMap();
        FuncsExecutorsType.Base.getExecutor(cmd).addSyncFunc(SyncFuncType.ChangeGuildMny, 0, Integer.valueOf(parm[1])).exec(ret, src);
        return ret;
    }
}
