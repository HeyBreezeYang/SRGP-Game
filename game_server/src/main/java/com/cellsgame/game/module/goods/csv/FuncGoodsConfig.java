package com.cellsgame.game.module.goods.csv;

import com.cellsgame.game.module.func.FuncConfig;

public class FuncGoodsConfig extends GoodsConfig {

    public static final int FuncType_Player = 1;
    public static final int FuncType_Worker = 2;
    public static final int FuncType_Beauty = 3;

    /**
     * 功能类型
     * 1：玩家功能道具
     * 2：员工功能道具
     * 3：美女功能道具
     * */
    private int funcType;

    /**
     * 需求等級
     */
    private int reqLv;
    /**
     * 需求的vip等级
     */
    private int reqVip;

    private FuncConfig func;

    public int getReqLv() {
        return reqLv;
    }

    private void setReqLv(int reqLv) {
        this.reqLv = reqLv;
    }

    public int getReqVip() {
        return reqVip;
    }

    private void setReqVip(int reqVip) {
        this.reqVip = reqVip;
    }

    public int getFuncType() {
        return funcType;
    }

    public void setFuncType(int funcType) {
        this.funcType = funcType;
    }

    public FuncConfig getFunc() {
        return func;
    }

    public void setFunc(FuncConfig func) {
        this.func = func;
    }
}
