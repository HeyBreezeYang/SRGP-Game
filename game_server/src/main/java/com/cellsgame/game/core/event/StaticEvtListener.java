package com.cellsgame.game.core.event;

import java.util.Map;

import com.cellsgame.game.core.message.CMD;

public interface StaticEvtListener {

    EvtType[] getListenTypes();

    Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event);

}