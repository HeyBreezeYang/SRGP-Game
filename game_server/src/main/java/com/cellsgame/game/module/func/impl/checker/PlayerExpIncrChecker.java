package com.cellsgame.game.module.func.impl.checker;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.player.msg.CodePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class PlayerExpIncrChecker implements IChecker {

	@Override
	public void check(PlayerVO player, FuncParam param) throws LogicException {
		  CodePlayer.Player_ExpMinus.throwIfTrue(param.getValue() < 0);
	}

}
