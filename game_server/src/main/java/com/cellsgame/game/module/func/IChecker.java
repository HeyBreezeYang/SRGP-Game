package com.cellsgame.game.module.func;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.player.vo.PlayerVO;

public interface IChecker{
	

    void check(PlayerVO player, FuncParam param) throws LogicException;
  
}
