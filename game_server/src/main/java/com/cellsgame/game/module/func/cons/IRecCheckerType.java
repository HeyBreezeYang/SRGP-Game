package com.cellsgame.game.module.func.cons;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.IRecChecker;
import com.cellsgame.game.module.func.impl.checker.*;

public enum IRecCheckerType {
    CurCost(new CurCostChecker()),
    GoodsCost(new GoodsCostChecker()),
    GuildMnyCost(new GuildMnyCostChecker()),
    ;

    private Class<? extends IRecChecker> checkerClass;
    private IRecChecker<?> checker;

    IRecCheckerType(IRecChecker<?> checker) {
        checkerClass = checker.getClass();
        this.checker = checker;
        SpringBeanFactory.autowireBean(checker);
    }

    public Class<? extends IRecChecker> getCheckerClass() {
        return checkerClass;
    }


    public <T extends CheckRec<T>> IRecChecker<T> getChecker() {
        return (IRecChecker<T>) checker;
    }


}
