package com.cellsgame.game.module.sys.csv;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.sys.funOpen.FunOpenParam;

/**
 * Created by alyx on 17-6-13.
 * 功能开启
 */
public class FunOpenCfg extends BaseCfg {
    // 功能ID
    private int funID;
    // 开启条件
    private FunOpenParam open;

    public int getFunID() {
        return funID;
    }

    public void setFunID(int funID) {
        this.funID = funID;
    }

    public FunOpenParam getOpen() {
        return open;
    }

    public void setOpen(FunOpenParam open) {
        this.open = open;
    }
}
