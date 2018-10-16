package com.cellsgame.game.module.chat.cache;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.chat.vo.ChatVO;

public class CacheChatInfo {
	
	private static Map<Integer, ChatVO> cache = GameUtil.createSimpleMap();
	
	public static void cacheChatInfo(ChatVO info){
		cache.put(info.getPid(), info);
	}

	public static ChatVO getChatInfoByPid(int pid){
		return cache.get(pid);
	}
	
}
