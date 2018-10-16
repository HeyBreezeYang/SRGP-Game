package com.cellsgame.game.module.player.msg;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.activity.msg.MsgFactoryActivity;
import com.cellsgame.game.module.card.msg.MsgFactoryCard;
import com.cellsgame.game.module.depot.msg.MsgFactoryDepot;
import com.cellsgame.game.module.friend.msg.MsgFactoryFriend;
import com.cellsgame.game.module.guild.msg.MsgFactoryGuild;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.hero.vo.HeroVO;
import com.cellsgame.game.module.pay.msg.MsgFactoryPay;
import com.cellsgame.game.module.player.cons.FuncTimes;
import com.cellsgame.game.module.player.vo.CheckInVO;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.quest.msg.MsgFactoryQuest;
import com.cellsgame.game.module.rank.msg.MsgFactoryRank;
import com.cellsgame.game.module.store.msg.MsgFactoryStore;

import java.util.*;
import java.util.function.Function;

import static com.cellsgame.common.util.DateUtil.getSecond;

/**
 * File Description.
 *
 * @author Yang
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class MsgFactoryPlayer extends MsgFactory {

	public static final String PLAYER = "p";
	public static final String PLAYER_SCENE = "s";
	public static final String PLAYER_LOGOUT_TIME = "logout";       // 登出时间
	public static final String PLAYER_LOGIN_TIME = "login";         // 登陆时间
	public static final String PLAYER_ID = "pid";                   // PID
	public static final String PLAYER_ACTIVENESS = "an";            // 活跃度
	public static final String PLAYER_ACTIVENESS_PRIZED = "anp";    // 活跃度奖励

	public static final String PLAYER_FUNCTIMES = "times";    // 功能次数
	public static final String PLAYER_PARTY_TIMES = "ptyTimes";//酒会次数

	public static final String PLAYER_Random_Name = "rnm";
	public static final String PLAYER_FIRST_PAY_PRIZE_STATE = "fpay";

	public static final String PLAYER_STORY = "stry";
	public static final String PLAYER_ADD_STORYID = "adStyId";

	private static final String RANK_ADD_LIKE = "addlk";        // 已结点过赞的玩家ID
	private static final String PLAYER_SKILL_FIGHTFORCE = "skForce"; //角色技能战斗力

	private static final String ALL_HERO = "allhero";



	private static final String CHECK_IN = "cki";
	private static final String CHECK_IN_DAYS = "ckds";
	private static final String CAN_CHECK_IN = "cci";
	private static final String PRIZED_TOTAL_CHECK_IN = "pld";

	private static final String BUY_MONEY_CRIT = "crit";
	private static final String PLAYER_CHILD_LEN = "clen";
	private static final String PLAYER_PARTY_SCORE = "ptySco";
	private static final String PLAYER_WORSHIP = "worship";
	private static final String PLAYER_TITLE_DEC = "titleDec";
	private static final String PLAYER_LAST_PET_TIME = "lstPetTime";

	private static final String HERO_NAME = "heroName";

	/**英雄数据**/
	private static final String HERO_ID = "heroId";//英雄id
	private static final String HERO_CID = "heroCid";//cid
	private static final String HERO_LEVEL = "heroLevel";//英雄等级
	private static final String HERO_START = "heroStart";//英雄当前星级
	private static final String HERO_STAGE = "heroStage";//英雄突破等级
	private static final String HERO_CAREEREXCHANGEED = "heroCareerexchangeed";//是否转职
	private static final String HERO_HP = "heroHp";
	private static final String HERO_SPEED = "heroSpeed";
	private static final String HERO_ATTACK = "heroIdAttack";
	private static final String HERO_DEFENSE = "heroDefense";
	private static final String HERO_MAGICDEFENSE = "heroMagicdefense";
	private static final String HERO_LUCK = "heroLuck";
	private static final String HERO_SP = "heroSp";





	private static final MsgFactoryPlayer instance = new MsgFactoryPlayer();


	public static MsgFactoryPlayer instance() {
		return instance;
	}

	@Override
	public String getModulePrefix() {
		return PLAYER;
	}


	public Map<String, Object> getPlayerBaseInfoMessage(PlayerVO player){
		Map<String, Object> result = GameUtil.createSimpleMap();
		Map pInfo = gocInfoMap(result);
		pInfo.put(PLAYER_ID, player.getId());
		pInfo.put(NAME, player.getName());
//		pInfo.put(LEVEL, player.getLevel());
//		pInfo.put(EXP, player.getExp());
		pInfo.put(CREATE_DATA, player.getCreateDate().getTime());
		pInfo.put(LOGIN_DATA, player.getLoginDate());
		return result;
	}

	public Map<String, Object> getPlayerMessage(PlayerVO player) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		Map pInfo = gocInfoMap(result);
		pInfo.put(ID, player.getId());
		pInfo.put(NAME, player.getName());

		pInfo.put(SPT, player.getSpirit());
		pInfo.put(JJA, player.getJjcfightAcount());
		pInfo.put(GOLD, player.getGold());
		pInfo.put(HEROZY, player.getHerozyAccount());

