package com.cellsgame.game.module.func;

import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.orm.enhanced.annotation.Save;
import com.google.common.collect.Maps;

public class FuncConfig extends FuncParam {

    public FuncConfig() {
    }

    public FuncConfig(int type, int param, long value) {
        this(type, param, value, false);
    }

    public FuncConfig(int type, int param, long value, boolean overMax) {
        setType(type);
        setParam(param);
        setValue(value);
        setOverMax(overMax);
    }

    @Save(ix = 1)
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public FuncConfig copy() {
        FuncConfig clone = new FuncConfig();
        clone.setType(this.getType());
        clone.setParam(this.getParam());
        clone.setValue(this.getValue());
        clone.setParam2(this.getParam2());
        clone.setParam3(this.getParam3());
        if (this.getExtraParams() != null)
            clone.setExtraParams(Maps.newHashMap(this.getExtraParams()));
        return clone;
    }

    public String toString() {
        return JSONUtils.toJSONString(this);
    }
}
