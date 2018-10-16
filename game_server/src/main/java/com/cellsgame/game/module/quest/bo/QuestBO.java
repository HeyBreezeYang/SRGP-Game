/**
 * 
 */
package com.cellsgame.game.module.quest.bo;

import java.util.Map;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.DailyResetable;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author peterveron
 *
 */
@AModule(ModuleID.Quest)
public interface QuestBO extends IBuildData, DailyResetable, StaticEvtListener{

	
    @Client(Command.Quest_commit)
	public Map commit(CMD cmd, PlayerVO pvo,  @CParam("cid") int qCid) throws LogicException;

}
