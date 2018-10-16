package com.cellsgame.game.module;

import java.util.Map;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.vo.PlayerVO;

public interface IBuildData {

    public static final String DATA_SIGN_PLAYER = "player";
    public static final String DATA_SIGN_HERO = "hero";
    public static final String DATA_SIGN_WORKER = "worker";
    public static final String DATA_SIGN_SKILL = "skill";
    public static final String DATA_SIGN_DEPOT = "depot";
    public static final String DATA_SIGN_EXPLORE = "explore";
    public static final String DATA_SIGN_SHOP = "shop";
    public static final String DATA_SIGN_SHOP_ITEM_RECORD = "shop_item_record";
    public static final String DATA_SIGN_MAIL = "mail";
    public static final String DATA_SIGN_SEARCH = "search";
    public static final String DATA_SIGN_CHECK_IN = "check_in";
    public static final String DATA_SIGN_CONVERT = "convert";
    public static final String DATA_SIGN_HALO = "halo";
    public static final String DATA_SIGN_WEEKLY_PRIZE_DUNGEON_GROUP = "wpDungeonGrp";
    public static final String DATA_SIGN_MS_DUNGEON_INFO = "msDungeonInfo";
    public static final String DATA_SIGN_FIXED_DROP = "fixed_drop";
    public static final String DATA_SIGN_EXPRESSION = "cab";
    public static final String DATA_SIGN_QUEST = "quest";
    public static final String DATA_SIGN_FAVOR_QUEST = "favor_quest";
    public static final String DATA_SIGN_GUILD_QUEST = "guild_quest";
    public static final String DATA_SIGN_Adventure_QUEST = "adventure_quest";
    public static final String DATA_SIGN_EQUIP = "equip";
    public static final String DATA_SIGN_GOODS_GEM = "gem";
    public static final String DATA_SIGN_RUNE = "rune";
    public static final String DATA_SIGN_EXPEDITION = "expedition";
    public static final String DATA_SIGN_TOWER = "tower";
    public static final String DATA_SIGN_MAGIC_STONE = "magicStone";
    public static final String DATA_SIGN_MAKELEVEL = "makeLv";
    //    public static final String DATA_SIGN_HOME_STABLE = "homeStable";
    public static final String DATA_SIGN_BUSINESS = "business";
    public static final String DATA_SIGN_FRIEND_LIST = "friendList";
    public static final String DATA_SIGN_INVESTMENT = "investment";
    public static final String DATA_SIGN_ADV_TASK_HOME = "advTaskHome";
    public static final String DATA_SIGN_Guild_Play = "guild_play";
    public static final String DATA_SIGN_TRADE_INFO = "tradeInfo";
    public static final String DATA_SIGN_Rank_VO = "RANK_VO";
    public static final String DATA_SIGN_ACHIEVE = "ACHIEVE_VO";
    public static final String DATA_SIGN_ACTIVITY_COND = "actCond";
    public static final String DATA_SIGN_ACTIVITY_BEV = "actBev";
    public static final String DATA_SIGN_FREEAP = "freeAp";
    public static final String DATA_SIGN_WISH = "wish";
    public static final String DATA_SIGN_MINIGAME = "mini_game";
    public static final String DATA_SIGN_ORDER = "order";
    public static final String DATA_SIGN_CARD = "card";
    public static final String DATA_SIGN_SETUPLOCK = "setupLock";
    public static final String DATA_SIGN_ONLINE_PRIZE = "onlinePrize";
    public static final String DATA_SIGN_FUND = "fund";
    public static final String DATA_SIGN_BEAUTY = "beauty";
    public static final String DATA_SIGN_CHILD = "child";
    public static final String DATA_SIGN_STORY_REC = "storyRec";
    public static final String DATA_SIGN_QUEST_PROC = "questProc";
    public static final String DATA_SIGN_QUEST_REC = "questRec";
    public static final String DATA_SIGN_STATS = "stats";
    public static final String DATA_SIGN_ACADEMY_REC = "academyRec";
    public static final String DATA_SIGN_MIRACLE_REC = "miracleRec" ;
    public static final String DATA_SIGN_SOCIETY = "soci";
    public static final String DATA_SIGN_SOCIETY_REC = "sociRec";
    public static final String DATA_SIGN_SOCIETY_PRZ_REC = "sociPrzRec" ;
    public static final String DATA_SIGN_PARTY_REC = "partyRec" ;
    public static final String DATA_SIGN_PLUNDER = "plunder" ;
    public static final String DATA_SIGN_STORE_ITEM_RECORD = "store_item_record";
    public static final String DATA_SIGN_DEBATE_REC = "debateRec";
    public static final String DATA_SIGN_TRADE = "trade" ;
    public static final String DATA_SIGN_PLAYER_BOSS = "player_boss";

    public static final String DATA_SIGN_RANK_LIKE_REC = "rkLkRec";
    public static final String DATA_SIGN_VX_CONTACTS = "vxContacts";
    public static final String DATA_SIGN_FIN_DIALOG = "finDialog";
    public static final String DATA_SIGN_BEAUTYPLOT = "beautyPlot";

    void buildAsLoad(CMD cmd, PlayerVO pvo, Map<String, ?> data) throws LogicException;
    
    void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException;
}
