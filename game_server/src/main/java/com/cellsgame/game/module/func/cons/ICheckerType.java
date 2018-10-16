package com.cellsgame.game.module.func.cons;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.func.impl.checker.*;

public enum ICheckerType {
    CurType(new CurTypeChecker()),
    PlayerExp(new PlayerExpIncrChecker()),
    FuncsGoods(new FuncsGoodsChecker()),
    SkillLearn(new SkillLearnChecker()),
    HeroSwallow(new HeroSwallowChecker()),
//    GemCapacity(new DepotCapacityChecker<>(GemVO.class)),
//    RuneCapacity(new DepotCapacityChecker<>(RuneVO.class)),
//    MagicStoneCapacity(new DepotCapacityChecker<>(MagicStoneVO.class)),
    PlayerLevel(new PlayerLevelChecker()),
    FuncTimes(new FuncTimesCostChecker()),
    ValueMinus(new ValueMinusChecker())


    ;

    ICheckerType(IChecker checker) {
        checkerClass = checker.getClass();
        this.checker = checker;
        SpringBeanFactory.autowireBean(checker);
    }

    private Class<? extends IChecker> checkerClass;

    private IChecker checker;


    public Class<? extends IChecker> getCheckerClass() {
        return checkerClass;
    }

    public void setCheckerClass(Class<? extends IChecker> checkerClass) {
        this.checkerClass = checkerClass;
    }

    public IChecker getChecker() {
        return checker;
    }

    public void setChecker(IChecker checker) {
        this.checker = checker;
    }


}
