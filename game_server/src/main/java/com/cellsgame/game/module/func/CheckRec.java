package com.cellsgame.game.module.func;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.player.vo.PlayerVO;

public abstract class CheckRec<T extends CheckRec<T>>{
		
	public void add(T rec){
		if(rec == null)
			return;
		accept(rec);
	}
	
	@SuppressWarnings("unchecked")
	public void check(PlayerVO player) throws LogicException{
		getChecker().checkRec(player,  (T) this);
	}
	

	
	
	public abstract T create();
	
	protected abstract void accept(T rec);
	
	public abstract IRecChecker<T> getChecker();
}
