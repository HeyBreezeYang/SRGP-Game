package com.cellsgame.game.module.friend.msg;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.friend.cache.CacheFriendReq;
import com.cellsgame.game.module.friend.vo.FriendBlessVO;
import com.cellsgame.game.module.friend.vo.FriendListVO;
import com.cellsgame.game.module.friend.vo.FriendReqVO;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Aly @2017-02-07.
 */
public class MsgFactoryFriend extends MsgFactory {
    private static final String friend = "friend";
    private static final String QueryList = "qlist";        // 查询列表
    private static final String BlackList = "blist";        // 屏蔽列表
    private static final String NEAR_CHAT = "near";         // 最近联系
    private static final String ApplyList = "apply";        // 被请求列表
    private static final String NewReq = "nreq";            // 新好友请求
    private static final String DeleteReq = "dreq";         // 删除好友请求
    private static final String NewBlack = "nblack";        // 新黑名单请求
    private static final String DeleteBlack = "dblack";        // 新黑名单请求


    private static final String BLESS = "bless";            // 祝福
    private static final String BLESS_FRIEND = "blfrd";     // 可以祝福的好友
    private static final String BLESS_NUM = "bnum";         // 祝福次数
    public static final String BLESSED_NUM = "bednum";     // 被祝福次数
    private static final String LAST_PRIZE_TIME = "ptime";  // 上次领奖时间
    private static final String PRIZE_TIMES = "pnum";       // 领奖次数

    private static MsgFactoryFriend instance = new MsgFactoryFriend();

    public static MsgFactoryFriend instance() {
        return instance;
    }

    public Map<String, Object> insertBaseInfo(Map<String, Object> parent, PlayerInfoVO baseInfo) {
        parent = creIfNull(parent);
        Map<String, Object> info = gocInfoMap(parent);
        List<Map<String, Object>> qlist = gocLstIn(info, QueryList);
        if (baseInfo != null) {
            qlist.add(toFriendsInfo(baseInfo));
        }
        return parent;
    }

    public Map<String, Object> insertBaseInfo(Map<String, Object> parent, List<PlayerInfoVO> baseInfos) {
        parent = creIfNull(parent);
        Map<String, Object> info = gocInfoMap(parent);
        List<Map<String, Object>> qlist = gocLstIn(info, QueryList);
        if (baseInfos != null) {
            for (PlayerInfoVO baseInfo : baseInfos) {
                qlist.add(toFriendsInfo(baseInfo));
            }
        }
        return parent;
    }

    private Map<String, Object> toFriendsInfo(Map<String, Object> info, PlayerInfoVO baseInfo) {
        info.put(NAME, baseInfo.getName());
        info.put(LEVEL, baseInfo.getPlv());
        info.put(ID, baseInfo.getPid());
        info.put(FIGHT_FORCE, baseInfo.getFightForce());
        info.put(IMG, baseInfo.getImage());
        info.put(GUILD, baseInfo.getGuildName());
        info.put(LOGIN, DateUtil.getSecond(baseInfo.getLastLoginTime()));
        info.put(LOGOUT, DateUtil.getSecond(baseInfo.getLastLogOutTime()));
        return info;
    }

    private Map<String, Object> toFriendsInfo(PlayerInfoVO baseInfo) {
        return toFriendsInfo(new HashMap<>(), baseInfo);
    }

    @Override
    public String getModulePrefix() {
        return friend;
    }

    public Map<String, Object> getNewFriendReqMsg(int tgtPid, FriendReqVO reqVO) {
        Map<String, Object> ret = GameUtil.createSimpleMap();
        Map<String, Object> info = gocOpMap(ret);
        Map<String, Object> map = gocMapIn(info, NewReq);
        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(tgtPid);
        if (null != baseInfo) {
            toFriendsInfo(map, baseInfo);
            map.put(STATUS, reqVO.getStatus());
            map.put(TIME, DateUtil.getSecond(reqVO.getModifyTime()));
        }
        return ret;
    }

    public Map<String, Object> getDeleteFriendMsg(Map<String, Object> map, int id) {
        Map<String, Object> opMap = gocOpMap(map);
        opMap.put(DELETE, id);
        return map;
    }

    public Map<String, Object> getNewFriendMsg(Map<String, Object> map, int tgtPid) {
        Map<String, Object> opMap = gocOpMap(map);
        Map<String, Object> add = gocMapIn(opMap, ADD);
        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(tgtPid);
        if (null != baseInfo) {
            toFriendsInfo(add, baseInfo);
        }
        return map;
    }

