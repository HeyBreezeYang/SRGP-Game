package com.cellsgame.game.module.func.impl.exec;

import java.util.Collection;
import java.util.List;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.func.AbstractFunc;
import com.cellsgame.game.module.func.FuncsExecutor;

public class FuncsExecutorCollection extends FuncsExecutor<FuncsExecutorCollection> {
    private Collection<FuncsExecutor<?>> execs = GameUtil.createList();


    @Override
    public Collection<AbstractFunc> getFuncs(Object ... params) {
        List<AbstractFunc> ret = GameUtil.createList();
        for (FuncsExecutor<?> exec : execs) {
            ret.addAll(exec.getFuncs(params));
        }
        ret.addAll(allFuncs);
        return ret;
    }


    @Override
    public FuncsExecutorCollection innerCopy() {
        FuncsExecutorCollection execs = new FuncsExecutorCollection();
        for (FuncsExecutor exec : this.execs) {
            execs.addExecutor(exec.copy());
        }
        Collection<AbstractFunc> allFuncs = super.getAllFuncs();
        for (AbstractFunc func : allFuncs) {
            execs.acceptFunc(func.clone());
        }
        return execs;
    }

    public void addExecutor(FuncsExecutor<?> exec, Object ... inputParam) {
        if (exec == this)
            return;
        execs.add(exec);
    }


    @Override
    public Collection<AbstractFunc> getAllFuncs() {
        List<AbstractFunc> allFuncs = GameUtil.createList();
        for (FuncsExecutor<?> exec : execs) {
            allFuncs.addAll(exec.getAllFuncs());
        }
        allFuncs.addAll(super.getAllFuncs());
        return allFuncs;
    }

}
