package com.cellsgame.game.core.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aly on  2016-10-26.
 */
public class EvtParamHolder {
    Map<EvtParamType, Object> evtParam;

    EvtParamHolder() {
    }

    public static EvtParamHolder of(EvtParam... params) {
        EvtParamHolder holder = new EvtParamHolder();
        for (EvtParam param : params) {
            holder.addParam(param);
        }
        return holder;
    }

    @SuppressWarnings("unchecked")
    public <T> T getParam(EvtParamType<T> type) {
        return evtParam == null ? null : (T) evtParam.get(type);
    }

    /**
     * @param deft 如果没有设置参数 则返回默认值
     */
    public <T> T getParam(EvtParamType<T> type, T deft) {
        T val = getParam(type);
        return val == null ? deft : val;
    }

    void addParam(EvtParam param) {
        if (evtParam == null) {
            evtParam = new HashMap<>();
        }
        evtParam.put(param.type, param.val);
    }

    public int size() {
        return null == evtParam ? 0 : evtParam.size();
    }
}
