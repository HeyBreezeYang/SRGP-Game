package com.cellsgame.game.module.func.impl.checker;

import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class ValueMinusChecker implements IChecker{

	@Override
	public void check(PlayerVO player, FuncParam param) throws LogicException {
		CodeGeneral.General_Func_Error.throwIfTrue(param.getValue()<0);
	}
}
