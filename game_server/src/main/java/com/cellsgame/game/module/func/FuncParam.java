package com.cellsgame.game.module.func;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;
import org.springframework.util.CollectionUtils;

public class FuncParam extends DBVO {

    @Save(ix = 1)
    private int param;

    @Save(ix = 2)
    private long value;

    @Save(ix = 3)
    private boolean overMax = true;

    @Save(ix = 4)
    private int param2;

    @Save(ix = 5)
    private int param3;


    // 暂时只作为临时缓存使用
    private Map<EvtParamType, Object> extraParams;

    public FuncParam() {
        this(0, 0);
    }

    public FuncParam(int param, long value) {
        this.param = param;
        this.value = value;
    }

    public FuncParam(long value) {
        this.value = value;
    }

    public FuncParam addExtra(EvtParamType key, Object value) {
        if (extraParams == null) extraParams = GameUtil.createSimpleMap();
        extraParams.put(key, value);
        return this;
    }

    public <T> T getExtra(EvtParamType key, T defaultValue) {
        //noinspection unchecked
        return CollectionUtils.isEmpty(extraParams) ? defaultValue : (T) extraParams.getOrDefault(key, defaultValue);
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public boolean isOverMax() {
        return overMax;
    }

    public void setOverMax(boolean overMax) {
        this.overMax = overMax;
    }

    public int getParam2() {
        return param2;
    }

    public void setParam2(int param2) {
        this.param2 = param2;
    }

    public int getParam3() {
        return param3;
    }

    public void setParam3(int param3) {
        this.param3 = param3;
    }

    public Map<EvtParamType, Object> getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(Map<EvtParamType, Object> extraParams) {
        this.extraParams = extraParams;
    }


    @Override
    protected Object initPrimaryKey() {
        return null;
    }

    @Override
    protected Object getPrimaryKey() {
        return null;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
    }

    @Override
    protected Object[] getRelationKeys() {
        return null;
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {

    }

    @Override
    protected void init() {
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {
    }
}
