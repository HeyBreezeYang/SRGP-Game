package com.cellsgame.game.module.func.impl.exec;

import java.util.Collection;

import com.cellsgame.game.module.func.AbstractFunc;
import com.cellsgame.game.module.func.FuncsExecutor;

public class AllFuncsExecutor extends FuncsExecutor<AllFuncsExecutor> {

    @Override
    public Collection<AbstractFunc> getFuncs(Object ... params) {
        return allFuncs;
    }

    @Override
    public AllFuncsExecutor innerCopy() {
        AllFuncsExecutor exec = new AllFuncsExecutor();
        for (AbstractFunc func : allFuncs) {
            exec.acceptFunc(func.clone());
        }
        return exec;
    }

}
