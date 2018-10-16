package com.cellsgame.game.module.sys.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

public enum CodeSystem implements ICode{
		Suc(0)
	;
	
	private int code;
	
	private CodeSystem(int code) {
		this.code = code;
	}
	
	@Override
	public int getModule() {
		return ModuleID.System;
	}

	@Override
	public int getCode() {
		return code;
	}

}
