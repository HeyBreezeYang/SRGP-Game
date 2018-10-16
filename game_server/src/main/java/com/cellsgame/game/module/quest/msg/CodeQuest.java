package com.cellsgame.game.module.quest.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

public enum CodeQuest implements ICode {
	NOT_IN_QUEST(1),
	FIN_CDIT_NOT_SATISFIED(2),
	MANUAL_BEHAV_FIN(3)
	;

	private int code;
	
	
	CodeQuest(int code){
		this.code = code;
	}
	
	
	@Override
	public int getModule() {
		return ModuleID.Quest;
	}

	@Override
	public int getCode() {
		return code;
	}

}
