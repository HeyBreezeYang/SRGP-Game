/**
 * 
 */
package com.cellsgame.game.module.quest.csv.adjust;

import com.cellsgame.game.module.quest.csv.QuestAchieveCfg;

/**
 * @author peterveron
 *
 */
public class QuestAchieveCfgAdjuster extends QuestAdjuster<QuestAchieveCfg> {

	/**
	 * @see com.cellsgame.game.core.cfg.core.proc.CfgAdjuster#getProType()
	 */
	@Override
	public Class<QuestAchieveCfg> getProType() {
		return QuestAchieveCfg.class;
	}

}
