package com.cellsgame.game.module.chat;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.chat.cons.ChatType;
import com.cellsgame.game.module.chat.cons.NotifyType;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotifyListener implements StaticEvtListener {

    private static final Logger Log = LoggerFactory.getLogger(NotifyListener.class);
	
	private Map<EvtType, List<NotifyType>> mapping = GameUtil.createMap();

	private ChatBO chatBO;

	public void setChatBO(ChatBO chatBO) {
		this.chatBO = chatBO;
	}

	@Override
	public EvtType[] getListenTypes() {
		Set<EvtType> evtTypes = new HashSet<>();
		for(NotifyType type : NotifyType.values()){
			for(EvtType evtType : type.getEvtTypes()){
				if(!evtTypes.contains(evtType)) evtTypes.add(evtType);
				List<NotifyType> list = mapping.get(evtType);
				if(list == null) mapping.put(evtType, list = GameUtil.createList());
				list.add(type);
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
        if(holder instanceof PlayerVO){
			PlayerVO player = (PlayerVO)holder;
			EvtType type = event.getType();
			List<NotifyType> list = mapping.get(type);
			if(list != null) {
				for (NotifyType notifyType : list) {
					String[] params = notifyType.getProcess().builderChatMsg(player, event);
					if(params != null) {
						Dispatch.dispatchNotify(() -> {
							chatBO.notifyMsg(player, ChatType.Sys, notifyType.getType(), params);
						});
					}
				}
			}
		}
		return parent;
	}

}
