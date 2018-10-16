package com.cellsgame.game.module.stats.bo;

import java.util.Map;

import com.cellsgame.game.core.event.EvtParam;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.stats.cons.EvtTypeStats;

/**
 * @author peterveron
 *
 */
@SuppressWarnings("rawtypes")
public interface StatsBO extends IBuildData, StaticEvtListener{

	/**
	 * @param cmd
	 * @param parent
	 * @param pvo
	 * @param statsType
	 * @param change
	 * @param params
	 * @return
	 */
	
	Map add(CMD cmd, Map parent, PlayerVO pvo, EvtTypeStats statsType, Long change, EvtParam ... params);

	/**
	 * @param cmd
	 * @param parent
	 * @param pvo
	 * @param statsType
	 * @param typeParam
	 * @param change
	 * @param params
	 * @return
	 */
	Map add(CMD cmd, Map parent, PlayerVO pvo, EvtTypeStats statsType, Integer typeParam, Long change,
			EvtParam ... params);
	
	
	Map update(CMD cmd, Map parent, PlayerVO pvo, EvtTypeStats statsType, Long update, EvtParam ... params);
	
	
	Map update(CMD cmd, Map parent, PlayerVO pvo, EvtTypeStats statsType,  Integer typeParam,  Long update, EvtParam ... params); 
	
	
	
}
