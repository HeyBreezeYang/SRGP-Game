package com.cellsgame.game.module.func.formula;

import com.cellsgame.game.module.func.formula.impl.*;

public enum FormulaType {
    //经营系统
    _OperateChance(301, new OperateChance()),//经营机会奖励计算
    _OperateContacts(302, new OperateContacts()),//经营人脉奖励计算
    _OperateMoney(303, new OperateMoney()),//经营金币奖励计算
    //每日任务系统
    _QuestPrize1(401, new QuestPrize1()),//每日任务奖励 1档
    _QuestPrize2(402, new QuestPrize2()),//每日任务奖励 2档
    _QuestPrize3(403, new QuestPrize3()),//每日任务奖励 3档
    //寻访系统
    _SearchChanceHigh(501, new SearchChanceHigh()),//寻访机会奖励奖励 - 高档资源
    _SearchContactsHigh(502, new SearchContactsHigh()),//寻访人脉奖励 - 高档资源
    _SearchMoneyHigh(503, new SearchMoneyHigh()),//寻访金币奖励 - 高档资源
    _SearchChanceCenter(504, new SearchChanceCenter()),//寻访机会奖励奖励 - 中档资源
    _SearchContactsCenter(505, new SearchContactsCenter()),//寻访人脉奖励 - 中档资源
    _SearchMoneyCenter(506, new SearchMoneyCenter()),//寻访金币奖励 - 中档资源
    _SearchChanceLow(507, new SearchChanceLow()),//寻访机会奖励奖励 - 低档资源
    _SearchContactsLow(508, new SearchContactsLow()),//寻访人脉奖励 - 低档资源
    _SearchMoneyLow(509, new SearchMoneyLow()),//寻访金币奖励 - 低档资源
    ;
    private int id;

    private IFormula formula;

    FormulaType(int id, IFormula formula){
        this.id = id;
        this.formula = formula;
    }

    public int getId() {
        return id;
    }

    public IFormula getFormula() {
        return formula;
    }
}
