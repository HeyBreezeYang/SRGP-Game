package com.cellsgame.game.module.stats.bo.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cellsgame.game.core.event.EventTypeAll;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParam;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.card.cons.CardCons;
import com.cellsgame.game.module.card.evt.EvtTypeCard;
import com.cellsgame.game.module.depot.cons.EventTypeDepot;
import com.cellsgame.game.module.guild.cons.EvtTypeGuild;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.rank.evt.EvtTypeRank;
import com.cellsgame.game.module.shop.csv.ShopType;
import com.cellsgame.game.module.shop.evt.EvtTypeShop;
import com.cellsgame.game.module.stats.bo.StatsBO;
import com.cellsgame.game.module.stats.cons.EvtTypeStats;
import com.cellsgame.game.module.stats.cons.StatsCons;
import com.cellsgame.game.module.stats.vo.StatsVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;

/**
 * @author peterveron
 *
 */
@SuppressWarnings("rawtypes")
public class StatsBOImpl implements StatsBO{

	@Resource
	private BaseDAO<StatsVO> statsDAO;


	public Map add(CMD cmd, Map parent, PlayerVO pvo, EvtTypeStats statsType, Long change, EvtParam ... params) {
		EvtParam[] newParams = dealChange(pvo, statsType.getStatsType(), change, true, params);
		statsType.happen(parent, cmd, pvo, newParams);
		return parent;
	}


	@Override
	public Map add(CMD cmd, Map parent, PlayerVO pvo, EvtTypeStats statsType, Integer typeParam, Long change, EvtParam ... params) {
		EvtParam[] newParams = dealChange(pvo, exchangParameterizedType(statsType.getStatsType(), typeParam), change, true, params);
		statsType.happen(parent, cmd, pvo, newParams);
		return parent;
	}

	@Override
	public Map update(CMD cmd, Map parent, PlayerVO pvo, EvtTypeStats statsType, Long update, EvtParam ... params) {
		EvtParam[] newParams = dealChange(pvo, statsType.getStatsType(), update, false, params);
		statsType.happen(parent, cmd, pvo, newParams);
		return parent;
	}


	@Override
	public Map update(CMD cmd, Map parent, PlayerVO pvo, EvtTypeStats statsType, Integer typeParam, Long update, EvtParam ... params) {
		EvtParam[] newParams = dealChange(pvo, exchangParameterizedType(statsType.getStatsType(), typeParam), update, false, params);
		statsType.happen(parent, cmd, pvo, newParams);
		return parent;
	}


	private EvtParam[] dealChange(PlayerVO pvo, Integer type, Long change, boolean addOrUpd, EvtParam... params) {
		StatsVO svo = pvo.getStatsVO();
		Long before = svo.getData(type);
		if(addOrUpd)
			svo.add(type, change);
		else
			svo.update(type, change);
		statsDAO.save(svo);
		int len = params.length;
		EvtParam[] newParams = new EvtParam[len+3];
		System.arraycopy(params, 0, newParams, 0, len);
		newParams[len++] = EvtParamType.TYPE.val(type);
		newParams[len++] = EvtParamType.BEFORE.val(before);
		newParams[len] = EvtParamType.AFTER.val(svo.getData(type));
		return newParams;
	}


