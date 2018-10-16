package com.cellsgame.game.module;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "rawtypes", "WeakerAccess"})
public abstract class MsgFactory {
    /* A---------------------------------------------*/
    public static final String ADD = "add";
    public static final String ATT = "att";
    public static final String ATT_TYPE = "aT";
    public static final String ATT_VALUE = "aV";

    /* B---------------------------------------------*/


    /* C---------------------------------------------*/
    public static final String CID = "cid";
    public static final String CAL_TYPE = "calTyp";
    public static final String CONDITION = "cdit";
    public static final String CONTENT = "cont";
    public static final String CHAT = "chat";
    public static final String CD = "cd";
    public static final String COUNT = "count";
    public static final String CREATE_DATA = "crtDt";
    public static final String LOGIN_DATA = "lgnDt";

    /* D---------------------------------------------*/
    public static final String DELETE = "del";
    public static final String DESC = "desc";
    public static final String DATE = "date";
    public static final String DATA = "data";
    public static final String DQSM = "dqsm";

    /* E---------------------------------------------*/
    public static final String EXP = "exp";
    public static final String EXT = "ext";

    /* F---------------------------------------------*/
    public static final String FAVOR = "fa";
    public static final String FIGHT = "ft";
    public static final String FAVOR_LEVEL = "faLv";
    public static final String AUTO_FIGHT = "atft";
    public static final String FIGHT_FORCE = "ff";
    public static final String FIGHT_INFO = "fgtInfo";
    public static final String FIGHT_ID = "fid";
    public static final String FINISH = "fin";
    public static final String FLOOR = "flr";
    public static final String FLOOR_MAX = "flrMax";
    public static final String FORMATION = "fmt";
    public static final String FURY = "fry";
    public static final String FROM = "frm";

    /* G---------------------------------------------*/
    public static final String GOODS_INDEX = "gix";
    public static final String GRADE_LEVEL = "gLv";
    public static final String GOODS = "gs";
    public static final String GOODS_CID = "gcid";
    public static final String GUILD = "gui";
    public static final String GUILD_NAME = "guinm";
    public static final String GOAL = "goal";
    public static final String GUILD_ID = "guildId";
    public static final String GUILD_BOSS_LF = "bossLf";
    public static final String GROUP = "gup";
    public static final String GOLD = "gold";
    /* H---------------------------------------------*/
    public static final String HERO = "h";
    public static final String HEROS = "hs";
    public static final String HELP = "hlp";
    public static final String HERO_LV = "hlv";
    public static final String HERO_CID = "hcid";
    public static final String HEROZY = "herozy";
    /* I---------------------------------------------*/
    public static final String ID = "id";
    public static final String IMG = "img";
    public static final String IMG2 = "img2";
    public static final String IMG2_List = "img2s";
    public static final String INDEX = "ix";
    public static final String INFO = "info";
    public static final String INFO_SUFFIX = "Info";

    /* J---------------------------------------------*/
    public static final String JOIN = "join";
    public static final String JJA = "jja";
	/* K---------------------------------------------*/

    /* L---------------------------------------------*/
    public static final String LEADER = "leder" ;
    public static final String LEVEL = "lv";
    public static final String LIFE = "hp";
    public static final String LIST = "lst";
    public static final String LIMIT = "lmt";
    public static final String LOGIN = "lgin";
    public static final String LOGOUT = "lgout";
    public static final String LOSE = "lose";
    /* M---------------------------------------------*/
    public static final String MONNEY = "mny";
    public static final String MATCH = "match";
    public static final String MAX_TIMES = "maxtms";
    public static final String MONSTOR = "mons";
    public static final String MAX = "max";
    public static final String MESSAGE = "msg";

    /* N---------------------------------------------*/
    public static final String NAME = "nm";
    public static final String NUM = "num";
    public static final String NEW_LV = "nlv";
    public static final String NOTICE = "notice";
    public static final String NPC = "npc";
    public static final String NEXT = "nxt";


    /* O---------------------------------------------*/
    public static final String OPERATE = "op";
    public static final String OPERATE_SUFFIX = "Op";
    public static final String OPEN_SERVER_DATE = "opdta";
    /* P---------------------------------------------*/
    public static final String PARAM = "parm";
    public static final String PID = "pid";
    public static final String POS = "pos";
    public static final String PRICE = "pric";
    public static final String PRIZES = "przs";
    public static final String PRIZE = "prz";
    public static final String PRIZE_2 = "prz2";
    public static final String PRIZE_2_TYPE = "prz2Typ";
    public static final String PROGRESS = "pgs";
    public static final String PUBLISH = "pub";
    public static final String POINT = "point";


    /* Q---------------------------------------------*/
    public static final String QUALITY = "qua";
    public static final String QUERY = "qry";
    public static final String QUERY_DTAIL = "qrydtl";
    public static final String QQ = "qq";

