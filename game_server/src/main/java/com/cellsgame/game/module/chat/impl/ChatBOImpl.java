package com.cellsgame.game.module.chat.impl;

import java.util.*;
import javax.annotation.Resource;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.Bootstrap;
import com.cellsgame.game.cache.CacheWord;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.context.GameConfig;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.chat.ChatBO;
import com.cellsgame.game.module.chat.cache.CacheChat;
import com.cellsgame.game.module.chat.cache.CacheChatInfo;
import com.cellsgame.game.module.chat.cache.CacheDisDataChat;
import com.cellsgame.game.module.chat.cons.ChatType;
import com.cellsgame.game.module.chat.msg.CodeChat;
import com.cellsgame.game.module.chat.msg.MsgFactoryChat;
import com.cellsgame.game.module.chat.thread.ChatData;
import com.cellsgame.game.module.chat.thread.TaskManager;
import com.cellsgame.game.module.chat.vo.ChatMsgVO;
import com.cellsgame.game.module.chat.vo.ChatVO;
import com.cellsgame.game.module.friend.cons.CodeFriend;
import com.cellsgame.game.module.friend.vo.FriendListVO;
import com.cellsgame.game.module.player.cons.CacheDisDataPlayer;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.cons.PlayerState;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author Aly on  2016-09-12.
 */
public class ChatBOImpl implements ChatBO, StaticEvtListener {

    @Resource
    private BaseDAO<FriendListVO> friendListDAO;

    @Resource
    private BaseDAO<ChatVO> chatDAO;

    @Override
    public Map chat(PlayerVO pvo, CMD cmd, int type, String msg) throws LogicException {
        if(!PlayerState.BAN_CHAT.check(pvo)) {
            if (!StringUtils.isEmpty(msg)) {
                if (msg.startsWith(CacheChat.cmdPre) && !GameConfig.getConfig().isRelease()) {
                    Map<String, Object> result = CacheChat.exeCmd(pvo, cmd, msg.substring(CacheChat.cmdPre.length() + 1));
                    return MsgUtil.brmAll(result, Command.CHAT_CHAT);
                } else {
                    msg = CacheWord.replaceDirtyWords(msg);
                    ChatType cType = Enums.get(ChatType.class, type);
                    CodeChat.Chat_TypeError.throwIfTrue(cType == null);
//                    CodeChat.Chat_PlayerLevelMinus.throwIfTrue(pvo.getLevel() < CacheDisDataChat.PlayerLevelReq.first());
                    Long lastChatTime = pvo.getLastChatTime().get(cType);
                    if (lastChatTime != null) {
                        CodeChat.Chat_CD.throwIfTrue(cType.checkChatCd(lastChatTime));
                    }
                    Map<String, Object> parent = MsgFactoryChat.instance().getChatMsg(GameUtil.createSimpleMap(), pvo, type, msg);
                    cType.cacheMsg(parent);
                    Collection<PlayerVO> target = cType.getBroadcastTarget(pvo);
                    if (!CollectionUtils.isEmpty(target)) {
                        Map<String, Object> result = MsgUtil.brmAll(parent, Command.CHAT_MSG);
                        broadcastMsg(result, pvo, false, target);
                    }
                    ChatData data = new ChatData();
                    data.setServerId(String.valueOf(GameConfig.getConfig().getGameServerId()));
                    data.setUserId(pvo.getAccountId());
                    data.setPlayerId(pvo.getId());
                    data.setPlayerName(pvo.getName());
//                    data.setVipLevel(pvo.getVip());
//                    data.setLevel(pvo.getLevel());
                    data.setMsg(msg);
                    data.setCh(cType.getName());
                    TaskManager.commit(data);
                }
            }
        }
        return MsgUtil.brmAll(MsgFactoryChat.instance().getChatMsg(GameUtil.createSimpleMap(), pvo, type, msg), Command.CHAT_CHAT);
    }

    @Override
    public Map getCacheChat(PlayerVO pvo, CMD cmd, int type) throws LogicException {
        ChatType cType = Enums.get(ChatType.class, type);
        CodeChat.Chat_TypeError.throwIfTrue(cType == null);
        return MsgUtil.brmAll(cType.getCacheMsg(), cmd.getCmd());
    }


    @Override
    public Map privateMsg(PlayerVO pvo, int targetPlayerId, String msg)
            throws LogicException {
        CodeChat.Chat_Ban.throwIfTrue(PlayerState.BAN_CHAT.check(pvo));
        PlayerVO tgtPlayer = CachePlayer.getPlayerByPid(targetPlayerId);
        msg = CacheWord.replaceDirtyWords(msg);
        if (tgtPlayer != null) {
            FriendListVO tgtFlist = tgtPlayer.getFriendList();
            // 直接提示 在黑名单
            CodeFriend.IN_MY_BLACK_LIST.throwIfTrue(null != tgtFlist && tgtFlist.getBlackList().contains(pvo.getId()));
            addNearChat(tgtPlayer, pvo.getId());
            if (tgtPlayer.isOnline()) {
                // 推送消息
                Map<String, Object> parent = MsgFactoryChat.instance().getChatMsg(GameUtil.createSimpleMap(), pvo, msg);
                Map<String, Object> result = MsgUtil.brmAll(parent, Command.CHAT_REV_PRIVATE_MSG);
                Push.multiThreadSend(tgtPlayer.getMessageController(), result);
            } else {
                cacheChatMsg(pvo, targetPlayerId, msg);
            }
        } else {
            cacheChatMsg(pvo, targetPlayerId, msg);
        }
        addNearChat(pvo, targetPlayerId);
        return MsgUtil.brmAll(MsgFactoryChat.instance().getChatMsg(GameUtil.createSimpleMap(), pvo, targetPlayerId, msg), Command.CHAT_PRIVATE_MSG);
    }

