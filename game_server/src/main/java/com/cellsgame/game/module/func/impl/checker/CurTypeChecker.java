package com.cellsgame.game.module.func.impl.checker;

import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.depot.msg.CodeDepot;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class CurTypeChecker implements IChecker {
	
	@Override
	public void check(PlayerVO player, FuncParam param) throws LogicException {
		 CurrencyType type = Enums.get(CurrencyType.class, param.getParam());
		 CodeDepot.Depot_Cny_Type_Error.throwIfTrue(type == null);
	}
}
