package com.cellsgame.game.module.func.impl.func;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Resource;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.SyncFunc;
import com.cellsgame.game.module.mail.bo.MailBO;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class MailPrizeFunc extends SyncFunc {

	@Resource
    private MailBO mailBO;

    @Override
    public Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        mailBO.sendSysMail(player.getId(), param.getExtraParams());
		return null;
	}

	@Override
	public Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) {
		return null;
	}
	

}