	private int exchangParameterizedType(int type, int param){
		if(type>=StatsCons.COMPLEX_TYPE_START)
			return type+param;
		return type;
	}


	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsLoad(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO, java.util.Map)
	 */
	@Override
	public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
		List<DBObj> dbObjs = (List<DBObj>) data.get(DATA_SIGN_STATS);
		if(dbObjs == null || dbObjs.isEmpty()){
			createStatsVOIfNull(player);
		}else{
			StatsVO svo = new StatsVO();
			svo.readFromDBObj(dbObjs.get(0));
			player.setStatsVO(svo);
		}
	}

	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
		createStatsVOIfNull(pvo);
	}


	public void createStatsVOIfNull(PlayerVO pvo) {
		if(pvo.getStatsVO() != null)
			return;
		StatsVO svo = new StatsVO();
		svo.setPid(pvo.getId());
		statsDAO.save(svo);
		pvo.setStatsVO(svo);
	}


	/**
	 * @see com.cellsgame.game.core.event.StaticEvtListener#getListenTypes()
	 */
	@Override
	public EvtType[] getListenTypes() {
		return new EvtType[]{EventTypeAll.ALL};
	}


	/**
	 * @see com.cellsgame.game.core.event.StaticEvtListener#listen(java.util.Map, com.cellsgame.game.core.message.CMD, com.cellsgame.game.core.event.EvtHolder, com.cellsgame.game.core.event.GameEvent)
	 */
	@Override
	public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
		if(holder instanceof PlayerVO) {
			Enum<?> e = event.getType();
			if (e instanceof EvtTypePlayer) {
				dealPlayerEvt(parent, cmd, holder, event, e);
			} else if (e instanceof EvtTypeRank) {
				dealRankEvt(parent, cmd, holder, e);
			} else if (e instanceof EvtTypeShop) {
				dealShopEvt(parent, cmd, holder, event, e);
			} else if (e instanceof EvtTypeGuild) {
				dealGuildEvt(parent, cmd, holder, event, e);
			} else if (e instanceof EvtTypeCard) {
				dealCardEvt(parent, cmd, holder, event, e);
			} else if (e instanceof EventTypeDepot) {
				dealDepotEvt(parent, cmd, holder, event, e);
			}
		}
		return parent;
	}




	/**
	 * @param parent
	 * @param cmd
	 * @param holder
	 * @param event
	 * @param e
	 */
	private void dealDepotEvt(Map parent, CMD cmd, EvtHolder holder, GameEvent event, Enum<?> e) {
		EventTypeDepot type =  (EventTypeDepot) e;
		PlayerVO pvo = (PlayerVO) holder;
		EvtTypeStats evtTypeStats = null;
		switch(type){
		case Goods:
			int cid = event.getParam(EvtParamType.GOODS_CID);
			long change = event.getParam(EvtParamType.CHANGE);
			evtTypeStats = change<0?EvtTypeStats.SPEC_ID_GOODS_USE:EvtTypeStats.SPEC_ID_GOODS_COLLC;
			add(cmd, parent, pvo, evtTypeStats, cid, Math.abs(change));
			break;
		default:
			break;
		}

	}


	public void dealCardEvt(Map parent, CMD cmd, EvtHolder holder, GameEvent event, Enum<?> e) {
		EvtTypeCard type =  (EvtTypeCard) e;
		PlayerVO pvo = (PlayerVO) holder;
		int bizType = -1;
		switch(type){
		case GET_CARD_PRIZE:
			Integer cardType = event.getParam(EvtParamType.TYPE);
			switch(cardType){
			case CardCons.CARD_TYPE_MONTH:
				bizType = StatsCons.BIZ_TYPE_MONTH_CARD_TIMES;
				break;
			case CardCons.CARD_TYPE_YEAR:
				bizType = StatsCons.BIZ_TYPE_YEAR_CARD_TIMES;
				break;
			}
			break;
		default:
			break;
		}
		if(bizType>-1)
			add(cmd, parent, pvo, EvtTypeStats.SPEC_BIZ_COUNT, bizType, 1L);
	}





	public void dealGuildEvt(Map parent, CMD cmd, EvtHolder holder, GameEvent event, Enum<?> e) {
		EvtTypeGuild type =  (EvtTypeGuild) e;
		PlayerVO pvo = (PlayerVO) holder;
		int bizType = -1;
		switch(type){
		case Join:
			bizType = StatsCons.BIZ_TYPE_GUILD_JOIN_TIMES;
			break;
		case Donate:
			bizType = StatsCons.BIZ_TYPE_GUILD_BUILD_TIMES;
			break;
		case LV_UP:
			update(cmd, parent, pvo, EvtTypeStats.SPEC_BIZ_COUNT, StatsCons.BIZ_TYPE_GUILD_LV, event.getParam(EvtParamType.AFTER));
			break;
		default:
			break;
		}
		if(bizType>-1)
			add(cmd, parent, pvo, EvtTypeStats.SPEC_BIZ_COUNT, bizType, 1L);
	}


	public void dealShopEvt(Map parent, CMD cmd, EvtHolder holder, GameEvent event, Enum<?> e) {
		EvtTypeShop type =  (EvtTypeShop) e;
		PlayerVO pvo = (PlayerVO) holder;
		int bizType = -1;
		switch(type){
		case BUY:
			bizType = StatsCons.BIZ_TYPE_SHOPPING_TIMES;
			ShopType shopType = event.getParam(EvtParamType.SHOP_Type);
			if(shopType == ShopType.Guild){
				add(cmd, parent, pvo, EvtTypeStats.SPEC_BIZ_COUNT, StatsCons.BIZ_TYPE_GUILD_EXCHG_TIMES, 1L);
			}
			break;
		default:
			break;
		}
		if(bizType>-1)
			add(cmd, parent, pvo, EvtTypeStats.SPEC_BIZ_COUNT, bizType, 1L);
	}




	public void dealRankEvt(Map parent, CMD cmd, EvtHolder holder, Enum<?> e) {
		EvtTypeRank type =  (EvtTypeRank) e;
		PlayerVO pvo = (PlayerVO) holder;
		int bizType = -1;
		switch(type){
		case ADD_LIKE:
			bizType = StatsCons.BIZ_TYPE_RANK_ADD_LIKE_TIMES;
			break;
		}
		if(bizType>-1)
			add(cmd, parent, pvo, EvtTypeStats.SPEC_BIZ_COUNT, bizType, 1L);
	}






	public void dealPlayerEvt(Map parent, CMD cmd, EvtHolder holder, GameEvent event, Enum<?> e) {
		EvtTypePlayer type =  (EvtTypePlayer) e;
		PlayerVO pvo = (PlayerVO) holder;
		int bizType = -1;
		switch(type){
		case FightForce:
			bizType = StatsCons.BIZ_TYPE_FIGHT_FORCE_SUM;
			break;
		case LevelUp:
			bizType = StatsCons.BIZ_TYPE_PLAYER_LV;
			break;
		case ACTIVENESS:
			bizType = StatsCons.BIZ_TYPE_ACTIVENESS;
			break;
		case EnterGame:
			if(!event.getParam(EvtParamType.SAME_DATE)){
				add(cmd, parent, pvo, EvtTypeStats.SPEC_BIZ_COUNT, StatsCons.BIZ_TYPE_LOGIN_DATES, 1L);
			}
			break;
		case VipLevelUp:
			bizType = StatsCons.BIZ_TYPE_VIP_LV;
			break;
		case Worship:
			add(cmd, parent, pvo, EvtTypeStats.SPEC_BIZ_COUNT,  StatsCons.BIZ_TYPE_WORSHIP_TIMES, 1L);
			break;
		case CheckIn:
			add(cmd, parent, pvo, EvtTypeStats.SPEC_BIZ_COUNT,  StatsCons.BIZ_TYPE_CHECK_IN_TIMES, 1L);
			break;
		default:
			break;
		}
		if(bizType>-1)
			update(cmd, parent, pvo, EvtTypeStats.SPEC_BIZ_COUNT, bizType, event.getParam(EvtParamType.AFTER));
	}




}