    private void cacheChatMsg(PlayerVO pvo, int targetPlayerId, String msg) {
        //如果玩家不在线，则缓存私聊信息
        ChatVO info = CacheChatInfo.getChatInfoByPid(targetPlayerId);
        if (info == null) {
            info = new ChatVO();
            info.setPid(targetPlayerId);
            CacheChatInfo.cacheChatInfo(info);
        }
        List<ChatMsgVO> msgs = info.getPrivateMsg();
        msgs.add(createChatMsgVO(pvo, msg));
        chatDAO.save(info);
    }

    private ChatMsgVO createChatMsgVO(PlayerVO pvo, String msg) {
        ChatMsgVO msgVO = new ChatMsgVO();
        msgVO.setPlayerId(pvo.getId());
        msgVO.setMsg(msg);
        return msgVO;
    }

    @Override
    public Map getAllPrivateMsg(PlayerVO pvo) {
        ChatVO info = CacheChatInfo.getChatInfoByPid(pvo.getId());
        Map result = GameUtil.createSimpleMap();
        if (info != null && !info.getPrivateMsg().isEmpty()) {
            result = MsgFactoryChat.instance().createOfflineChatMsg(result, info.getPrivateMsg());
            info.getPrivateMsg().clear();
            chatDAO.save(info);
        }
        return MsgUtil.brmAll(result, Command.CHAT_GET_ALL_PRIVATE_MSG);
    }

    @Override
    public void notifyMsg(PlayerVO pvo, ChatType cType, int msgType, String[] msgParams) {
        Collection<PlayerVO> target = cType.getBroadcastTarget(pvo);
        if (!CollectionUtils.isEmpty(target)) {
            Map<String, Object> parent = MsgFactoryChat.instance().getNotifyMsg(GameUtil.createSimpleMap(), msgType, msgParams);
            Map<String, Object> result = MsgUtil.brmAll(parent, Command.CHAT_NOTIFY_MSG);
            broadcastMsg(result, target);
        }
    }

    private void addNearChat(PlayerVO pvo, int tgtPid) {
        FriendListVO friendList = pvo.getFriendList();
        if (null == friendList) {
            friendList = new FriendListVO();
            friendList.setPid(pvo.getId());
            pvo.setFriendList(friendList);
        }
        List<Integer> nearCharPlayer = friendList.getNearCharPlayer();
        nearCharPlayer.remove(tgtPid);
        nearCharPlayer.add(0, tgtPid);
        int first = CacheDisDataPlayer.NearChatFriendLimit.first();
        if (nearCharPlayer.size() > first) {
            nearCharPlayer.remove(nearCharPlayer.size() - 1);
        }
        friendListDAO.save(friendList);
    }

    private void broadcastMsg(Map<String, Object> message, PlayerVO self, boolean toSelf, Collection<PlayerVO> players) {
        for (PlayerVO target : players) {
            FriendListVO friendListVO = target.getFriendList();
            if (friendListVO != null && friendListVO.getBlackList().contains(self.getId()))
                continue;
            if (target != self || toSelf) {
                Map<String, Object> copyMsg = GameUtil.createSimpleMap();
                copyMsg.putAll(message);
                Push.multiThreadSend(target.getMessageController(), copyMsg);
            }
        }
    }

    private void broadcastMsg(Map<String, Object> message, Collection<PlayerVO> players) {
        for (PlayerVO target : players) {
            Map<String, Object> copyMsg = GameUtil.createSimpleMap();
            copyMsg.putAll(message);
            Push.multiThreadSend(target.getMessageController(), copyMsg);
        }
    }

    @Override
    public void init() {
        List<DBObj> all = chatDAO.getAll();
        if (all != null) {
            for (DBObj dbObj : all) {
                ChatVO vo = new ChatVO();
                vo.readFromDBObj(dbObj);
                CacheChatInfo.cacheChatInfo(vo);
            }
        }
    }

    @Override
    public EvtType[] getListenTypes() {
        return new EvtType[]{EvtTypePlayer.EnterGame};
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        if (holder instanceof PlayerVO) {
            PlayerVO player = (PlayerVO) holder;
            ChatVO info = CacheChatInfo.getChatInfoByPid(player.getId());
            if (info == null) return parent;
            List<ChatMsgVO> privateMsg = info.getPrivateMsg();
            for (int i = privateMsg.size() - 1; i >= 0; i--) {
                ChatMsgVO vo = privateMsg.get(i);
                addNearChat(player, vo.getPlayerId());
            }
        }
        return parent;
    }
}
