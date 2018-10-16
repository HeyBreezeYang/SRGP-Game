package com.cellsgame.game.module.player.vo;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CachePlayerDBID;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.module.activity.vo.ActivityRecVO;
import com.cellsgame.game.module.hero.vo.HeroVO;
import com.cellsgame.game.module.player.att.PlayerAttCal;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.cellsgame.game.module.quest.vo.QuestProcVO;
import com.cellsgame.game.module.quest.vo.QuestRecVO;
import com.cellsgame.game.module.card.vo.CardVO;
import com.cellsgame.game.module.chat.cons.ChatType;
import com.cellsgame.game.module.depot.vo.DepotVO;
import com.cellsgame.game.module.friend.vo.FriendListVO;
import com.cellsgame.game.module.func.vo.FixedDropVO;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.mail.vo.MailVO;
import com.cellsgame.game.module.rank.vo.RankLikeRecVO;
import com.cellsgame.game.module.shop.vo.ShopItemRecordVO;
import com.cellsgame.game.module.shop.vo.ShopVO;
import com.cellsgame.game.module.skill.vo.SkillSealVO;
import com.cellsgame.game.module.skill.vo.SkillVO;
import com.cellsgame.game.module.stats.vo.StatsVO;
import com.cellsgame.game.module.store.vo.StoreItemRecordVO;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import java.util.*;

public class PlayerVO extends DBVO implements EvtHolder, QuestHolder {

    private PlayerAttCal att = new PlayerAttCal();
    /***
     *
     * 新增玩家基础
     */
    private int spirit=99; //体力
    private int jjcfightAcount=0;//竞技场挑战次数
    private int gold=0;//金币
    private int herozyAccount=0;//英雄之羽

    public int getSpirit() {
        return spirit;
    }

    public int getJjcfightAcount() {
        return jjcfightAcount;
    }

    public void setSpirit(int spirit) {
        this.spirit = spirit;
    }

