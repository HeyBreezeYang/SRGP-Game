package com.cellsgame.game.core.event;


import java.util.HashMap;
import java.util.Map;

import com.cellsgame.game.core.message.CMD;

@SuppressWarnings("rawtypes")
public final class GameEvent extends EvtParamHolder {
    private EvtType type;

    GameEvent(EvtType type) {
        super();
        this.type = type;
    }
    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> T getType() {
        return (T) type;
    }

    /**
     * 严禁将子模块下定义的某一个类或接口作为核心模块的参数
     * 否则核心模块将与某个子模块产生强耦合!!!!!!!
     * @param parent
     * @param cmd
     * @param holder
     * @param params
     * @return
     */

	public final Map<?, ?> happen(Map<?, ?> parent, CMD cmd, EvtHolder holder, EvtParam... params) {
        if (params.length > 0 && null == evtParam) {
            evtParam = new HashMap<>(params.length);
        }
        for (EvtParam param : params) {
            addParam(param);
        }
        return GameEventManager.eventHappen(parent, cmd, holder, this);
    }
}
