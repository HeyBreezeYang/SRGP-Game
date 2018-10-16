package com.cellsgame.game.module.mail.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

/**
 * 邮件
 */
public enum CodeMail implements ICode {

    Mail_NotFind(1),                         // 没有找到邮件
    Mail_EmptyAccessory(2),                  // 空附件
    Mail_NotFindPlayer(3),                   // 没有找到玩家
    Mail_CanNotDeleteMail(4),                // 不能删除邮件
    Mail_NotFindAccType(5),                  // 没有该附件类型
    Mail_ParamsError(6),                     // 参数错误
    Mail_FuncTypeError(7),                   // 功能类型错误
    Mail_FuncParamsError(8),                 //功能参数错误
    Mail_InLimit(9)	,						 //保护期中
	Mail_AlreadyPick(10)							 //已经领取

	;
	
	private int code;
	
	CodeMail(int code){
		this.code = code;
	}

	@Override
	public int getModule() {
		return ModuleID.Mail;
	}

	@Override
	public int getCode() {
		return code;
	}

}