    public Map<String, Object> getMyFriendInfo(Map<String, Object> ret, PlayerVO pvo) {
        FriendListVO fvo = pvo.getFriendList();
        Map<String, Object> info = gocInfoMap(ret);
        List<Map<String, Object>> reqList = gocLstIn(info, ApplyList);
        Collection<Integer> reqMeList = CacheFriendReq.getFriendByStatusTgt(FriendReqVO.STATUS_REQUEST_SEND, pvo.getId());
        // 请求列表
        pids2InfoMap(reqList, reqMeList);
        if (null != fvo) {
            List<Map<String, Object>> flist = gocLstIn(info, friend);
            List<Map<String, Object>> blackList = gocLstIn(info, BlackList);
            List<Map<String, Object>> nearChat = gocLstIn(info, NEAR_CHAT);
            // 好友
            pids2InfoMap(flist, fvo.getMyFriends());
            // 黑名单
            pids2InfoMap(blackList, fvo.getBlackList());
            // 最近联系
            pids2InfoMap(nearChat, fvo.getNearCharPlayer());
        }
        return ret;
    }

    public Map<String, Object> getMyFriendInfo_near(Map<String, Object> ret, PlayerVO pvo) {
        FriendListVO fvo = pvo.getFriendList();
        Map<String, Object> info = gocInfoMap(ret);
        if (null != fvo) {
            List<Map<String, Object>> nearChat = gocLstIn(info, NEAR_CHAT);
            // 最近联系
            pids2InfoMap(nearChat, fvo.getNearCharPlayer());
        }
        return ret;
    }

    private void pids2InfoMap(List<Map<String, Object>> infoMapList, Collection<Integer> pids) {
        if (null == pids || pids.size() == 0)
            return;
        for (Integer pid : pids) {
            PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(pid);
            if (null != baseInfo) {
                infoMapList.add(toFriendsInfo(baseInfo));
            }
        }
    }

    public Map<String, Object> getNewBlackList(Map<String, Object> parent, int pid) {
        parent = creIfNull(parent);
        Map<String, Object> op = gocOpMap(parent);
        Map<String, Object> map = gocMapIn(op, NewBlack);
        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(pid);
        if (null != baseInfo)
            toFriendsInfo(map, baseInfo);
        return parent;
    }

    public Map<String, Object> getDeleteBlackMsg(int pid) {
        Map<String, Object> ret = GameUtil.createSimpleMap();
        Map<String, Object> op = gocOpMap(ret);
        op.put(DeleteBlack, pid);
        return ret;
    }

    public Map<String, Object> getRecommendedFriendInfo(List<PlayerInfoVO> recommended) {
        Map<String, Object> ret = gocInfoMap(GameUtil.createSimpleMap());
        Map<String, Object> info = gocInfoMap(ret);
        List<Map<String, Object>> list = gocLstIn(info, RECOMMENDED);
        for (PlayerInfoVO vo : recommended) {
            list.add(toFriendsInfo(GameUtil.createSimpleMap(), vo));
        }
        return ret;
    }

    public Map<String, Object> insertDeleteReq(Map<String, Object> ret, int pid) {
        Map<String, Object> op = gocOpMap(ret);
        op.put(DeleteReq, pid);
        return ret;
    }

    public Map<String, Object> getFriendBlessData(Map<String, Object> ret, FriendBlessVO blessVO) {
        ret = creIfNull(ret);
        Map<Object, Object> bless = gocMapIn(gocInfoMap(ret), BLESS);
        bless.put(BLESS_NUM, blessVO.getBlessNum());
        bless.put(BLESSED_NUM, blessVO.getBlessedNum());
        bless.put(LAST_PRIZE_TIME, mills2Second_roundUp(blessVO.getLastPrizeTime()));
        bless.put(PRIZE_TIMES, blessVO.getPrizeNum());
        return ret;
    }

    public void getCanBlessedFriend(Map<String, Object> ret, List<Integer> canBlessFriend, BiConsumer<Integer, Map<String, Object>> msg) {
        List<Map<String, Object>> map = gocLstIn(gocInfoMap(ret), BLESS_FRIEND);
        for (int pid : canBlessFriend) {
            PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(pid);
            if (null != baseInfo) {
                Map<String, Object> info = toFriendsInfo(GameUtil.createSimpleMap(), baseInfo);
                msg.accept(pid, info);
                map.add(info);
            }
        }
    }
}