//		pInfo.put(IMG, player.getImage());
		pInfo.put(CREATE_DATA, String.valueOf(player.getCreateDate().getTime()));
		pInfo.put(LOGIN_DATA, String.valueOf(player.getLoginDate()));

		pInfo.put(GUILD_ID, player.getGuildID());
//		GuildVO guild = player.getGuild();
//		pInfo.put(GUILD_NAME, guild == null ? "" : guild.getName());

		pInfo.put(PLAYER_ID, player.getId());
//		pInfo.put(PLAYER_ACTIVENESS, player.getActiveness());
		pInfo.put(PLAYER_FIRST_PAY_PRIZE_STATE, player.getFirstPayPrizeState());

//		gocLstIn(pInfo, PLAYER_ACTIVENESS_PRIZED).addAll(player.getActivenessPrized());
//		pInfo.put(VIP, player.getVip());
//		pInfo.put(VIP_EXP, player.getVipExp());
//		gocLstIn(pInfo, VIP_PRIZE).addAll(player.getVipPrize());
//		pInfo.put(EXP, player.getExp());
//		pInfo.put(LEVEL, player.getLevel());
		pInfo.put(ATT, player.getAtt().get().getValue());
//		pInfo.put(PLAYER_WORSHIP, player.isWorship());
//		pInfo.put(PLAYER_TITLE_DEC, player.getTitleDec());
//		pInfo.put(FIGHT_FORCE, player.getFightForce());

//		pInfo.put(CHECK_IN, getCheckInMsg(player));
		//gocLstIn(pInfo, PLAYER_STORY).addAll(player.getPlotIds());
		//getFuncTimesInfo(result, player);

		pInfo.put(ALL_HERO, setSendClientData(player));



		//result = MsgFactoryDepot.instance().getDepotInfoMsg(result, player);
		//result = MsgFactoryGuild.instance().memberBaseInfo(result, player);
		//result = MsgFactoryFriend.instance().getMyFriendInfo(result, player);
		//result = MsgFactoryActivity.instance().getActivityListMsg(result, player);
//		result = MsgFactoryCard.instance().getCardListMsg(result, player.getCardVOMap());
		//result = MsgFactoryPay.instance().getBuyOrderGoodsMapMsg(result, player.getBuyOrderItemTimes());
		//result = MsgFactoryQuest.instance().getQuestInfoMsg(result, player);
