package com.cellsgame.game.module.guild.msg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.guild.bo.impl.GuildBOImpl;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.cons.GuildBossState;
import com.cellsgame.game.module.guild.vo.*;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.msg.MsgFactoryPlayer;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import static com.cellsgame.common.util.DateUtil.getSecond;

/**
 * @author Aly on  2016-08-15.
 */
public class MsgFactoryGuild extends MsgFactory {
    public static final String GUILD_REQ_ED = "reqed";
    public static final String QUERY_GUILD = "qryGuild";
    private static final String REQ_LIST = "reqlist";
    private static final String DIS_TIME = "disT";
    private static final String RIGHT = "right";
    private static final String MEMBER = "mem";
    private static final String MEMBER_ADD = "memAd";
    private static final String MEMBER_UPDATE = "memUpd";
    private static final String MEMBER_DELETE = "memDel";
    private static final String MEMBER_NUMER = "memSize";
    private static final String QUERY_MEMBER = "qrymem";
    private static final String NEED_REQ = "needReq";           // 是否需要申请
    private static final String LOG_S = "logs";                 // 日志
    private static final String HISTORY_GUILD_COIN = "hisCoin";          // 历史贡献
    private static final String DONATE_CID = "dCid";
    private static final String BOSS = "boss";
    private static final String BOSS_UPDATE = "bossUpd";
    private static final String RANK = "rank";

    private static final MsgFactoryGuild instance = new MsgFactoryGuild();

    public static MsgFactoryGuild instance() {
        return instance;
    }

    public Map toGuildInfo(Map parent, GuildVO vo) {
        parent = creIfNull(parent);
        Map fam = gocInfoMap(parent);
        Map<String, Object> info = gocMapIn(fam, INFO);
        info.put(LEVEL, vo.getLevel());
        info.put(MONNEY, vo.getMny());
        info.put(EXP, vo.getExp());
        info.put(NOTICE, vo.getNotice());
        if (vo.getStatus() == GuildVO.IN_DISSOLUTION) {
            info.put(DIS_TIME, getSecond(vo.getDissolutionTime()));
        }
        toBaseInfoMap(info, vo);
        return parent;
    }

    public Map guildUpdateMsg(Map parent, GuildVO vo) {
        parent = creIfNull(parent);
        Map fam = gocOpMap(parent);
        Map<String, Object> info = gocMapIn(fam, INFO);
        info.put(LEVEL, vo.getLevel());
        info.put(MONNEY, vo.getMny());
        info.put(EXP, vo.getExp());
        info.put(NOTICE, vo.getNotice());
        if (vo.getStatus() == GuildVO.IN_DISSOLUTION) {
            info.put(DIS_TIME, getSecond(vo.getDissolutionTime()));
        }
        toBaseInfoMap(info, vo);
        return parent;
    }

    public static final String ReqState = "reqsts";
    public static final String ReqGuild = "reqGld";

    public Map guildJoinStateMsg(Map parent, GuildVO vo, boolean result) {
        parent = creIfNull(parent);
        Map fam = gocOpMap(parent);
        fam.put(ReqState, result);
        fam.put(ReqGuild, vo.getName());
        return parent;
    }

    public Map getGuildMoneyUpdateMsg(Map parent, GuildVO vo) {
        parent = creIfNull(parent);
        Map opMap = gocOpMap(parent);
        opMap.put(MONNEY, String.valueOf(vo.getMny()));
        return parent;
    }

    public Map toFullInfo(Map parent, GuildVO vo, GuildMemberVO memberVO) {
        parent = creIfNull(parent);
        memberBaseInfo(parent, vo, memberVO);
        toGuildInfo(parent, vo);
        return parent;
    }

    public Map memberBaseInfo(Map parent, PlayerVO pvo) {
        GuildVO guild = pvo.getGuild();
        return memberBaseInfo(parent, guild, CacheGuild.getGuildMemberVO(pvo.getId()));
    }

    public Map memberBaseInfo(Map parent, GuildVO vo, GuildMemberVO memberVO) {
        Map fam = gocInfoMap(parent);
        Map<String, Object> info = gocMapIn(fam, MEMBER);
        if (memberVO != null) {
            Integer rightLevel = vo == null ? null : vo.getMemberRights().get(memberVO.getPid());
            memberBaseInfo(info, memberVO, rightLevel);
        }
        return parent;
    }

    public Map memberAddMsg(Map parent, GuildVO vo, GuildMemberVO memberVO) {
        Map fam = gocOpMap(parent);
        Map<String, Object> info = gocMapIn(fam, MEMBER_ADD);
        if (memberVO != null) {
            Integer rightLevel = vo == null ? null : vo.getMemberRights().get(memberVO.getPid());
            memberBaseInfo(info, memberVO, rightLevel);
        }
        return parent;
    }

    public Map memberUpdateMsg(Map parent, GuildVO vo, GuildMemberVO memberVO) {
        Map fam = gocOpMap(parent);
        Map<String, Object> info = gocMapIn(fam, MEMBER_UPDATE);
        if (memberVO != null) {
            Integer rightLevel = vo == null ? null : vo.getMemberRights().get(memberVO.getPid());
            memberBaseInfo(info, memberVO, rightLevel);
        }
        return parent;
    }

    public Map memberDeleteMsg(Map parent, GuildMemberVO memberVO) {
        Map fam = gocOpMap(parent);
        List<Integer> info = gocLstIn(fam, MEMBER_DELETE);
        info.add(memberVO.getPid());
        return parent;
    }
    public static final String WORKER = "useWrk";

