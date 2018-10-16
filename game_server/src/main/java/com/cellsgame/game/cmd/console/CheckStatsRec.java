/**
 * 
 */
package com.cellsgame.game.cmd.console;

import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.StringUtil;
import com.cellsgame.common.util.cmd.console.AConsoleCmd;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.mail.MailFactory;
import com.cellsgame.game.module.mail.MailType;
import com.cellsgame.game.module.mail.cons.MailConstant;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author peterveron
 *
 */
public class CheckStatsRec extends AConsoleCmd {

	/**
	 * @see com.cellsgame.common.util.cmd.Command#getName()
	 */
	@Override
	public Object getName() {
		return "ckStatsRec";
	}

	/**
	 * @see com.cellsgame.common.util.cmd.console.AConsoleCmd#exe(java.lang.String, java.lang.String[])
	 */
	@Override
	public Object exe(String cmd, String[] param) throws Exception {
		  Dispatch.dispatchGameLogic(() -> {
	            String pname = param[1];
	            Integer playerId = CachePlayerBase.getPIDByPname(pname);
	            PlayerVO pvo = CachePlayer.getPlayerByPid(playerId);
	            if(pvo == null){
	            	PlayerBO playerBO = SpringBeanFactory.getBean("playerBO");
	            	playerBO.loadPlayerAndExecBiz(CMD.system, pvo, playerId.intValue(), this::checkStats);
	            	
	            }else{
	            	checkStats(CMD.system, GameUtil.createSimpleMap(), pvo, pvo);
	            }
	        });
	        return null;
	}
	
	public Map checkStats(CMD cmd, Map parent, PlayerVO srcPvo, PlayerVO tgtPvo){
		if(tgtPvo!=null)
			System.out.println(tgtPvo.getStatsVO().getData());
		return parent;
	}

}
