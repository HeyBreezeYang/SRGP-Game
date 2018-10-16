package com.cellsgame.game.module.sys.funOpen;

import com.cellsgame.game.cache.Enums;

/**
 * Created by alyx on 17-6-13.
 * 功能开启参数
 */
public class FunOpenParam {
    //    {type:2,param:55010004,value:3}
    private int type;
    private int param;
    private int value;
    private FunOpenCheckType checkType;

    public FunOpenCheckType getCheckType() {
        if (checkType == null) {
            checkType = Enums.get(FunOpenCheckType.class, type);
        }
        return checkType;
    }

    public int getType() {

        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

