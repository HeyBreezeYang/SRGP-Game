package com.cellsgame.game.module.func.impl.checker;

import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cons.FuncTimes;
import com.cellsgame.game.module.player.vo.PlayerVO;

import javax.annotation.Resource;

public class FuncTimesCostChecker implements IChecker{

	@Resource
	private PlayerBO playerBO;

	public void setPlayerBO(PlayerBO playerBO) {
		this.playerBO = playerBO;
	}

	@Override
	public void check(PlayerVO player, FuncParam param) throws LogicException {
		if(param.getValue()<0){
			int funcTimeId = param.getParam();
			int value = (int)param.getValue();
			playerBO.checkFuncTimes(player, Enums.get(FuncTimes.class, funcTimeId), Math.abs(value));
		}
	}
}
