package com.cellsgame.game.module.chat.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

/**
 * @see ModuleID#Chat
 */
public enum CodeChat implements ICode{

    Chat_NotOnline(1),                //角色没在线
    Chat_TypeError(2),                //类型错误
    Chat_CD(3),                       //聊天CD中
	Chat_Ban(4),                      //禁言中
	Chat_PlayerLevelMinus(5),                //等级不足
	;
	
	private int code;
	
	
	CodeChat(int code){
		this.code = code;
	}


    @Override
	public int getModule() {
		return ModuleID.Chat;
	}

	@Override
	public int getCode() {
		return code;
	}

}
