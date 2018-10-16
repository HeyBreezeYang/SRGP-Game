/**
 * 
 */
package com.cellsgame.game.module.quest.cache;

import com.cellsgame.game.module.quest.cons.LoopType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * @author peterveron
 *
 */
public class QuestCidCache {
	
	public static final Multimap<LoopType, Integer> loopTypeCidMapping = ArrayListMultimap.create();
	
	public static final Multimap<Integer, Integer> questTypeCidMapping = ArrayListMultimap.create();
	
}
