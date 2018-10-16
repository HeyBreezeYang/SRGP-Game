package com.cellsgame.game.module.rank.bo.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.depot.cons.EventTypeDepot;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.guild.cons.EvtTypeGuild;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.rank.bo.RankBO;
import com.cellsgame.game.module.rank.cache.CacheRank;
import com.cellsgame.game.module.rank.cache.RankBoard;
import com.cellsgame.game.module.rank.cons.CacheDisRank;
import com.cellsgame.game.module.rank.cons.CodeRank;
import com.cellsgame.game.module.rank.cons.RankType;
import com.cellsgame.game.module.rank.evt.EvtTypeRank;
import com.cellsgame.game.module.rank.msg.MsgFactoryRank;
import com.cellsgame.game.module.rank.vo.RankLikeRecVO;
import com.cellsgame.game.module.rank.vo.RankVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;

/**
 * @author Aly on 2017-03-14.
 */
public class RankBOImpl implements RankBO{
    @Resource
    private BaseDAO<RankVO> rankDAO;
    
    @Resource
    private BaseDAO<RankLikeRecVO> rankLikeRecDAO;
   
    
    private MsgFactoryRank msgFactory = MsgFactoryRank.instance();

    @Override
    public void init() {
    	RankType[] types = RankType.values();
    	for (RankType rankType : types) {
			CacheRank.createBoard(rankType.getType(), rankType.getViewFunction());
		}
        List<DBObj> all = rankDAO.getAll();
        if (all != null && all.size() > 0) {
            for (DBObj dbObj : all) {
                RankVO vo = new RankVO();
                vo.readFromDBObj(dbObj);
                RankBoard<RankVO,Integer> board = CacheRank.getBoard(vo.getType());
                board.update(vo, CacheRank.getAdd2Cache());
            }
        }
    }
    
    @Override
    public void onShutDown() {
        // 关闭的时候保存排行榜数据
        rankDAO.save(CacheRank.getCacheRank());
    }

    
    @Override
    public Map<String, Object> getRank(PlayerVO pvo, CMD cmd, int type, int start, int end) throws LogicException {
    	RankBoard<RankVO, Integer> board = CacheRank.getBoard(type);
    	RankType rankType = Enums.get(RankType.class, type);
    	RankVO rvo = null;
    	switch (rankType) {
		case GUILD:
			rvo = CacheRank.getRankVO(pvo.getGuildID(), type);
			break;
		default:
			rvo = CacheRank.getRankVO(pvo.getId(), type);
			break;
		}
    	int selfRank = -1;
    	if(rvo!=null)
    		selfRank = board.getRank(rvo);
        return MsgUtil.brmAll(msgFactory.getRankMsg(GameUtil.createSimpleMap(), board, start, end, selfRank), cmd.getCmd());
    }

    @Override
    public Map<String, Object> addLike(PlayerVO pvo, CMD cmd, int likeType) throws LogicException {
    	
//        RankLikeRecVO rec = pvo.getRankLikeRec();
		RankLikeRecVO rec=null;
        CodeRank.NOT_LIKE_ADD_RANK_TYPE.throwIfTrue(!inAddLikeType(likeType));
        CodeRank.LIKE_ADD_NUM_MAX.throwIfTrue(rec.getLikeCount(likeType) >= 1);
        rec.addLike(likeType);
        rankLikeRecDAO.save(rec);
        int[] treData = CacheDisRank.MAX_ADD_LIKE_PRIZE_TRE.getData();
        int tre = treData[GameUtil.r.nextInt(treData.length)];
       
        Map<String, Object> ret = GameUtil.createSimpleMap();
        
        Map prizeMap = GameUtil.createSimpleMap();
        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
     
        executor.addSyncFunc(new FuncConfig(SyncFuncType.ChangeCur.getType(), CurrencyType.TRE.getType(), tre));
        executor.exec(ret, prizeMap, pvo);
    	msgFactory.getPrizeMsg(ret, prizeMap);
       
        //消息处理
        msgFactory.getRankLikeUpdateMsg(ret,rec);
        EvtTypeRank.ADD_LIKE.happen(ret, cmd, pvo);
        return MsgUtil.brmAll(ret, cmd.getCmd());
    }
  
    @Override
    public List<Integer> getRankAround(int type, int key, int range){
    	RankBoard<RankVO,Integer> board = CacheRank.getBoard(type);
    	List<Integer> keys = board.getKeysAround(key, range);
    	return keys;	
    }

    /**
	 * @param likeType
	 * @return
	 */
	private boolean inAddLikeType(int likeType) {
		int[] types = CacheDisRank.ADD_LIKE_RANK_TYPE.getData();
		for (int type : types) {
			if(likeType == type)
				return true;
		}
		return false;
	}

    
    @Override
    public EvtType[] getListenTypes() {
        return new EvtType[]{ 
        		EvtTypePlayer.FightForce,  
        		EvtTypePlayer.Worship, 
        		EventTypeDepot.Currency,
        		EvtTypeGuild.Donate
        };
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        Enum<?> type = event.getType();
        if (type instanceof EvtTypePlayer) {
        	PlayerVO pvo = (PlayerVO) holder;
            EvtTypePlayer eType = (EvtTypePlayer) type;
            switch (eType) {
                case FightForce: {
                    updateRankVal(pvo.getId(), RankType.FIGHT_FORCE.getType(), event.getParam(EvtParamType.AFTER));
                    break;
                }
                case Worship: {
                	Integer pid = event.getParam(EvtParamType.PID);
                	incrRankVal(pid,  RankType.POPULAR.getType(), 1L);
                	break;
                }
                default:
				break;
            }
        }else if(type instanceof EventTypeDepot){//未变化前未处理
        	PlayerVO pvo = (PlayerVO) holder;
        	EventTypeDepot eType = (EventTypeDepot) type;
        	long after = 0;
	    	switch (eType) {
	        	case Currency: {
	        		
	        		break;
	        	}
	        	default:
	        		break;
	    	}
        }else if(type instanceof EvtTypeGuild){
        	EvtTypeGuild eType = (EvtTypeGuild) type;
	    	switch (eType) {
	        	case Donate: {
	        		Long rankVal = event.getParam(EvtParamType.AFTER);
	        		int gid = event.getParam(EvtParamType.GUILD_ID);
	        		updateRankVal(gid, RankType.GUILD.getType(), rankVal);
	        		break;
	        	}
	        	default:
	        		break;
	    	}
        }

        return parent;
    }

