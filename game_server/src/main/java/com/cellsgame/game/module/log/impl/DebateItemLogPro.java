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
public class DebateItemLogPro extends LogProcess {
	
	
	public static String CID = "cid";
	
	public static String NUM = "num";

	/**
	 * @see com.cellsgame.game.module.log.LogProcess#builderInfo(com.cellsgame.game.core.event.EvtHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	protected Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(CID, e.getParam(EvtParamType.CID));
		result.put(NUM, e.getParam(EvtParamType.NUM));
		return result;
	}

}
