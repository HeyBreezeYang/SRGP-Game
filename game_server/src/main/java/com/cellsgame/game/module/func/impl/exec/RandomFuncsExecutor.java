package com.cellsgame.game.module.func.impl.exec;

import java.util.Collection;
import java.util.List;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.func.AbstractFunc;
import com.cellsgame.game.module.func.FuncsExecutor;

public class RandomFuncsExecutor extends FuncsExecutor<RandomFuncsExecutor> {

    private int randomNum;


    @Override
    public Collection<AbstractFunc> getFuncs(Object ... params) {
        Collection<AbstractFunc> funcs = allFuncs;
        int funcSize = funcs.size();
        int selectNum = Math.min(randomNum, funcSize);
        List<AbstractFunc> ret = GameUtil.createList();
        ret.addAll(funcs);
        if (selectNum < funcSize) {
            int rm = funcSize - selectNum;
            for (int i = 0; i < rm; i++) {
                ret.remove(GameUtil.r.nextInt(ret.size()));
            }
        }
        return ret;
    }


    @Override
    public void setExecutorParam(int param) {
        randomNum = param;
    }

    @Override
    public RandomFuncsExecutor innerCopy() {
        RandomFuncsExecutor exec = new RandomFuncsExecutor();
        Collection<AbstractFunc> funcs = allFuncs;
        for (AbstractFunc func : funcs) {
            exec.acceptFunc(func.clone());
        }
        exec.setExecutorParam(randomNum);
        return exec;
    }


}
