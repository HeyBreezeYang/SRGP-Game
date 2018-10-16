package com.cellsgame.game.module.chat.cons;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.msg.CodeGuild;
import com.cellsgame.game.module.guild.vo.GuildMemberVO;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

public enum ChatType {


    /**
     * CMD
     */
    CMD(0, 0, "SYS_C") {
        @Override
        public Collection<PlayerVO> getBroadcastTarget(PlayerVO pvo)
                throws LogicException {
            return null;
        }

        @Override
        public void cacheMsg(Map msg) throws LogicException {

        }
    },

    /**
     * 公会
     */
    Guild(2, 5000, "公会") {
        @Override
        public Collection<PlayerVO> getBroadcastTarget(PlayerVO pvo) throws LogicException {
            GuildMemberVO memberVO = CacheGuild.getGuildMemberVO(pvo.getId());
            CodeGuild.NotJoinGuild.throwIfTrue(null == memberVO);
            GuildVO guildVO = CacheGuild.getGuildByID(memberVO.getGuildID());
            CodeGuild.NotJoinGuild.throwIfTrue(null == guildVO);
            List<PlayerVO> players = GameUtil.createList();
            Map<Integer, Integer> rightCopy = new HashMap<>();
            rightCopy.putAll(guildVO.getMemberRights());
            for (Map.Entry<Integer, Integer> entry : rightCopy.entrySet()) {
                Integer pid = entry.getKey();
                PlayerVO p = CachePlayer.getPlayerByPid(pid);
                if (p != null && p.isOnline())
                    players.add(p);
            }
            return players;
        }

        @Override
        public void cacheMsg(Map msg) throws LogicException {
            this.getCacheMsg().add(msg);
            if(this.getCacheMsg().size() >= 50){
                this.getCacheMsg().remove(0);
            }
        }
    },
    /**
     * 世界
     */
    World(3, 30000, "世界") {
        @Override
        public Collection<PlayerVO> getBroadcastTarget(PlayerVO pvo) throws LogicException {
            return CachePlayer.getOnlinePlayers();
        }

        @Override
        public void cacheMsg(Map msg) throws LogicException {
            this.getCacheMsg().add(msg);
            if(this.getCacheMsg().size() >= 100){
                this.getCacheMsg().remove(0);
            }
        }
    },
    /**
     * 世界事件
     */
    WorldEvt(4, 0, "世界事件") {
        @Override
        public Collection<PlayerVO> getBroadcastTarget(PlayerVO pvo) throws LogicException {
            return CachePlayer.getOnlinePlayers();
        }

        @Override
        public void cacheMsg(Map msg) throws LogicException {

        }
    },
    /**
     * 系统
     */
    Sys(99, 0, "系统") {
        @Override
        public Collection<PlayerVO> getBroadcastTarget(PlayerVO pvo) throws LogicException {
            return CachePlayer.getOnlinePlayers();
        }

        @Override
        public void cacheMsg(Map msg) throws LogicException {

        }
    },;

    private int value;
    
    private int chatCd;

    private String name;

    private List<Map> cacheMsg = GameUtil.createList();

    ChatType(int val, int chatCd, String name) {
        this.value = val;
        this.chatCd = chatCd;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public int getChatCd() {
		return chatCd;
	}

    public String getName() {
        return name;
    }

    public abstract Collection<PlayerVO> getBroadcastTarget(PlayerVO pvo) throws LogicException;

    public abstract void cacheMsg(Map msg) throws LogicException;

    public boolean checkChatCd(long lastChatTime){
    	long currTime = System.currentTimeMillis();
        return lastChatTime + chatCd <= currTime;
    }

    public List<Map> getCacheMsg() {
        return cacheMsg;
    }
}
