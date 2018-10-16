/**
 * 
 */
package com.cellsgame.game.module.quest.csv.adjust;

import java.util.List;
import java.util.Map;

import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.quest.cache.AQuestCfgCache;
import com.cellsgame.game.module.quest.cache.QuestCidCache;
import com.cellsgame.game.module.quest.csv.AQuestCfg;
import com.cellsgame.game.module.quest.csv.CditCfg;

/**
 * @author peterveron
 *
 */
public abstract class QuestAdjuster<T extends AQuestCfg> implements CfgAdjuster<T> {

	/**
	 * @see com.cellsgame.game.core.cfg.core.proc.CfgAdjuster#needAdd2Cache()
	 */
	@Override
	public boolean needAdd2Cache() {
		return false;
	}


	/**
	 * @see com.cellsgame.game.core.cfg.core.proc.CfgAdjuster#doAdjustData(com.cellsgame.game.core.cfg.core.ConfigCache, java.util.Map)
	 */
	@Override
	public void doAdjustData(ConfigCache allCfg, Map<Integer, T> map) {
		map.values().forEach(v -> {
    		QuestCidCache.loopTypeCidMapping.put(v.getLoop(), v.getId());
    		QuestCidCache.questTypeCidMapping.put(v.getType(), v.getId());
    		
    		List<CditCfg> acceptCditLst = v.getAcceptCditLst();
    	
    		for (CditCfg cditCfg : acceptCditLst) {
    			cditCfg.getType().getAcceptableQuestCfgCache().add(cditCfg, v);
    		}
    		
    		AQuestCfgCache.addQuestCfg(v);
    	});
	}

}
