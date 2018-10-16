package com.cellsgame.game.cmd.chat;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author Aly on  2016-09-12.
 */
public class AddCnt extends AChatCMD {
    @Override
    public Object getName() {
        return "addcnt";
    }

    @Override
    public Map execute(PlayerVO src, CMD cmd, String command, String[] parm) throws Exception {
        CurrencyType type = Enums.get(CurrencyType.class, Integer.parseInt(parm[1]) + CurrencyType.typeFix);
        Map ret = GameUtil.createSimpleMap();
        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd).addSyncFunc(SyncFuncType.ChangeCur, type.getType(), Integer.parseInt(parm[2]));
        executor.exec(ret, src);
        return ret;
    }
}
