package com.cellsgame.game.core.event;

import java.util.HashSet;
import java.util.Set;

public abstract class EvtNoticer {
	private Set<EvtType> concernEvents;
	
	public EvtNoticer(){
		concernEvents = new HashSet<EvtType>();
		EvtType[] concernTypes = getConcernType();
		if(concernTypes == null)
			return;
		for (EvtType etype : concernTypes) {
			concernEvents.add(etype);
		}
	}
	
	/**
	 * 获取关注的事件类型
	 * @return 关注的事件类型数组
	 */
	public abstract  EvtType[] getConcernType();
	
	/**
	 * 是否监听事件
	 * @param event
	 * @return
	 */
	public boolean isListener(GameEvent event){
    	return concernEvents.contains(event.getType());
    }
}
