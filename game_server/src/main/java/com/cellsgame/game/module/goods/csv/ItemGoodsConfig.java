package com.cellsgame.game.module.goods.csv;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.FuncsScriptUtil;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;

public class ItemGoodsConfig extends GoodsConfig {

    /**
     * 需求等級
     */
    private int reqLv;
    /**
     * 需求的vip等级
     */
    private int reqVip;

    /**
     * 是否自动开启
     */
    private boolean isAutoOpen;

    private FuncsExecutor funcs;

    public FuncsExecutor getFuncs(CMD cmd) {
        FuncsExecutor copy = funcs.copy();
        copy.setCmd(cmd);
        return copy;
    }

    public void setFuncs(String[] funcs) {
        FuncsExecutor<?> exec = FuncsExecutorsType.collection.getExecutor(CMD.system.now());
        try {
            for (String func : funcs) {
                exec.addExecutor(FuncsScriptUtil.trans(func));
            }
        } catch (LogicException e) {
            throw new RuntimeException(e);
        }
        this.funcs = exec;

    }

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

    public boolean isAutoOpen() {
        return isAutoOpen;
    }

    public void setIsAutoOpen(boolean autoOpen) {
        isAutoOpen = autoOpen;
    }
}
