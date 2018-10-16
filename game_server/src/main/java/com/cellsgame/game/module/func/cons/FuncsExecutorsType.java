package com.cellsgame.game.module.func.cons;

import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.AbstractFunc;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.impl.exec.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum FuncsExecutorsType {
    All(new AllFuncsExecutor()),
    Random(new RandomFuncsExecutor()),
    Roulette(new RouletteFuncsExecutor()),
    collection(new FuncsExecutorCollection()),
    Base(new BaseFuncsExecutor()),
    Selection(new SelectionFuncsExcecutor());


    private FuncsExecutor executor;

    FuncsExecutorsType(FuncsExecutor executor) {
        this.executor = executor;
//        SpringBeanFactory.autowireBean(executor);
    }

//    public FuncsExecutor getExecutor() {
//        return executor.copy();
//    }

    public FuncsExecutor getExecutor(CMD cmd) {
        FuncsExecutor copy = executor.copy();
        copy.setCmd(cmd);
        return copy;
    }

    public FuncsExecutor getExecutor(CMD cmd, Function function) {
        FuncsExecutor copy = executor.copy();
        copy.setCmd(cmd);
        copy.setFunction(function);
        return copy;
    }
//    public void setExecutor(FuncsExecutor executor) {
//        this.executor = executor;
//    }

}