    /* R---------------------------------------------*/
    public static final String RATIO = "ratio";
    public static final String RESET = "rst";
    public static final String RECORD = "rec";
    public static final String ROBOT = "rbt";
    public static final String ROBOT_ID = "rbtid";
    public static final String RECOMMENDED = "rcmd";
    public static final String RANK = "rk";
    public static final String RESULT = "rult";
    public static final String REMOVE = "rm";
    /* S---------------------------------------------*/
    public static final String SCORE = "sco";
    public static final String SEED = "seed";
    public static final String SELF = "self";
    public static final String SIGN = "sign";
    public static final String SOURCE = "src";

    public static final String SPEED = "spd";
    public static final String STAR_LEVEL = "sLv";
    public static final String STATUS = "sts";
    public static final String STORE = "store";

    public static final String SPT = "spt";

    /* T---------------------------------------------*/
    public static final String TARGET = "tgt";
    public static final String TEAM = "team";
    public static final String TITLE = "ttl";
    public static final String TIME = "time";
    public static final String TYPE = "typ";

    /* U---------------------------------------------*/
    public static final String UPDATE = "upd";
    public static final String UID = "uid";

    /* V---------------------------------------------*/
    public static final String VALIDATE = "vali";
    public static final String VALUE = "val";
    public static final String VIP = "vip";
    public static final String VX = "vx";
    public static final String VIP_EXP = "vipExp";
    public static final String VIP_PRIZE = "vipPrz";


    /* W---------------------------------------------*/
    public static final String WIN = "win";

	/* X---------------------------------------------*/

    public static final String X = "x";
    /* Y---------------------------------------------*/
    public static final String Y = "y";

    /* Z---------------------------------------------*/
    protected final String infoName;
    private final String opName;

    public MsgFactory() {
        infoName = getModulePrefix() + INFO_SUFFIX;
        opName = getModulePrefix() + OPERATE_SUFFIX;
    }

    public abstract String getModulePrefix();

    public String[] intAryTransStrAry(int[] input) {
        String[] attr = new String[input.length];
        for (int i = 0; i < input.length; i++)
            attr[i] = String.valueOf(input[i]);
        return attr;
    }


    public <T> Set<T> gocSetIn(Map parentInfo, Object sign) {
        Set<T> son = (Set<T>) parentInfo.get(sign);
        if (son == null)
            parentInfo.put(sign, son = GameUtil.createSet());
        return son;
    }


    public <T, K> Map<T, K> gocMapIn(Map parentInfo, Object sign) {
        Map<T, K> son = (Map<T, K>) parentInfo.get(sign);
        if (son == null)
            parentInfo.put(sign, son = GameUtil.createSimpleMap());
        return son;
    }

    public <T> List<T> gocLstIn(Map parentInfo, Object sign) {
        List<T> son = (List<T>) parentInfo.get(sign);
        if (son == null)
            parentInfo.put(sign, son = GameUtil.createList());
        return son;
    }

    public Map<String, Object> creIfNull(Map parent) {
        if (null == parent)
            return GameUtil.createSimpleMap();
        return parent;
    }

    public <K, V> Map<K, V> gocOpMap(Map parent) {
        return gocMapIn(parent, opName);
    }

    public <T> List<T> gocOpLst(Map parent) {
        return gocLstIn(parent, opName);
    }

    public <K, V> Map<K, V> gocInfoMap(Map parent) {
        return gocMapIn(parent, infoName);
    }

    public <T> List<T> gocInfoLst(Map parent) {
        return gocLstIn(parent, infoName);
    }

    public Map getPrizeMsg(Map parent, Map prizeMap){
    	Map opInfo = gocOpMap(parent);
    	addPrizeMsg(opInfo, prizeMap);
    	return parent;
    }
    
    
    public Map getPrize2Msg(Map parent, int prizeType, Map prizeMap){
    	Map opInfo = gocOpMap(parent);
    	addPrize2Msg(opInfo, prizeMap);
    	opInfo.put(PRIZE_2_TYPE, prizeType);
    	return parent;
    }
    
    public void addPrizeMsg(Map info, Map prizeMap) {
        List<Map> prizeInfos = gocLstIn(info, PRIZE);
        prizeInfos.add(prizeMap);
    }


    public void addPrizeMsg(Map info, List<Map> prizeMapLst) {
        List<Map> prizeInfos = gocLstIn(info, PRIZE);
        prizeInfos.addAll(prizeMapLst);
    }

    public void addPrize2Msg(Map info, Map prizeMap) {
        List<Map> prizeInfos = gocLstIn(info, PRIZE_2);
        prizeInfos.add(prizeMap);
    }


    public void addPrize2Msg(Map info, List<Map> prizeMapLst) {
        List<Map> prizeInfos = gocLstIn(info, PRIZE_2);
        prizeInfos.addAll(prizeMapLst);
    }

    public int mills2Second_roundUp(long mills) {
        return (int) (mills / DateUtil.SECONDS_MILLIS + 1);
    }

    public void insertPrizeInfo(Map<String, Object> ret, Map<String, Object> prize) {
        if (prize != null && prize.size() > 0)
            gocInfoMap(ret).put(PRIZE, prize);
    }
}