	public void updateRankVal(Integer key, int type, Long val) {
		RankBoard<RankVO, Integer> board = CacheRank.getBoard(type);
		RankVO rvo = CacheRank.getRankVO(key,type);
		if(rvo==null)
			return;
		rvo.setValue(val);
		board.update(rvo);
		rankDAO.save(rvo);
	}
	
	public void incrRankVal(Integer key, int type, Long val) {
		RankBoard<RankVO, Integer> board = CacheRank.getBoard(type);
		RankVO rvo = CacheRank.getRankVO(key,type);
		if(rvo == null)
			return;
		rvo.setValue(val+rvo.getValue());
		board.update(rvo);
		rankDAO.save(rvo);
	}
    
	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsLoad(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO, java.util.Map)
	 */
	@Override
	public void buildAsLoad(CMD cmd, PlayerVO pvo, Map<String, ?> data) throws LogicException {
		List<DBObj> objs = (List<DBObj>) data.get(DATA_SIGN_RANK_LIKE_REC);
		RankLikeRecVO recvo = new RankLikeRecVO();
		if(objs!=null&&objs.size()>0){
			DBObj dbObj = objs.get(0);
			recvo.readFromDBObj(dbObj);
		}
		recvo.setPid(pvo.getId());
//		pvo.setRankLikeRec(recvo);
		rankLikeRecDAO.save(recvo);
		build(pvo);
	}

	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
		RankLikeRecVO recvo = new RankLikeRecVO();
		recvo.setPid(pvo.getId());
//		pvo.setRankLikeRec(recvo);
		rankLikeRecDAO.save(recvo);
		build(pvo);
	}
	
	public void build(PlayerVO pvo) {
		RankType[] types = RankType.values();
		List<RankVO> saves = GameUtil.createList();
		for (RankType rtype : types) {
			if(rtype == RankType.GUILD)//公会的不由pvo读取
				continue;
			int type = rtype.getType();
			RankVO rvo = CacheRank.getRankVO(pvo.getId(), type);
			if(rvo!=null)
				continue;
			RankBoard<RankVO, Integer> board = CacheRank.getBoard(type);
			rvo = new RankVO();
			rvo.setKeyId(pvo.getId());
			rvo.setType(type);
			long val = 0;
			switch(rtype){
				case PARTY_SOCRE:
					break;
			default:
				break;
			}
			rvo.setValue(val);
			board.update(rvo, CacheRank.getAdd2Cache());
			saves.add(rvo);
		}
		rankDAO.save(saves);
	}
	
	@Override
	public void build(GuildVO gvo) {
		RankType[] types = RankType.values();
		List<RankVO> saves = GameUtil.createList();
		for (RankType rtype : types) {
			switch(rtype){
			case GUILD://公会的不由pvo读取
				int type = rtype.getType();
				RankVO rvo = CacheRank.getRankVO(gvo.getId(), type);
				if(rvo==null){
					RankBoard<RankVO, Integer> board = CacheRank.getBoard(type);
					rvo = new RankVO();
					rvo.setKeyId(gvo.getId());
					rvo.setType(type);
					rvo.setValue(gvo.getRankVal());
					board.update(rvo, CacheRank.getAdd2Cache());
					saves.add(rvo);
				}
				break;
			default:
				break;
			}
		}
		rankDAO.save(saves);
	}

	/**
	 * @see com.cellsgame.game.module.DailyResetable#dailyReset(com.cellsgame.game.core.message.CMD, java.util.Map, com.cellsgame.game.module.player.vo.PlayerVO, long)
	 */
	@Override
	public void dailyReset(CMD cmd, Map parent, PlayerVO pvo, long ms) {
//		RankLikeRecVO recvo = pvo.getRankLikeRec();
		RankLikeRecVO recvo=null;
		recvo.getTypeLikeCount().clear();
		recvo.setLastUpdateTime(ms);
        rankLikeRecDAO.save(recvo);
        msgFactory.getRankLikeUpdateMsg(parent, recvo);
	}
	
	/**
	 * @see com.cellsgame.game.module.DailyResetable#lastDailyResetTime(com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public long lastDailyResetTime(PlayerVO pvo) {
//		return pvo.getRankLikeRec().getLastUpdateTime();
		return 0;
	}

	/**
	 * @see com.cellsgame.game.module.DailyResetable#dailyResetable(com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public boolean dailyResetable(PlayerVO pvo) {
//		return pvo.getRankLikeRec()!=null;
		return true;
	}

}
