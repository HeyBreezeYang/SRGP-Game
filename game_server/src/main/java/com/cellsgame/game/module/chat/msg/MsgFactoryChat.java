package com.cellsgame.game.module.chat.msg;

import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.chat.vo.ChatMsgVO;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.vo.GuildMemberVO;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MsgFactoryChat extends MsgFactory {
	
	

    public static final String CHAT_LIST = "lst";

    public static final String NOTIFY_MSG = "nMsg";
    public static final String NOTIFY_MSG_PARAMS = "pms";


    public static final String CHAT_MSG = "msg";
    public static final String CHAT_TYPE = "tp";
    public static final String CHAT_PLAYER_ID = "pid";
    public static final String CHAT_PLAYER_NAME = "pnm";
    public static final String CHAT_PLAYER_LEVEL = "plv";
    public static final String CHAT_TARGET_PLAYER_ID = "tpid";
    

    public static final String CHAT_GUILD_NAME = "gnm";
    public static final String CHAT_GUILD_LEVEL = "glv";
    public static final String CHAT_GUILD_RIGHT = "grt";
    
    private static final MsgFactoryChat instance = new MsgFactoryChat();

    public static MsgFactoryChat instance() {
        return instance;
    }

    @Override
    public String getModulePrefix() {
        return CHAT;
    }


    public Map<String, Object> getChatMsg(Map parent, PlayerVO pvo, String msg) {
        parent = getPlayerMsg(parent, pvo);
    	parent = getPlayerGuildMsg(parent, pvo);
    	Map hInfo = gocOpMap(parent);
        hInfo.put(CHAT_MSG, msg);
        return parent;
    }

    public Map<String, Object> getChatMsg(Map parent, PlayerVO pvo, String tPid, String msg) {
        Map hInfo = gocOpMap(parent);
        hInfo.put(CHAT_TARGET_PLAYER_ID, tPid);
        parent = getChatMsg(parent, pvo, msg);
        return parent;
    }


    public Map<String,Object> getNotifyMsg(Map parent, int msgType, String[] msgParams) {
        Map hInfo = gocOpMap(parent);
        hInfo.put(NOTIFY_MSG, msgType);
        hInfo.put(NOTIFY_MSG_PARAMS, msgParams);
        return parent;
    }

    public Map<String, Object> getChatMsg(Map parent, int type, String msg) {
        Map hInfo = gocOpMap(parent);
        hInfo.put(CHAT_MSG, msg);
        hInfo.put(CHAT_TYPE, type);
        return parent;
    }

    public Map<String, Object> getChatMsg(Map parent, PlayerVO pvo, int type, String msg) {
        parent = getChatMsg(parent, pvo, msg);
    	Map hInfo = gocOpMap(parent);
        hInfo.put(CHAT_MSG, msg);
        hInfo.put(CHAT_TYPE, type);
        return parent;
    }
    
    public Map getPlayerMsg(Map parent, PlayerVO pvo) {
        Map hInfo = gocOpMap(parent);
        hInfo.put(ID, pvo.getId());
//        hInfo.put(IMG, pvo.getImage());
        hInfo.put(CHAT_PLAYER_ID, pvo.getId());
        hInfo.put(CHAT_PLAYER_NAME, pvo.getName());
//        hInfo.put(CHAT_PLAYER_LEVEL, pvo.getLevel());
//        hInfo.put(VIP, pvo.getVip());
        return parent;
    }
    
    public Map getPlayerGuildMsg(Map parent, PlayerVO pvo){
        Map hInfo = gocOpMap(parent);
        GuildMemberVO memberVO = CacheGuild.getGuildMemberVO(pvo.getId());
        if (null != memberVO) {
            GuildVO guildVO = CacheGuild.getGuildByID(memberVO.getGuildID());
            if(guildVO != null){
                hInfo.put(CHAT_GUILD_NAME, guildVO.getName());
                hInfo.put(CHAT_GUILD_LEVEL, guildVO.getLevel());
                hInfo.put(CHAT_GUILD_RIGHT, guildVO.getMemberRights().get(pvo.getId()));
            }
        }
        return parent;
    }

	public Map createOfflineChatMsg(Map parent, List<ChatMsgVO> privateMsg) {
        Map hInfo = gocOpMap(parent);
		List list = gocLstIn(hInfo, CHAT_LIST);
		for(ChatMsgVO vo : privateMsg){
		    Map info = createOfflineChatMsg(vo);
		    if(info != null)
			    list.add(info);
		}
		return parent;
	}
	
	public Map createOfflineChatMsg(ChatMsgVO msgVO){
        PlayerInfoVO info = CachePlayerBase.getBaseInfo(msgVO.getPlayerId());
        if(info != null) {
            Map result = GameUtil.createSimpleMap();
            result.put(CHAT_PLAYER_ID, info.getPid());
            result.put(CHAT_PLAYER_NAME, info.getName());
            result.put(CHAT_PLAYER_LEVEL, info.getPlv());
            if (info.getGuildId() > 0) {
                GuildVO guildVO = CacheGuild.getGuildByID(info.getGuildId());
                if(guildVO != null) {
                    result.put(CHAT_GUILD_NAME, info.getGuildName());
                    result.put(CHAT_GUILD_LEVEL, guildVO.getLevel());
                    Integer right = guildVO.getMemberRights().get(info.getPid());
                    result.put(CHAT_GUILD_RIGHT, right == null ? 0 : right.intValue());
                }
            }
            result.put(CHAT_MSG, msgVO.getMsg());
        }
		return null;
	}
}
