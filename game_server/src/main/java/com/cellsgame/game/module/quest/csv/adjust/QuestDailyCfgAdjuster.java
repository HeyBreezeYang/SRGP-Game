/**
 * 
 */
package com.cellsgame.game.module.quest.csv.adjust;

import com.cellsgame.game.module.quest.csv.QuestDailyCfg;

/**
 * @author peterveron
 *
 */
public class QuestDailyCfgAdjuster extends QuestAdjuster<QuestDailyCfg> {

	/**
	 * @see com.cellsgame.game.core.cfg.core.proc.CfgAdjuster#getProType()
	 */
	@Override
	public Class<QuestDailyCfg> getProType() {
		return QuestDailyCfg.class;
	}

}
