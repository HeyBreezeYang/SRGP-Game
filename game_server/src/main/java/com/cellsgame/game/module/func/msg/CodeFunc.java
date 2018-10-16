package com.cellsgame.game.module.func.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

public enum CodeFunc implements ICode {
		INPUT_PARAM_ERROR(1)
	;
	
	private int code;
	
	CodeFunc(int code){
		this.code = code;
	}

	@Override
	public int getModule() {
		return ModuleID.Func;
	}

	@Override
	public int getCode() {
		return code;
	}

}
