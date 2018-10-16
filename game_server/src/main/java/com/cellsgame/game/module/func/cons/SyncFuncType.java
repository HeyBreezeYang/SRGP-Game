package com.cellsgame.game.module.func.cons;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.module.func.AbstractFunc;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.SyncFunc;
import com.cellsgame.game.module.func.impl.func.*;


public enum SyncFuncType {                          // 添加道具
    NullFunc(0, new NullFunc()),
    LearnSkill(1, new SkillLearnFunc()),            // 学习技能
    ChangeGoods(2, new ChangeGoodsFunc()),          // 变更物品
    SwallowHero(3, new HeroSwallowFunc()),          // 吞噬英雄(突破)
    // 3 : 变更商会贡献
    ChangeCur(4, new ChangeCurFunc()),              // 变更货币
    SendMail(8, new MailPrizeFunc()),
    AddPlayerVipExp(9, new AddPlayerVipExpFunc()),       // 增加VIP经验
    AddPlayerExp(10, new AddPlayerExpFunc()),       // 增加主角经验
    ExecFuncsGoods(11, new FuncsGoodsFunc()),       // 开宝箱
    AddActiveness(15, new AddActivenessFunc()),     // 活跃度
    // 17: 指定美女增加亲密度
    PlayerLevel(18, new PlayerLevelFunc()),         // 玩家等级
    CollectManualCost(19, new NullFunc()),          // 需要玩家自己选择扣除收集类型物品及数量, 仅用于完成任务时消耗，不能用于其它功能
    ChangeGuildMny(20, new ChangeGuildMnyFunc()),   // 需要玩家自己选择扣除收集类型物品及数量, 仅用于完成任务时消耗，不能用于其它功能
    OpenCard(24, new OpenCardFunc()), //卡开启
    ChangeFuncTimes(30, new ChangeFuncTimesFunc()),//修改功能次数
    ;
    private int type;
    private SyncFunc func;

    SyncFuncType(int type, SyncFunc func) {
        this.type = type;
        this.func = func;
        SpringBeanFactory.autowireBean(func);
    }


    public int getType() {
        return type;
    }

    public SyncFunc getFunc() {
        return (SyncFunc) func.clone();
    }

    public SyncFunc createFunc(FuncParam param) {
        AbstractFunc clone = func.clone();
        clone.setParam(param);
        return (SyncFunc) clone;
    }
}
