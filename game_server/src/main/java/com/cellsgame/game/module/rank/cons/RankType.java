/**
 * 
 */
package com.cellsgame.game.module.rank.cons;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import com.cellsgame.game.module.rank.msg.MsgFactoryRank;
import com.cellsgame.game.module.rank.cache.RankObj;

/**
 * @author peterveron
 *
 */
public enum RankType {
	FIGHT_FORCE(1,MsgFactoryRank::createPlayerRankObjMsg),
	STORY_PROC(2,MsgFactoryRank::createPlayerRankObjMsg),
	BEAUTY_INTIMACY(3,MsgFactoryRank::createPlayerRankObjMsg),
	WORLD_BOSS(4,MsgFactoryRank::createPlayerRankObjMsg),//未出
	PARTY_SOCRE(5,MsgFactoryRank::createPlayerRankObjMsg),
	PLUNDER_SCORE(6,MsgFactoryRank::createPlayerRankObjMsg),
	DEBATE_SCORE(7,MsgFactoryRank::createPlayerRankObjMsg),
	BUSINESS(8,MsgFactoryRank::createPlayerRankObjMsg),//未出
	POPULAR(9,MsgFactoryRank::createPlayerRankObjMsg), 
	GUILD(10,MsgFactoryRank::createGuildRankObjMsg),
	WORLD_BOSS_SCORE(11,MsgFactoryRank::createPlayerRankObjMsg),//未出
	TRADE_SCORE(12,MsgFactoryRank::createPlayerRankObjMsg),
	;
	
	RankType(int type, BiFunction<MsgFactoryRank, List<RankObj<Integer>>,  List<Map<String, Object>>> viewFunction){
		this.type = type;
		this.viewFunction = viewFunction;
	}
	
	private int type;
	
	private BiFunction<MsgFactoryRank, List<RankObj<Integer>>,  List<Map<String, Object>>> viewFunction;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public BiFunction<MsgFactoryRank, List<RankObj<Integer>>,  List<Map<String, Object>>> getViewFunction() {
		return viewFunction;
	}

	public void setViewFunction(BiFunction<MsgFactoryRank, List<RankObj<Integer>>,  List<Map<String, Object>>> viewFunction) {
		this.viewFunction = viewFunction;
	}
}
