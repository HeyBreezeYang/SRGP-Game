/**
 * 
 */
package com.cellsgame.game.module;

import java.util.Map;

import com.cellsgame.game.cons.SYSCons;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * @author peterveron
 *
 */
public interface DailyResetable {

	
	
	public void dailyReset(CMD cmd, Map parent, PlayerVO pvo, long ms);
	
	public boolean dailyResetable(PlayerVO pvo);

	public long lastDailyResetTime(PlayerVO pvo);

	
	
	default public void reset(CMD cmd, Map parent, PlayerVO pvo){
		if(!dailyResetable(pvo))
			return;
		long ms = System.currentTimeMillis();
		if(validateResetTime(ms, pvo)){
			dailyReset(cmd, parent, pvo, ms);
		}
	}
	
	default public boolean validateResetTime(long ms, PlayerVO pvo){
		
		return SYSCons.notSameDate(ms, lastDailyResetTime(pvo));
	}




}