//		result = MsgFactoryRank.instance().getRankLikeInfoMsg(result,player.getRankLikeRec());
		//result = MsgFactoryStore.instance().getStoreInfoMsg(result);
		return result;
	}


	List<Map> setSendClientData(PlayerVO pvo) {
		List<HeroVO> fvo = pvo.getHeroList();
		List<Map> list = new ArrayList<Map>();
		for(HeroVO hervo:fvo){
			Map checkInData = GameUtil.createSimpleMap();
			checkInData.put(HERO_CID, hervo.getCid());
			checkInData.put(HERO_ATTACK, hervo.getAttack());
			checkInData.put(HERO_CAREEREXCHANGEED, hervo.getCareerExchanged());
			checkInData.put(HERO_DEFENSE, hervo.getDefense());
			checkInData.put(HERO_HP, hervo.getHp());
			checkInData.put(HERO_LEVEL, hervo.getLevel());
			checkInData.put(HERO_LUCK, hervo.getLuck());
			checkInData.put(HERO_MAGICDEFENSE, hervo.getMagicDefense());
			checkInData.put(HERO_SP, hervo.getSp());
			checkInData.put(HERO_SPEED, hervo.getSpeed());
			checkInData.put(HERO_START, hervo.getStar());
			checkInData.put(HERO_STAGE, hervo.getStage());
			list.add(checkInData);
		}
		return list;
	}


	Map<String, Object> getCheckInMsg(PlayerVO pvo) {
		CheckInVO fvo = pvo.getCheckInVO();
		Map checkInData = GameUtil.createSimpleMap();//gocMapIn(ret, CHECK_IN);
		checkInData.put(CHECK_IN_DAYS, fvo.getCheckInDaysPerMonth());
		checkInData.put(CAN_CHECK_IN, fvo.isChecked());
		checkInData.put(PRIZED_TOTAL_CHECK_IN, fvo.getTotalCheckInPrized());
		return checkInData;
	}


	public Map getCheckInUpdateMsg(Map ret, PlayerVO pvo) {
		CheckInVO fvo = pvo.getCheckInVO();
		Map opMap = gocOpMap(ret);
		Map checkInData = gocMapIn(opMap, CHECK_IN);
		checkInData.put(CHECK_IN_DAYS, fvo.getCheckInDaysPerMonth());
		checkInData.put(CAN_CHECK_IN, fvo.isChecked());
		checkInData.put(PRIZED_TOTAL_CHECK_IN, fvo.getTotalCheckInPrized());
		return ret;
	}


	private Map<Integer, Integer> toIntMap(Map<Integer, Long> integerLongMap) {
		Map<Integer, Integer> map = GameUtil.createSimpleMap();
		for (Map.Entry<Integer, Long> entry : integerLongMap.entrySet()) {
			map.put(entry.getKey(), entry.getValue().intValue());
		}
		return map;
	}

	public List<Map<?, ?>> getPlayersSceneMsg(Collection<PlayerVO> players, Function<PlayerVO, Object> extFunc) {
		List<Map<?, ?>> list = GameUtil.createList();
		for (PlayerVO player : players) {
			Map<Object, Object> map = GameUtil.createSimpleMap();
			map.put(ID, player.getId());
			map.put(NAME, player.getName());
//			map.put(IMG, player.getImage());
//			map.put(LEVEL, player.getLevel());
			if (extFunc != null) {
				map.put(EXT, extFunc.apply(player));
			}
			list.add(map);
		}
		return list;
	}


	public List<Map<?, ?>> getPlayersMoveMsg(Collection<PlayerVO> players) {
		List<Map<?, ?>> list = GameUtil.createList();
		for (PlayerVO player : players) {
			Map<Object, Object> map = GameUtil.createSimpleMap();
			map.put(ID, player.getId());
			list.add(map);
		}
		return list;
	}

	public Map getPlayerPropertyChangeMsg(Map msg, String key, Object params) {
		if (msg == null)
			msg = GameUtil.createSimpleMap();
		Map opInfo = gocOpMap(msg);
		opInfo.put(key, params);
		return msg;
	}

	public Map<String, Object> getPlayerQueryMsg(Map parent, PlayerVO playerVO) {
		Map op = gocOpMap(parent);
		Map query = gocMapIn(op, QUERY);
		getPlayerTipsInfo(query, playerVO);
		return parent;
	}

	private static final String  SERVER_ID = "sid";
	private static final String  STORY_CHAPTER = "sct";
	private static final String  STORY_PART = "spt";
	private static final String  STORY_INDEX = "sidx";
	private static final String  INTIMACYS = "itmys";
	private static final String  TITLES = "titles";
	private static final String  USE_TITLE = "useTitle";

	public Map<String, Object> getPlayerTipsInfo(Map<String, Object> info, PlayerVO playerVO) {
		if (null == info) info = GameUtil.createSimpleMap();
		info.put(NAME, playerVO.getName());
//		info.put(IMG, playerVO.getImage());
		info.put(PLAYER_ID, playerVO.getId());
//		info.put(LEVEL, playerVO.getLevel());
//		info.put(EXP, playerVO.getExp());
//		info.put(VIP, playerVO.getVip());
		info.put(ATT, playerVO.getAtt().get().getValue());
		info.put(SERVER_ID, playerVO.getServerId());
//		info.put(INTIMACYS, playerVO.getIntimacy());
		info.put(PLAYER_LOGOUT_TIME, getSecond(playerVO.getLogoutDate()));
		info.put(PLAYER_LOGIN_TIME, getSecond(playerVO.getLoginDate()));
		info.put(GUILD_NAME, playerVO.getGuild() == null ? "" : playerVO.getGuild().getName());
		return info;
	}

	public Map<String, Object> getBaseInfo(Map<String, Object> info, PlayerInfoVO baseInfo) {
		if (null == info) info = GameUtil.createSimpleMap();
		info.put(NAME, baseInfo.getName());
		info.put(IMG, baseInfo.getImage());
		info.put(PLAYER_ID, baseInfo.getPid());
		info.put(LEVEL, baseInfo.getPlv());
		info.put(FIGHT_FORCE, baseInfo.getFightForce());
		info.put(PLAYER_LOGOUT_TIME, getSecond(baseInfo.getLastLogOutTime()));
		info.put(PLAYER_LOGIN_TIME, getSecond(baseInfo.getLastLoginTime()));
		return info;
	}

	/**
	 * 剧情进度
	 */
	public Map<String, Object> getAddStoryIdMessage(Map<String, Object> parent, int storyId) {
		if (parent == null)
			parent = GameUtil.createSimpleMap();
		Map op = gocOpMap(parent);
		List<Integer> list = gocLstIn(op, PLAYER_ADD_STORYID);
		list.add(storyId);
		return parent;
	}

	/**
	 * 活跃度
	 *
	 * @param parent 消息
	 * @param pvo    玩家数据
	 * @return 玩家好感度数据，包括任务
	 */
	public Map getActivenessMsg(Map parent, PlayerVO pvo) {
//		parent = getPlayerPropertyChangeMsg(parent, PLAYER_ACTIVENESS, pvo.getActiveness());
		return parent;
	}


	public Map getFirstPayPrizeStateMsg(Map parent, PlayerVO playerVO) {
		return getPlayerPropertyChangeMsg(parent, PLAYER_FIRST_PAY_PRIZE_STATE, playerVO.getFirstPayPrizeState());
	}

	public Map getRandomNameMsg(Map parent, String name) {
		return getPlayerPropertyChangeMsg(parent, PLAYER_Random_Name, name);
	}


	public Map getActivenessMessage(Map parent, Set<Integer> prized) {
		return getPlayerPropertyChangeMsg(parent, PLAYER_ACTIVENESS_PRIZED, new ArrayList<>(prized));
	}


	public Map getPlayerExpUpdateMsg(Map parent, PlayerVO playerVO) {
		if (parent == null) parent = GameUtil.createSimpleMap();
		Map op = gocOpMap(parent);
//		op.put(EXP, playerVO.getExp());
		return parent;
	}

	public Map getPlayerVipUpdateMsg(Map parent, PlayerVO playerVO) {
		if (parent == null) parent = GameUtil.createSimpleMap();
		Map op = gocOpMap(parent);
//		op.put(VIP, playerVO.getVip());
//		op.put(VIP_EXP, playerVO.getVipExp());
		return parent;
	}

	public Map getPlayerLevelUpdateMsg(Map parent, PlayerVO playerVO) {
		if (parent == null) parent = GameUtil.createSimpleMap();
		Map op = gocOpMap(parent);
//		op.put(LEVEL, playerVO.getLevel());
		return parent;
	}

	/**
	 * 公会ID 更新
	 */
	public Map<String, Object> getGuildIDNameUpdateMsg(Map msg, int guildID, String guildName) {
		Map<String, Object> parent = getPlayerPropertyChangeMsg(msg, GUILD_ID, guildID);
		//        parent = getPlayerPropertyChangeMsg(parent, GUILD_SCENE, guildScene);
		parent = getPlayerPropertyChangeMsg(parent, GUILD_NAME, guildName);
		return parent;
	}

	public Map getFirstPayPrizeMsg(Map<String,Object> parent, Map prizeMap) {
		parent = creIfNull(parent);
		Map op = gocOpMap(parent);
		addPrizeMsg(op, prizeMap);
		return parent;
	}

	public static final String TIMES = "times";
	public static final String REV_TIME = "revTime";

    public Map getFuncTimesUpdate(Map<?, ?> parent, FuncTimes funcTimes, PlayerVO playerVO) {
		parent = creIfNull(parent);
		Map op = gocOpMap(parent);
		Map<Integer, Object> cnyOp = gocMapIn(op, PLAYER_FUNCTIMES);
		Map<String, Object> timesInfo = gocMapIn(cnyOp, funcTimes.getId());
//		timesInfo.put(TIMES, playerVO.getFuncTimes().getOrDefault(funcTimes.getId(), 0));
//		timesInfo.put(REV_TIME, String.valueOf(playerVO.getFuncTimesLastRevTime().getOrDefault(funcTimes.getId(), 0L)));
		return parent;
    }


