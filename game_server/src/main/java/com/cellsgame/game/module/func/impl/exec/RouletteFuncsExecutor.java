package com.cellsgame.game.module.func.impl.exec;

import java.util.Collection;
import java.util.List;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.func.AbstractFunc;
import com.cellsgame.game.module.func.FuncsExecutor;

/**
 * 轮盘功能执行器
 *
 * @author peterveron
 */
public class RouletteFuncsExecutor extends FuncsExecutor<RouletteFuncsExecutor> {

    private int maxRate;

    @Override
    public Collection<AbstractFunc> getFuncs(Object ... params) {
        Collection<AbstractFunc> funcs = allFuncs;
        List<AbstractFunc> ret = GameUtil.createList();
        int random = GameUtil.r.nextInt(maxRate) + 1;
        int count = 0;
        for (AbstractFunc func : funcs) {
            count += func.getRate();
            if (count >= random) {
                ret.add(func);
                return ret;
            }
        }
        return ret;
    }


    @Override
    public void whenAcceptFunc(AbstractFunc func) {
        maxRate += func.getRate();
    }

    @Override
    public RouletteFuncsExecutor innerCopy() {
        RouletteFuncsExecutor exec = new RouletteFuncsExecutor();
        Collection<AbstractFunc> funcs = allFuncs;
        for (AbstractFunc func : funcs) {
            exec.acceptFunc(func.clone());
        }
        return exec;
    }

}
