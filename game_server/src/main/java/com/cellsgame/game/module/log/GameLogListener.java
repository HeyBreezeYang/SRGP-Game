package com.cellsgame.game.module.log;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.log.cons.LogType;

public class GameLogListener implements StaticEvtListener {

    private static final Logger Log = LoggerFactory.getLogger(GameLogListener.class);
	
	private Map<EvtType, LogType> mapping = GameUtil.createMap();

	@Override
	public EvtType[] getListenTypes() {
		List<EvtType> evtTypes = GameUtil.createList();
		for(LogType type : LogType.values()){
			for(EvtType evtType : type.getEvtTypes()){
				evtTypes.add(evtType);
				mapping.put(evtType, type);
			}
		}
		return evtTypes.toArray(new EvtType[evtTypes.size()]);
	}

	@Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
		if(cmd == null){
			Log.error("cmd is null...event : " + event.getType().toString() + ", class : " + event.getType().getClass(), new Exception());
			return parent;
		}
		EvtType type = event.getType();
		LogType logType = mapping.get(type);
		Dispatch.dispatchGameLog(cmd, holder, logType, event);
		return parent;
	}

}
