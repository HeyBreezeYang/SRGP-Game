package com.cellsgame.game.module.rank.msg;

import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.rank.cache.RankBoard;
import com.cellsgame.game.module.rank.cache.RankObj;
import com.cellsgame.game.module.rank.vo.RankLikeRecVO;
import com.cellsgame.game.module.rank.vo.RankVO;

/**
 * @author Aly on 2017-03-14.
 */
public class MsgFactoryRank extends MsgFactory {
    private static final MsgFactoryRank instance = new MsgFactoryRank();
    
    public static final String START = "st" ;
    public static final String END = "ed";
    public static final String LIKE_REC = "lkrec";

	private static final String SIZE = "size";

    public static MsgFactoryRank instance() {
        return instance;
    }

    @Override
    public String getModulePrefix() {
        return RANK;
    }

    public Map<String, Object> getRankMsg(Map<String,Object> parent, RankBoard<RankVO,Integer> board, int start, int end, int selfIndex) {
    	parent = creIfNull(parent);
    	Map<String, Object> info = gocInfoMap(parent);
    	Map<String, Object> boardInfo = gocMapIn(info, board.getType());
    	boardInfo.put(START, start);
    	boardInfo.put(END, end);
    	boardInfo.put(TYPE,board.getType());
    	boardInfo.put(LIST,board.getRankInfo(start, end));
    	Map<String, Object> selfRankInfo = board.getRankInfo(selfIndex);
    	if(selfRankInfo!=null)
    		   selfRankInfo.put(RANK, selfIndex);
    	boardInfo.put(SELF, selfRankInfo);
    	boardInfo.put(SIZE,board.size());
    	return parent;
    }
    
    public Map<String,Object> getRankLikeInfoMsg(Map<String,Object> parent, RankLikeRecVO recvo){
    	parent = creIfNull(parent);
    	Map<String, Object> info = gocInfoMap(parent);
    	info.put(LIKE_REC, getRankLikeRecVOMsg(recvo));
    	return parent;
    }
    
    
    public Map<String,Object> getRankLikeUpdateMsg(Map<String,Object> parent, RankLikeRecVO recvo){
    	parent = creIfNull(parent);
    	Map<String, Object> info = gocOpMap(parent);
    	info.put(LIKE_REC, getRankLikeRecVOMsg(recvo));
    	return parent;
    }
    
    
    public List<Map<String,Object>> createPlayerRankObjMsg(List<RankObj<Integer>> objs){
    	List<Map<String,Object>> ret = GameUtil.createList();
    	for (RankObj<Integer> obj : objs) {
			Map<String, Object> info = getPlayerRankObjMsg(obj);
			if(info!=null)
				ret.add(info);
		}
    	return ret; 
    }

	public Map<String, Object> getPlayerRankObjMsg(RankObj<Integer> obj) {
		Map<String,Object> info = GameUtil.createSimpleMap();
		Integer pid = obj.getKey();
		PlayerInfoVO pInfo = CachePlayerBase.getBaseInfo(pid);
		if(pInfo == null)
			return null;
		info.put(ID, pid);
		info.put(NAME, CachePlayerBase.getPnameByPid(pid));
		info.put(VALUE, obj.getSort1());
		info.put(LEVEL, pInfo.getPlv());
		info.put(GUILD_NAME,pInfo.getGuildName());
		info.put(IMG, pInfo.getImage());
		info.put(VIP, pInfo.getVip());
		return info;
	}
    
    public List<Map<String,Object>> createGuildRankObjMsg(List<RankObj<Integer>> objs){
    	List<Map<String,Object>> ret = GameUtil.createList();
    	for (RankObj<Integer> obj : objs) {
    		Map<String, Object> info = getGuildRankObjMsg(obj);
    		if(info!=null)
    			ret.add(info);
		}
    	return ret; 
    }

	public Map<String, Object> getGuildRankObjMsg(RankObj<Integer> obj) {
		Integer gid = obj.getKey();
		GuildVO gvo = CacheGuild.getGuildByID(gid);
		if(gvo != null){
			Map<String,Object> info = GameUtil.createSimpleMap();
			info.put(ID, gid);
			info.put(NAME, gvo.getName());
			info.put(VALUE, obj.getSort1());
			info.put(LEVEL,gvo.getLevel());
			info.put(NUM, gvo.getMemberSize());
			info.put(FIGHT_FORCE, gvo.getFightForce());
			PlayerInfoVO pInfo = CachePlayerBase.getBaseInfo(gvo.getOwner());
			if(pInfo!=null)
				info.put(LEADER, pInfo.getName());
			return info;
		}
		return null;
	}
	
	public Map<String,Object> getRankLikeRecVOMsg(RankLikeRecVO recvo){
		Map<String,Object> ret = GameUtil.createSimpleMap();
		ret.put(COUNT, recvo.getTypeLikeCount());
		ret.put(DATE, recvo.getLastUpdateTime());
		return ret;
	}
    
}