    public void setJjcfightAcount(int jjcfightAcount) {
        this.jjcfightAcount = jjcfightAcount;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setHerozyAccount(int herozyAccount) {
        this.herozyAccount = herozyAccount;
    }

    public int getGold() {
        return gold;
    }

    public int getHerozyAccount() {
        return herozyAccount;
    }

    //test
    private int id;                      // 唯一ID
    private String accountId;               // 帐号ID
    private int serverId;                // 服务器ID
    /**
     * 角色名字
     */
    @Save(ix = 0)
    private String name;                    // 角色名字
//    @Save(ix = 1)
//    private int image;                      // 角色形象
//    @Save(ix = 3)
//    private int vip;                        //  VIP等级
//    @Save(ix = 4)
//    private int level;                      // 玩家等级
//    @Save(ix = 5)
//    private int exp;
    @Save(ix = 7)
    private long logoutDate;                // 登出时间
    @Save(ix = 8)
    private long loginDate;                 // 登入时间
    @Save(ix = 9)
    private int guildID;                   // 家族ID-------公会id
    @Save(ix = 10)
    private long lastDailyQuestRefTime;      // 任务最后刷新时间
    @Save(ix = 11)
    private Set<Integer> finishedQuests;    // 已完全的任务, 不能继续接受的任务
//    @Save(ix = 13)
//    private int activeness;                  // 活跃度
    @Save(ix = 14)
    private long resetTime;                  // 重置时间(数据重置时间)
//    @Save(ix = 15)
//    private Set<Integer> activenessPrized;       // 已领取的活跃度奖励列表
//    @Save(ix = 16)
//    private long fightForce;                     // 战力 缓存
//    @Save(ix = 21)
//    private Set<Integer> unusedSet1;            // 已经点赞的
    @Save(ix = 22)
    private Set<Integer> plotIds;                // 剧情Id
//    @Save(ix = 26)
//    private Set<Integer> prizedAchievePointCID;     // 成就点数领奖 记录
    @Save(ix = 36)
    private int systemShopVersion;                         // 系统商店版本号
    @Save(ix = 37)
    private int state;                   //玩家状态
    @Save(ix = 38)
    private int firstPayPrizeState;                   //第一次充值奖励状态
//    @Save(ix = 39)
//    private Map<Integer, Integer> funcTimes;
//    @Save(ix = 40)
//    private Map<Integer, Long> funcTimesLastRevTime;
  
//    @Save(ix = 46)
//    private boolean worship;
//    @Save(ix = 47)
//    private String titleDec;
//    @Save(ix = 48)
//    private int intimacy;
   
//    @Save(ix = 50)
//    private Set<Integer> vipPrize;
//    @Save(ix = 51)
//    private int vipExp;
   
   
    /**
     * ---------------逻辑数据--------------------
     */
    private StatsVO statsVO;
    private volatile boolean online;                                    // 是否在线
    private String token;
    private volatile MessageController messageController;
  
    private DepotVO depotVO;                                            // 背包数据
//    private Map<Integer, ShopItemRecordVO> shopItemRecordVOMap;         // 全服或者系统商品限购数据
    private Map<Integer, ShopVO> shopVOMap;                             // 玩家商店
    private Map<Integer, MailVO> mailVOMap;                             // 玩家可使用邮件
  
    private CheckInVO checkInVO;                                        // 签到数据
    private Set<Integer> finDungeons;                                   // 已经完成了的副本集合
  
    private FixedDropVO fixedDropVO;                                    // 固定掉落数据
   
//    private int heroTotalWeight;                                        // 玩家所有英雄的累计权重
    private FriendListVO friendList;                                    // 好友列表
    private Map<String, ActivityRecVO> activityRecs;                    // 活动数据

    private Map<ChatType, Long> lastChatTime;  // 聊天时间记录
    private Map<String, Integer> buyOrderItemTimes = GameUtil.createSimpleMap();
//    private Map<Integer, CardVO> cardVOMap;

    
    
    private Map<Integer, QuestProcVO> questProcMap = GameUtil.createSimpleMap();
    private Map<Integer, QuestRecVO> questRecMap = GameUtil.createSimpleMap();

 
//    private RankLikeRecVO rankLikeRec;
//    private Table<Integer, Integer, StoreItemRecordVO> storeItemRecordVOMap;

    private Map<Integer, HeroVO> heroes;

    private List<HeroVO> heroList =new ArrayList<HeroVO>();

    private Map<Integer, SkillVO> seals;

    public PlayerVO(){
        
    }
    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = (int)pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{accountId, serverId};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        accountId = (String) relationKeys[0];
        serverId = (int) relationKeys[1];
    }

    @Override
    protected void init() {
        name = "";
//        image = 0;
//        heroTotalWeight = 0;
        lastDailyQuestRefTime = 0;
        shopVOMap = GameUtil.createSimpleMap();
//        shopItemRecordVOMap = GameUtil.createSimpleMap();
        mailVOMap = GameUtil.createSimpleMap();
        finDungeons = GameUtil.createSet();
        finishedQuests = Sets.newHashSet();
//        activenessPrized = Sets.newHashSet();
//        vipPrize = Sets.newHashSet();
//        level = 1;
//        setUnusedSet1(new HashSet<>());
        plotIds = new HashSet<>();
        lastChatTime = new HashMap<>();
//        prizedAchievePointCID = GameUtil.createSet();
        activityRecs = GameUtil.createSimpleMap();
//        funcTimes = GameUtil.createSimpleMap();
//        funcTimesLastRevTime = GameUtil.createSimpleMap();
      
//        storeItemRecordVOMap = HashBasedTable.create();
//        titleDec = "";
      
        heroes = GameUtil.createSimpleMap();
        seals = GameUtil.createSimpleMap();
    }

    public Map<ChatType, Long> getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(Map<ChatType, Long> lastChatTime) {
        this.lastChatTime = lastChatTime;
    }

  
    @Override
    public Integer getCid() {
        return 0;
    }

    @Override
    public void setCid(Integer cid) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeroes(Map<Integer, HeroVO> heroes) {
        this.heroes = heroes;
    }
    //-------------------------------影响相关


    public List<HeroVO> getHeroList() {
        return heroList;
    }

    public void setHeroList(List<HeroVO> heroList) {
        this.heroList = heroList;
    }


    public CheckInVO getCheckInVO() {
        return checkInVO;
    }

    public void setCheckInVO(CheckInVO checkInVO) {
        this.checkInVO = checkInVO;
    }