//	public Map getTitleDecUpdate(Map<?, ?> parent, PlayerVO playerVO) {
//		return getPlayerPropertyChangeMsg(parent, PLAYER_TITLE_DEC, playerVO.getTitleDec());
//	}

	public Map getLastPetTimeUpdate(Map<?, ?> parent, int beautyCid, long time) {
		Map opInfo = gocOpMap(parent);
		Map lastPetTime = gocMapIn(opInfo, PLAYER_LAST_PET_TIME);
		lastPetTime.put(beautyCid, time);
		return parent;
	}

//	public Map getWorshipUpdate(Map<?, ?> parent, PlayerVO playerVO) {
//		return getPlayerPropertyChangeMsg(parent, PLAYER_WORSHIP, playerVO.isWorship());
//	}

    public Map getFuncTimesInfo(Map<?, ?> parent, PlayerVO playerVO){
		parent = creIfNull(parent);
		Map info = gocInfoMap(parent);
		Map<Integer, Object> cnyOp = gocMapIn(info, PLAYER_FUNCTIMES);
		for (FuncTimes funcTimes : FuncTimes.values()) {
			Map<String, Object> timesInfo = gocMapIn(cnyOp, funcTimes.getId());
//			timesInfo.put(TIMES, playerVO.getFuncTimes().getOrDefault(funcTimes.getId(), 0));
//			timesInfo.put(REV_TIME, String.valueOf(playerVO.getFuncTimesLastRevTime().getOrDefault(funcTimes.getId(), 0L)));
		}
		return parent;
	}

	/**
	 * @param parent
 * @param pvo
	 */
	public Map getPlayerAttUpdateMsg(Map parent, PlayerVO pvo) {
		parent = creIfNull(parent);
		Map op = gocOpMap(parent);
		op.put(ATT, pvo.getAtt().get().getValue());
		return parent;
	}

    public Map getPlayerNameUpdate(Map parent, PlayerVO player) {
		parent = getPlayerPropertyChangeMsg(parent, NAME, player.getName());
		return parent;
    }

//	public Map getPlayerVipPrizeUpdateMsg(Map parent, PlayerVO player) {
//		return getPlayerPropertyChangeMsg(parent, VIP_PRIZE, new ArrayList<>(player.getVipPrize()));
//	}
}