    public void memberBaseInfo(Map<String, Object> info, GuildMemberVO memberVO, Integer rightLevel) {
        if (null == info) info = new HashMap<>(3);
        info.put(JOIN, getSecond(memberVO.getJoinGuildTime()));
        if (null != rightLevel)
            info.put(RIGHT, rightLevel);
        info.put(HISTORY_GUILD_COIN, String.valueOf(memberVO.getHistoryGuildCoin()));
        info.put(DONATE_CID, memberVO.getDonateCid());
        info.put(WORKER, memberVO.getUsedWorker());
    }

    public void memberUpdateMsg(Map<String, Object> parent, GuildMemberVO memberVO) {
        Map op = gocOpMap(parent);
        Map<String, Object> info = gocMapIn(op, MEMBER);
        info.put(HISTORY_GUILD_COIN, String.valueOf(memberVO.getHistoryGuildCoin()));
        info.put(DONATE_CID, memberVO.getDonateCid());
        info.put(WORKER, memberVO.getUsedWorker());
    }

    public Map<String, Object> toMemberInfoFull(int pid, Integer rightLevel) {
        GuildMemberVO memberVO = CacheGuild.getGuildMemberVO(pid);
        if (memberVO == null)
            return null;
        Map<String, Object> info = new HashMap<>(10);
        info.put(JOIN, getSecond(memberVO.getJoinGuildTime()));
        if (null != rightLevel)
            info.put(RIGHT, rightLevel);
        info.put(HISTORY_GUILD_COIN, String.valueOf(memberVO.getHistoryGuildCoin()));
        info.put(DONATE_CID, memberVO.getDonateCid());
        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(pid);
        if (null != baseInfo) {
            info = MsgFactoryPlayer.instance().getBaseInfo(info, baseInfo);
        }
        return info;
    }

    private static final String OWNER = "owner";

    public Map<String, Object> toBaseInfoMap(Map<String, Object> infoMap, GuildVO vo) {
        if (null == vo) return infoMap;
        if (null == infoMap) infoMap = new HashMap<>(9);
        infoMap.put(QQ, vo.getQq());
        infoMap.put(VX, vo.getVx());
        infoMap.put(NAME, vo.getName());
        infoMap.put(GUILD_ID, vo.getId());
        infoMap.put(LEVEL, vo.getLevel());
        infoMap.put(MONNEY, vo.getMny());
        infoMap.put(FIGHT_FORCE, String.valueOf(vo.getFightForce()));
        infoMap.put(MEMBER_NUMER, vo.getMemberSize());
        infoMap.put(MAX, GuildBOImpl.getMaxMemberSize(vo));
        infoMap.put(DESC, vo.getDesc());
        infoMap.put(NEED_REQ, vo.isEnterNeedReq());
        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(vo.getOwner());
        infoMap.put(OWNER, baseInfo == null ? "" : baseInfo.getName());
        return infoMap;
    }

    @Override
    public String getModulePrefix() {
        return GUILD;
    }

    public Map<String, Object> getAllMemberInfo(GuildVO guildVO) {
        Map<Integer, Integer> rightCopy = new HashMap<>();
        rightCopy.putAll(guildVO.getMemberRights());
        Map<String, Object> parent = GameUtil.createSimpleMap();
        Map<String, Object> info = gocInfoMap(parent);
        List<Map> list = gocLstIn(info, QUERY_MEMBER);
        for (Map.Entry<Integer, Integer> entry : rightCopy.entrySet()) {
            int pid = entry.getKey();
            Integer rightLevel = entry.getValue();
            Map<String, Object> map = toMemberInfoFull(pid, rightLevel);
            if (null != map) list.add(map);
        }
        return parent;
    }

    public Map<String, Object> getReqList(Map<Integer, GuildReqVO> list) {
        Map<String, Object> parent = GameUtil.createSimpleMap();
        Map<Object, Object> info = gocInfoMap(parent);
        List<Object> ret = gocLstIn(info, REQ_LIST);
        for (Map.Entry<Integer, GuildReqVO> en : list.entrySet()) {
            if (en.getValue().getStatus() != GuildReqVO.STATUS_NORMAL) continue;
            PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(en.getKey());
            if (null != baseInfo) {
                Map<String, Object> res = GameUtil.createSimpleMap();
                ret.add(res);
                MsgFactoryPlayer.instance().getBaseInfo(res, baseInfo);
                res.put(TIME, en.getValue().getTime());
            }
        }
        return parent;
    }

    public Map getGuildRightChgMsg(Integer tgtRight) {
        Map<Object, Object> ret = GameUtil.createSimpleMap();
        Map<Object, Object> opMap = gocOpMap(ret);
        opMap.put(RIGHT, tgtRight);
        return ret;
    }

    public Map<String, Object> getGuildLog(List<GuildLogVO> logs) {
        Map<String, Object> ret = GameUtil.createSimpleMap();
        Map<Object, Object> info = gocInfoMap(ret);
        List<Map<String, Object>> list = gocLstIn(info, LOG_S);
        if (logs != null && logs.size() > 0) {
            for (GuildLogVO log : logs) {
                Map<String, Object> mp = GameUtil.createSimpleMap();
                list.add(mp);
                mp.put(ID, log.getLogID());
                mp.put(PARAM, log.getParam());
                mp.put(TIME, DateUtil.getSecond(log.getCreateDate().getTime()));
            }
        }
        return ret;
    }


    private static final String CURR_LF = "currLf";


    private static final String KILL_PLAYER = "killPly";



    private static final String HURT = "hurt";


}
