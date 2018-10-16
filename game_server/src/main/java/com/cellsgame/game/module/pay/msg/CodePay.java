package com.cellsgame.game.module.pay.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

public enum CodePay implements ICode {

    NotFindPlayer(1), //没有找到角色
    NotFincOrderItem(2), //没有找到商品
    ExecFail(3), //执行失败

    ;

    private int code;

    private CodePay(int code){
        this.code = code;
    }

    @Override
    public int getModule() {
        return ModuleID.Pay;
    }

    @Override
    public int getCode() {
        return code;
    }
}
