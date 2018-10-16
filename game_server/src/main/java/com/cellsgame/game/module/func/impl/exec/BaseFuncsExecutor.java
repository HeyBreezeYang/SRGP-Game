package com.cellsgame.game.module.func.impl.exec;

import java.util.Collection;
import java.util.List;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.func.AbstractFunc;
import com.cellsgame.game.module.func.FuncsExecutor;

public class BaseFuncsExecutor extends FuncsExecutor<BaseFuncsExecutor> {

    private List<AbstractFunc> unSelectFuncs = GameUtil.createList();

    @Override
    protected void whenAcceptFunc(AbstractFunc func) {
        unSelectFuncs.add(func);
    }

    @Override
    protected boolean isSupportManySelect() {
        return true;
    }

    @Override
    protected void addSelectFunc(List<AbstractFunc> select) {
        if (null == selectedFuncs) {
            selectedFuncs = select;
        } else {
            selectedFuncs.addAll(select);
        }
        unSelectFuncs.clear();
    }

    @Override
    public Collection<AbstractFunc> getFuncs(Object ... params) {
        return unSelectFuncs;
    }

    @Override
    public BaseFuncsExecutor innerCopy() {
        BaseFuncsExecutor exec = new BaseFuncsExecutor();
        for (AbstractFunc func : allFuncs) {
            exec.acceptFunc(func.clone());
        }
        return exec;
    }
}
