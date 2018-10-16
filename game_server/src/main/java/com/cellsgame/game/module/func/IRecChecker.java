package com.cellsgame.game.module.func;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.player.vo.PlayerVO;

public interface IRecChecker<T extends CheckRec<T>> extends IChecker{
	   
	void checkRec(PlayerVO player, T rec) throws LogicException;
	
	
	
}
