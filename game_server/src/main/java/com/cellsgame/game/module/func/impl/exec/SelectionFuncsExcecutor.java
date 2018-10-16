package com.cellsgame.game.module.func.impl.exec;

import java.util.Collection;
import java.util.List;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.func.AbstractFunc;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.msg.CodeFunc;

public class SelectionFuncsExcecutor extends FuncsExecutor<SelectionFuncsExcecutor> {
	
	private int reqParamNum;
	
    @Override
    public void setExecutorParam(int param) {
    	reqParamNum = param;
    }
	
	
	@Override
	public Collection<AbstractFunc> getFuncs(Object ... params) {
		Collection<AbstractFunc> funcs = allFuncs;
		AbstractFunc[] funcAry = 	funcs.toArray( new AbstractFunc[funcs.size()]);
		CodeFunc.INPUT_PARAM_ERROR.throwIfTrue(params == null || params.length!=reqParamNum || params.length>funcAry.length);
		List<AbstractFunc> selected = GameUtil.createList();
		for (int i = 0; i < params.length; i++) {
			Integer ix = (Integer) params[i] ;
			CodeFunc.INPUT_PARAM_ERROR.throwIfTrue(ix < 0 || ix >= funcAry.length || funcAry[ix] == null);
			selected.add(funcAry[ix]);
			funcAry[ix] = null;
		}
		return selected;
	}

	@Override
	protected SelectionFuncsExcecutor innerCopy() {
		SelectionFuncsExcecutor exec = new SelectionFuncsExcecutor();
		Collection<AbstractFunc> funcs = allFuncs;
		for (AbstractFunc func : funcs) {
			exec.acceptFunc(func.clone());
		}
		exec.setExecutorParam(reqParamNum);
		return exec;
	}

}
