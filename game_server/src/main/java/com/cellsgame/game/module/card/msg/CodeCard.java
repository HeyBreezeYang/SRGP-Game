package com.cellsgame.game.module.card.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

public enum CodeCard implements ICode {

    NotFindConfig(1), //没有找到配置档
    CardNotOpen(2), //卡片没有激活
    CardEnd(3), //卡片已经结束
    AlreadyRevPrize(4), //已经领取奖励
    ;

    private int code;

    private CodeCard(int code){
        this.code = code;
    }

    @Override
    public int getModule() {
        return ModuleID.Card;
    }

    @Override
    public int getCode() {
        return code;
    }
}