    public int getSystemShopVersion() {
        return systemShopVersion;
    }

    public void setSystemShopVersion(int systemShopVersion) {
        this.systemShopVersion = systemShopVersion;
    }
    @Override
    protected Object initPrimaryKey() {
        id = CachePlayerDBID.PLAYER_DBID.incrementAndGet();
        return id;
    }


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

//    public int getVip() {
//        return vip;
//    }

//    public void setVip(int vip) {
//        this.vip = vip;
//    }

    public MessageController getMessageController() {
        return messageController;
    }

    public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DepotVO getDepotVO() {
        return depotVO;
    }

    public void setDepotVO(DepotVO depotVO) {
        this.depotVO = depotVO;
    }

//    public Map<Integer, ShopItemRecordVO> getShopItemRecordVOMap() {
//        return shopItemRecordVOMap;
//    }

    public Map<Integer, ShopVO> getShopVOMap() {
        return shopVOMap;
    }

//    public Set<Integer> getActivenessPrized() {
//        return activenessPrized;
//    }

//    public void setActivenessPrized(Set<Integer> activenessPrized) {
//        this.activenessPrized = activenessPrized;
//    }

    public boolean finDungeon(int dunId) {
        return finDungeons.contains(dunId);
    }

    public Set<Integer> getFinDungeons() {
        return finDungeons;
    }

 
    public Map<Integer, MailVO> getMailVOMap() {
        return mailVOMap;
    }

    public GuildVO getGuild() {
        return CacheGuild.getGuildByID(this.guildID);
    }

    public FixedDropVO getFixedDropVO() {
        return fixedDropVO;
    }

    public void setFixedDropVO(FixedDropVO fixedDropVO) {
        this.fixedDropVO = fixedDropVO;
    }

    public long getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(long logoutDate) {
        this.logoutDate = logoutDate;
    }

    public long getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(long loginDate) {
        this.loginDate = loginDate;
    }

    public int getGuildID() {
        return guildID;
    }

    public void setGuildID(int guildID) {
        this.guildID = guildID;
    }

    public long getLastDailyQuestRefTime() {
        return lastDailyQuestRefTime;
    }

    public void setLastDailyQuestRefTime(long lastQuestRefreshTime) {
        this.lastDailyQuestRefTime = lastQuestRefreshTime;
    }

    public Set<Integer> getFinishedQuests() {
        return finishedQuests;
    }

    public void setFinishedQuests(Set<Integer> finishedQuests) {
        this.finishedQuests = finishedQuests;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

//    public int getActiveness() {
//        return activeness;
//    }

//    public void setActiveness(int activeness) {
//        this.activeness = activeness;
//    }

    public long getResetTime() {
        return resetTime;
    }

    public void setResetTime(long resetTime) {
        this.resetTime = resetTime;
    }

    public FriendListVO getFriendList() {
        return friendList;
    }

    public void setFriendList(FriendListVO friendList) {
        this.friendList = friendList;
    }

//    public long getFightForce() {
//        return fightForce;
//    }

//    public void setFightForce(long fightForce) {
//        this.fightForce = fightForce;
//    }
    
    public Set<Integer> getPlotIds() {
        return plotIds;
    }

    public void setPlotIds(Set<Integer> plotIds) {
        this.plotIds = plotIds;
    }

//    public Set<Integer> getPrizedAchievePointCID() {
//        return prizedAchievePointCID;
//    }
//
//    public void setPrizedAchievePointCID(Set<Integer> prizedAchievePointCID) {
//        this.prizedAchievePointCID = prizedAchievePointCID;
//    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerVO playerVO = (PlayerVO) o;

        return id == playerVO.id;
    }

    public Map<String, ActivityRecVO> getActivityRecs() {
        return activityRecs;
    }

