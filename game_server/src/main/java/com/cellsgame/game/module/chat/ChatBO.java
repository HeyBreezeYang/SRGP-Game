package com.cellsgame.game.module.chat;

import java.util.Map;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.chat.cons.ChatType;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author Aly on  2016-09-12.
 */
@AModule(ModuleID.Chat)
public interface ChatBO {
	
	void init();
	
    @Client(Command.CHAT_CHAT)
    Map chat(PlayerVO pvo, CMD cmd, @CParam("type") int type, @CParam("msg") String msg) throws LogicException;
    

    @Client(Command.CHAT_PRIVATE_MSG)
    Map privateMsg(PlayerVO pvo, @CParam("pid") int targetPlayerId, @CParam("msg") String msg) throws LogicException;

    @Client(Command.CHAT_GET_ALL_PRIVATE_MSG)
    Map getAllPrivateMsg(PlayerVO pvo);

    @Client(Command.CHAT_GET_CACHE_CHAT)
    Map getCacheChat(PlayerVO pvo, CMD cmd, @CParam("type") int type) throws LogicException;
    

    void notifyMsg(PlayerVO pvo, ChatType cType, int msgType, String[] msgParams);
}
