/**
 * 
 */
package com.cellsgame.game.module.log.impl;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;

/**
 * @author peterveron
 *
 */
public class DebateFinLogPro extends LogProcess {
	
	public static final String TYPE = "type";
	public static final String WIN = "win";
	/**
	 * @see com.cellsgame.game.module.log.LogProcess#builderInfo(com.cellsgame.game.core.event.EvtHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	protected Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(TYPE, e.getParam(EvtParamType.TYPE));
		result.put(WIN, e.getParam(EvtParamType.WIN));
		return result;
	}

}