    public void setActivityRecs(Map<String, ActivityRecVO> activityRecs) {
        this.activityRecs = activityRecs;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Map<String, Integer> getBuyOrderItemTimes() {
        return buyOrderItemTimes;
    }

    public void setBuyOrderItemTimes(Map<String, Integer> buyOrderItemTimes) {
        this.buyOrderItemTimes = buyOrderItemTimes;
    }

//    public Map<Integer, CardVO> getCardVOMap() {
//        return cardVOMap;
//    }

//    public void setCardVOMap(Map<Integer, CardVO> cardVOMap) {
//        this.cardVOMap = cardVOMap;
//    }

    public int getFirstPayPrizeState() {
        return firstPayPrizeState;
    }

    public void setFirstPayPrizeState(int firstPayPrizeState) {
        this.firstPayPrizeState = firstPayPrizeState;
    }


	public StatsVO getStatsVO() {
		return statsVO;
	}


	public void setStatsVO(StatsVO statsVO) {
		this.statsVO = statsVO;
	}

 
    public PlayerAttCal getAtt() {
        return att;
    }

//    public int getLevel() {
//        return level;
//    }

//    public void setLevel(int level) {
//        this.level = level;
//    }

//    public int getExp() {
//        return exp;
//    }

//    public void setExp(int exp) {
//        this.exp = exp;
//    }

	

//    public Map<Integer, Integer> getFuncTimes() {
//        return funcTimes;
//    }

//    public void setFuncTimes(Map<Integer, Integer> funcTimes) {
//        this.funcTimes = funcTimes;
//    }

//    public Map<Integer, Long> getFuncTimesLastRevTime() {
//        return funcTimesLastRevTime;
//    }

//    public void setFuncTimesLastRevTime(Map<Integer, Long> funcTimesLastRevTime) {
//        this.funcTimesLastRevTime = funcTimesLastRevTime;
//    }

    

  
    /**
	 * @see com.cellsgame.game.module.quest.vo.QuestHolder#getPlayer()
	 */
	@Override
	public PlayerVO getPlayer() {
		return this;
	}


	/**
	 * @see com.cellsgame.game.module.quest.vo.QuestHolder#holderID()
	 */
	@Override
	public Integer holderID() {
		return id;
	}


	/**
	 * @see com.cellsgame.game.module.quest.vo.QuestHolder#getQuestProcMap()
	 */
	@Override
	public Map<Integer, QuestProcVO> getQuestProcMap() {
		// TODO Auto-generated method stub
		return questProcMap;
	}


	/**
	 * @see com.cellsgame.game.module.quest.vo.QuestHolder#getQuestRecMap()
	 */
	@Override
	public Map<Integer, QuestRecVO> getQuestRecMap() {
		// TODO Auto-generated method stub
		return questRecMap;
	}

//	public Set<Integer> getUnusedSet1() {
//		return unusedSet1;
//	}

//	public void setUnusedSet1(Set<Integer> unusedSet1) {
//		this.unusedSet1 = unusedSet1;
//	}

//	public RankLikeRecVO getRankLikeRec() {
//		return rankLikeRec;
//	}

//	public void setRankLikeRec(RankLikeRecVO rankLikeRec) {
//		this.rankLikeRec = rankLikeRec;
//	}

//    public Table<Integer, Integer, StoreItemRecordVO> getStoreItemRecordVOMap() {
//        return storeItemRecordVOMap;
//    }

//    public void setStoreItemRecordVOMap(Table<Integer, Integer, StoreItemRecordVO> storeItemRecordVOMap) {
//        this.storeItemRecordVOMap = storeItemRecordVOMap;
//    }



//    public boolean isWorship() {
//        return worship;
//    }

//    public void setWorship(boolean worship) {
//        this.worship = worship;
//    }

//    public String getTitleDec() {
//        return titleDec;
//    }

//    public void setTitleDec(String titleDec) {
//        this.titleDec = titleDec;
//    }

//    public int getIntimacy() {
//        return intimacy;
//    }

//    public void setIntimacy(int intimacy) {
//        this.intimacy = intimacy;
//    }

   

  

//    public Set<Integer> getVipPrize() {
//        return vipPrize;
//    }

//    public void setVipPrize(Set<Integer> vipPrize) {
//        this.vipPrize = vipPrize;
//    }

//    public int getVipExp() {
//        return vipExp;
//    }

    public void setVipExp(int vipExp) {
//        this.vipExp = vipExp;
    }

    public Map<Integer, HeroVO> getHeroes() {
	    return heroes;
    }


    public Map<Integer, SkillVO> getSeals() {
        return seals;
    }



    public void setSeals(Map<Integer, SkillVO> seals) {
        this.seals = seals;
    }

}
