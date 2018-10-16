package com.cellsgame.game.module.rank.cache;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.module.rank.cons.RankType;
import com.cellsgame.game.module.rank.msg.MsgFactoryRank;
import com.cellsgame.game.module.rank.vo.RankVO;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author Aly on 2017-03-28.
 */
public class CacheRank {
    
	private static final Table<Integer, Integer, RankVO> cachePlayerRank = HashBasedTable.create();
	private static final Table<Integer, Integer, RankVO> cacheGuildRank = HashBasedTable.create();
    
    private static Consumer<RankVO> add2Cache = CacheRank::cacheRankVO;
    
    
    private static Map<Integer, RankBoard<RankVO,Integer>> boards = GameUtil.createSimpleMap();
    
    /**
     * 默认排序
     */
    public static <K> Comparator<RankObj<K>> DEFAULT_Comp() {
    	return Comparator.comparingLong(RankObj<K>::getSort1).reversed().thenComparingLong(RankObj::getSort2);
    }
    

    public static RankBoard<RankVO,Integer> getBoard(int type){
    	RankBoard<RankVO,Integer> board = boards.get(type);
    	return board;
    }
    
    public static void createBoard(int type, BiFunction<MsgFactoryRank, List<RankObj<Integer>>, List<Map<String, Object>>> viewFunction){
    	RankBoard<RankVO,Integer> board = RankBoard.of(
        		type, 
        		RankObjUtil.of(RankVO::getKeyId, 
        					   RankVO::getValue, 
        					   RankVO::setIx,
        					   DEFAULT_Comp()), 
        		viewFunction);
    	boards.put(type, board);
    }
    
    
    
    
    private static Table<Integer, Integer, RankVO> getCache(int RankType){
    	RankType t = Enums.get(RankType.class, RankType);
    	switch (t) {
	    	case GUILD:{
	    		return cacheGuildRank;
	    	}				
	    	case PARTY_SOCRE:
	    	case BEAUTY_INTIMACY:
	    	case BUSINESS:
	    	case DEBATE_SCORE:
	    	case FIGHT_FORCE:
	    	case PLUNDER_SCORE:
			case TRADE_SCORE:
	    	case STORY_PROC:
	    	case WORLD_BOSS:{
	    		return cachePlayerRank;
	    	}
    	}
    	return null;
    }
    
    
    
    public static RankVO getRankVO(int key, int type) {
    	Table<Integer, Integer, RankVO> cache = getCache(type);
    	if(cache == null)
    		return null;
    	return cache.get(key, type);
    	
    }

    public static RankVO removRankVO(int key, int type) {
    	Table<Integer, Integer, RankVO> cache = getCache(type);
    	if(cache == null)
    		return null;
    	return cache.remove(key, type);
    }

    private static void cacheRankVO(RankVO vo) {
    	Table<Integer, Integer, RankVO> cache = getCache(vo.getType());
    	if(cache == null)
    		return;
    	cache.put(vo.getKeyId(), vo.getType(), vo);
    }
    
  
    public static Consumer<RankVO> getAdd2Cache() {
        return add2Cache;
    }

    public static Collection<RankVO> getCacheRank(int type) {
    	Table<Integer, Integer, RankVO> cache = getCache(type);
    	if(cache == null)
    		return null;
    	return cache.values();
    }
    
    public static Collection<RankVO> getCacheRank() {
    	Collection<RankVO> ret = GameUtil.createList();
    	ret.addAll(cachePlayerRank.values());
    	ret.addAll(cacheGuildRank.values());
    	return ret;
    }
}

