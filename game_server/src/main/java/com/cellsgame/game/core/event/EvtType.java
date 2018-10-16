package com.cellsgame.game.core.event;

import java.util.Map;

import com.cellsgame.game.core.message.CMD;

public interface EvtType {

    default GameEvent createEvt() {
        return new GameEvent(this);
    }

    int getEvtCode();

    void setEvtCode(int code);

    default Map<?, ?> happen(Map<?, ?> parent, CMD cmd, EvtHolder holder, EvtParam ... params) {
        return createEvt().happen(parent, cmd, holder, params);
    }
    
}
