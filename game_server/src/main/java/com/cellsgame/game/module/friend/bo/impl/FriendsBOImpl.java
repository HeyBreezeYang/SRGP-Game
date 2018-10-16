package com.cellsgame.game.module.friend.bo.impl;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.collection.Filter;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.SYSCons;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.dispatch.CatchRunnable;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.LoadPlayerJobFactory;
import com.cellsgame.game.module.friend.bo.FriendsBO;
import com.cellsgame.game.module.friend.cache.CacheFriendBlessing;
import com.cellsgame.game.module.friend.cache.CacheFriendNames;
import com.cellsgame.game.module.friend.cache.CacheFriendReq;
import com.cellsgame.game.module.friend.cons.CacheDisDataFriend;
import com.cellsgame.game.module.friend.cons.CodeFriend;
import com.cellsgame.game.module.friend.cons.EventTypeFriend;
import com.cellsgame.game.module.friend.cons.FriendRecType;
import com.cellsgame.game.module.friend.csv.FriendBlessCfg;
import com.cellsgame.game.module.friend.ctx.FriendsContext;
import com.cellsgame.game.module.friend.msg.MsgFactoryFriend;
import com.cellsgame.game.module.friend.vo.FriendBlessVO;
import com.cellsgame.game.module.friend.vo.FriendListVO;
import com.cellsgame.game.module.friend.vo.FriendReqVO;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.mail.bo.MailBO;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.msg.CodePlayer;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.sys.SystemBO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.ChainLoadFinisher;
import com.cellsgame.orm.DBObj;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Aly @2017-02-07.
 */
public class FriendsBOImpl implements FriendsBO{
    private PlayerBO playerBO;
    private MailBO mailBO;
    private SystemBO systemBO;
    @Resource
    private BaseDAO<FriendReqVO> friendReqDAO;
    @Resource
    private BaseDAO<FriendListVO> friendListDAO;
    @Resource
    private BaseDAO<FriendBlessVO> friendBlessDAO;

    @Override
    public Map<String, Object> getMyFriendsList(CMD cmd, PlayerVO pvo) throws LogicException {
        Map<String, Object> msg = MsgFactoryFriend.instance().getMyFriendInfo(GameUtil.createSimpleMap(), pvo);
        return MsgUtil.brmAll(msg, cmd.getCmd());
    }

    @Override
    public Map<String, Object> queryFriends(CMD cmd, PlayerVO pvo, String text, boolean filterFriendAndBlackList) throws LogicException {
//        CodeGeneral.General_InvokeParamError.throwIfTrue(text <= 0);
//        Map<String, Object> ret;
//        FriendListVO friendList = pvo.getFriendList();
//        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(text);
//        if (null != baseInfo) {
//            ret = MsgFactoryFriend.instance().insertBaseInfo(GameUtil.createSimpleMap(), baseInfo);
//        } else {
//            List<PlayerInfoVO> vos = CacheFriendNames.friendNameCache.keyStartWith(text, 20,
//                    new Filter<PlayerInfoVO>() {
//                        @Override
//                        public boolean isFilter(PlayerInfoVO playerInfoVO) {
//                            // 过滤已经是好友/黑名单的情况
//                            int pid = playerInfoVO.getPid();
//                            boolean r = pid == pvo.getId();
//                            if (filterFriendAndBlackList && null != friendList) {
//                                r |= friendList.getMyFriends().contains(pid) || friendList.getBlackList().contains(pid);
//                            }
//                            return r;
//                        }
//                    });
//            ret = MsgFactoryFriend.instance().insertBaseInfo(GameUtil.createSimpleMap(), vos);
//        }
//
//        return MsgUtil.brmAll(ret, cmd.getCmd());
        return null;
    }

    @Override
    public Map<String, Object> queryFriendsNear(CMD cmd, PlayerVO pvo) throws LogicException {
        Map<String, Object> msg = MsgFactoryFriend.instance().getMyFriendInfo_near(GameUtil.createSimpleMap(), pvo);
        return MsgUtil.brmAll(msg, cmd.getCmd());
    }

    @Override
    public Map<String, Object> addNewFriend(CMD cmd, PlayerVO pvo, int pid) throws LogicException {
        PlayerInfoVO baseInfo = CachePlayerBase.getBaseInfo(pid);
        CodePlayer.Player_NotFindPlayer.throwIfTrue(null == baseInfo || pvo.getId() == pid);

        FriendListVO friendList = pvo.getFriendList();
        CodeFriend.IN_MY_FRIEND_LIST.throwIfTrue(null != friendList && friendList.getMyFriends().contains(pid));
        CodeFriend.IN_MY_BLACK_LIST.throwIfTrue(null != friendList && friendList.getBlackList().contains(pid));
        CodeFriend.MAX_FRIEND_LIST_SIZE.throwIfTrue(null != friendList
                && friendList.getMyFriends().size() >= CacheDisDataFriend.FRIEND_LIST_SIZE.getData()[0]);

        FriendReqVO reqVO = CacheFriendReq.getSrcOrTgt(pvo.getId(), pid);
        MsgFactoryFriend instance = MsgFactoryFriend.instance();
        if (null == reqVO) {
            PlayerVO player = CachePlayer.getPlayerByPid(pid);
            if (null != player) {
                //在内存中
                FriendListVO list = player.getFriendList();
                if (null != list) {
                    // 如果在黑名单中  自动拒绝
                    CodeFriend.NOT_ALLOWED_ADD.throwIfTrue(list.getBlackList().contains(pvo.getId()));
                }
            }

            reqVO = new FriendReqVO();
            reqVO.setSrc(pvo.getId());
            reqVO.setTgt(pid);
            reqVO.casStatus(FriendReqVO.STATUS_CREATE, FriendReqVO.STATUS_REQUEST_SEND);
            friendReqDAO.save(reqVO);
            CacheFriendReq.add2Cache(reqVO);

            // 看对方是否在线
            if (null != player && player.isOnline()) {
                Map<String, Object> msg = MsgUtil.brmAll(instance.getNewFriendReqMsg(pvo.getId(), reqVO), cmd.getCmd());
                Push.multiThreadSend(player.getMessageController(), msg);
            }
//        } else {
            // 请求已经发送 或者已经是好友
        }
        Map<String, Object> result = GameUtil.createSimpleMap();
        EventTypeFriend.Add.happen(result, cmd, pvo);
        return MsgUtil.brmAll(result, cmd.getCmd());
    }

    @Override
    public Map<String, Object> addNewFriendFeedback(CMD cmd, PlayerVO pvo, int tgtPid, boolean accept) throws LogicException {
        CodeFriend.BAD_REQ.throwIfTrue(CachePlayerBase.getPnameByPid(tgtPid) == null || pvo.getId() == tgtPid);
        // 肯定是对方发送的
        FriendReqVO req = CacheFriendReq.get(tgtPid, pvo.getId());
        CodeFriend.BAD_REQ.throwIfTrue(req == null || req.getSrc() != tgtPid);
        FriendListVO myFlist = pvo.getFriendList();
        // 在我的黑名单中
        if (null != myFlist) {
            CodeFriend.IN_MY_BLACK_LIST.throwIfTrue(myFlist.getBlackList().contains(tgtPid));
        }
        MsgFactoryFriend instance = MsgFactoryFriend.instance();
        PlayerVO tgtPvo = CachePlayer.getPlayerByPid(tgtPid);
        if (accept) { // 只有同意的时候才会检查
            // 验证对方的好友列表是否已满
            int tgtPlayerFriendSize = 0;
            if (null == tgtPvo) {
                Collection<Integer> friendList = CacheFriendReq.getFriendByStatusSrcOrTgt(FriendReqVO.STATUS_ACCEPT, tgtPid);
                if (null != friendList) tgtPlayerFriendSize = friendList.size();
                // 玩家不在内存中无法检查是否在黑名单中
                // 等玩家等级的时候检查
            } else {
                FriendListVO tgtFlist = tgtPvo.getFriendList();
                if (null != tgtFlist) {
                    // 在别人的黑名单中
                    CodeFriend.IN_MY_BLACK_LIST.throwIfTrue(tgtFlist.getBlackList().contains(pvo.getId()));
                    tgtPlayerFriendSize = tgtFlist.getMyFriends().size();
                }
            }
            CodeFriend.MAX_FRIEND_LIST_SIZE_OTHER.throwIfTrue(tgtPlayerFriendSize >= CacheDisDataFriend.FRIEND_LIST_SIZE.getData()[0]);
        }

        boolean suc = CacheFriendReq.statusChg(req, FriendReqVO.STATUS_REQUEST_SEND, accept ? FriendReqVO.STATUS_ACCEPT : FriendReqVO.STATUS_REJECT);
        Map<String, Object> myMsg = null;
        if (suc) {
            if (accept) {
                // 添加好友 到列表  me
                doFriendPid2List(pvo, tgtPid);
                // 添加好友列表 tgt
                if (null != tgtPvo) {
                    doFriendPid2List(tgtPvo, pvo.getId());
                    if (tgtPvo.isOnline()) {
                        // 发送好友添加消息
                        Map<String, Object> msg = instance.getNewFriendMsg(GameUtil.createSimpleMap(), pvo.getId());
                        Push.multiThreadSend(tgtPvo.getMessageController(), MsgUtil.brmAll(msg, cmd.getCmd()));
                    }
                    FriendsContext ctx = FriendsContext.getCtx();
                    mailBO.sendSysMail(tgtPid, ctx.getMailTitle(), ctx.getMailContent().replace("{name}", pvo.getName()), null);
                }

                // 封装消息
                myMsg = instance.getNewFriendMsg(GameUtil.createSimpleMap(), tgtPid);
                myMsg = instance.insertDeleteReq(myMsg, tgtPid);
                friendReqDAO.save(req);
            } else {
                // 拒绝
                // 删除请求
                if (CacheFriendReq.statusChg(req, req.getStatus(), FriendReqVO.STATUS_DELETE, true)) {
                    friendReqDAO.delete(req);
                    myMsg = instance.insertDeleteReq(GameUtil.createSimpleMap(), tgtPid);
                }
            }
        }

        return MsgUtil.brmAll(myMsg, cmd.getCmd());
    }

    private void doFriendPid2List(PlayerVO pvo, int pid) {
        FriendListVO friendList = pvo.getFriendList();
        if (null == friendList) {
            friendList = new FriendListVO();
            pvo.setFriendList(friendList);
            friendList.setPid(pvo.getId());
        }
        friendList.getMyFriends().add(pid);
        friendListDAO.save(friendList);
    }

    @Override
    public Map<String, Object> deleteFriend(CMD cmd, PlayerVO pvo, int pid) throws LogicException {
        FriendReqVO vo = CacheFriendReq.getSrcOrTgt(pvo.getId(), pid);
        if (null != vo) {
            doDeleteFriend(pvo.getFriendList(), pid, vo);
        }
        return MsgUtil.brmAll(MsgFactoryFriend.instance().getDeleteFriendMsg(GameUtil.createSimpleMap(), pid), cmd.getCmd());
    }

    // 删除对方的好友列表
    private void doDeleteFriend(FriendListVO myFriendList, int tgtPid, FriendReqVO vo) {
        int pid = myFriendList.getPid();
        // 删除请求
        if (CacheFriendReq.statusChg(vo, vo.getStatus(), FriendReqVO.STATUS_DELETE, true)) {
            friendReqDAO.delete(vo);
            myFriendList.getMyFriends().remove(tgtPid);
        }
        PlayerVO tgtPvo = CachePlayer.getPlayerByPid(tgtPid);
        if (tgtPvo != null) {
            tgtPvo.getFriendList().getMyFriends().remove(pid);
            // 封装 消息 删除好友
            if (tgtPvo.isOnline()) {
                Map<String, Object> msg = MsgFactoryFriend.instance().getDeleteFriendMsg(GameUtil.createSimpleMap(), pid);
                Push.multiThreadSend(tgtPvo.getMessageController(), MsgUtil.brmAll(msg, Command.FRIEND_DELETE));
            }
        }
    }

    @Override
    public Map<String, Object> addFriend2BlackList(CMD cmd, PlayerVO pvo, int pid) throws LogicException {
        CodePlayer.Player_NotFindPlayer.throwIfTrue(CachePlayerBase.getPnameByPid(pid) == null);
        FriendListVO friendList = pvo.getFriendList();
        if (null == friendList) {
            pvo.setFriendList(friendList = new FriendListVO());
            friendList.setPid(pvo.getId());
        } else {
            CodeFriend.MAX_FRIEND_LIST_SIZE.throwIfTrue(friendList.getBlackList().size() >= CacheDisDataFriend.BLACK_LIST_SIZE.getData()[0]);
        }
        boolean oldId = friendList.getMyFriends().remove(pid);
        Map<String, Object> ret = null;
        if (oldId) {
            // 如果是好友
            FriendReqVO reqVO = CacheFriendReq.getSrcOrTgt(pvo.getId(), pid);
            if (null != reqVO) {
                doDeleteFriend(friendList, pid, reqVO);
                ret = MsgFactoryFriend.instance().getDeleteFriendMsg(GameUtil.createSimpleMap(), pid);
            }
        }
        friendList.getNearCharPlayer().remove((Object)pid);
        friendList.getBlackList().add(pid);
        friendListDAO.save(friendList);
        return MsgUtil.brmAll(MsgFactoryFriend.instance().getNewBlackList(ret, pid), cmd.getCmd());
    }

    @Override
    public Map<String, Object> addRemoveFromBlackList(CMD cmd, PlayerVO pvo, int pid) throws LogicException {
        FriendListVO friendList = pvo.getFriendList();
        if (null != friendList) {
            friendList.getBlackList().remove(pid);
            friendListDAO.save(friendList);
        }
        return MsgUtil.brmAll(MsgFactoryFriend.instance().getDeleteBlackMsg(pid), cmd.getCmd());
    }

    @Override
    public Map<String, Object> getRecommendedFriend(CMD cmd, PlayerVO pvo) throws LogicException {
//        int level = pvo.getLevel();
        Multimap<Integer, PlayerInfoVO> levels = CachePlayerBase.levels();
        ArrayList<PlayerInfoVO> infos = new ArrayList<>();
        FriendListVO friendList = pvo.getFriendList();
        int[] range = CacheDisDataFriend.RECOMMENDED_RANGE.getData();
        for (int i = range[0]; i <= range[1]; i++) {
//            int curLV = level + i;
//            if (curLV <= 0) continue;
//            Collection<PlayerInfoVO> vos = levels.get(curLV);
//            if (vos != null) {
//                for (PlayerInfoVO vo : vos) {
//                    if (!(vo.getPid() == pvo.getId())
//                            ||
//                            null != friendList && (friendList.getBlackList().contains(vo.getPid())
//                                    || friendList.getMyFriends().contains(vo.getPid()))) {
//                        infos.add(vo);
//                    }
//                }
//            }
        }
        List<PlayerInfoVO> recommended;
        int recommendedSize = CacheDisDataFriend.RECOMMENDED_SIZE.getData()[0];
        if (recommendedSize >= infos.size()) {
            recommended = infos;
        } else {
            recommended = new ArrayList<>();
            int allSize = infos.size();
            for (int i = 0; i < recommendedSize; i++) {
                PlayerInfoVO remove = infos.remove(GameUtil.r.nextInt(allSize - i));
                if (null != remove) recommended.add(remove);
            }
        }
        return MsgUtil.brmAll(MsgFactoryFriend.instance().getRecommendedFriendInfo(recommended), cmd.getCmd());
    }

    @Override
    public Map<String, Object> fight(CMD cmd, PlayerVO pvo, int pid) throws LogicException {
        CodePlayer.Player_NotFindPlayer.throwIfTrue(CachePlayerBase.getPnameByPid(pid) == null || pvo.getId() == pid);
        PlayerVO tgtPlayer = CachePlayer.getPlayerByPid(pid);
        if (null == tgtPlayer) {
            LoadPlayerJobFactory.loadByPlayerId(pid, new ChainLoadFinisher() {
                @Override
                public void finishLoad(Map<String, Object> data) {
                    Dispatch.dispatchGameLogic(new CatchRunnable(pvo.getMessageController(), cmd) {
                        @Override
                        protected Map<String, Object> runLogic() throws LogicException {
                            playerBO.load(data, cmd);
                            // 通知数据加载成功
                            return MsgUtil.brmAll(cmd);
                        }
                    });
                }
            });
        } else {
            // 通知数据加载成功
            return MsgUtil.brmAll(cmd.getCmd());
        }
        return null;
    }

    /**
     * 获取 祝福数据   没有就创建
     */
    @Override
    public Map<String, Object> getBlessDATA(CMD cmd, PlayerVO pvo) throws LogicException {
        FriendBlessVO blessVO = getFriendBlessVO(pvo.getId());
        FriendListVO friendList = pvo.getFriendList();
        Map<String, Object> ret = GameUtil.createSimpleMap();

        putCanBlessFriendMsg(ret, friendList);
        ret = MsgFactoryFriend.instance().getFriendBlessData(ret, blessVO);
        return MsgUtil.brmAll(ret, cmd);
    }

    private void putCanBlessFriendMsg(Map<String, Object> ret, FriendListVO friendList) {
        // 推送好友数据
        List<Integer> canBlessFriend = GameUtil.createList();
        if (null != friendList) {
            int maxBlessedNum = CacheDisDataFriend.BLESSED_NUM.first();
            for (int pid : friendList.getMyFriends()) {
                if (getFriendBlessVO(pid).getBlessedNum() < maxBlessedNum) {
                    canBlessFriend.add(pid);
                }
            }
        }
        MsgFactoryFriend.instance().getCanBlessedFriend(ret, canBlessFriend, new BiConsumer<Integer, Map<String, Object>>() {
            @Override
            public void accept(Integer pid, Map<String, Object> msg) {
                FriendBlessVO vo = getFriendBlessVO(pid);
                msg.put(MsgFactoryFriend.BLESSED_NUM, vo.getBlessedNum());
            }
        });
    }

    private FriendBlessVO getFriendBlessVO(int pvoID) {
        return CacheFriendBlessing.get(pvoID, new Function<Integer, FriendBlessVO>() {
            @Override
            public FriendBlessVO apply(Integer pid) {
                FriendBlessVO vo = new FriendBlessVO();
                vo.setPid(pid);
                friendBlessDAO.save(vo);
                return vo;
            }
        });
    }

    /**
     * 祝福好友
     */
    @Override
    public Map<String, Object> blessFriend(CMD cmd, PlayerVO pvo, int[] pids) throws LogicException {
        CodeGeneral.General_InvokeParamError.throwIfTrue(null == pids || pids.length == 0);
        FriendListVO friendList = pvo.getFriendList();
        CodeFriend.NOT_FRIEND.throwIfTrue(null == friendList || friendList.getMyFriends().size() == 0);
        Set<Integer> myFriends = friendList.getMyFriends();
        FriendBlessVO myBlessVO = getFriendBlessVO(pvo.getId());
        int maxBless = CacheDisDataFriend.BLESSING_NUM.first() - myBlessVO.getBlessNum();
        CodeFriend.Max_BLESS_NUM.throwIfTrue(maxBless <= 0);

        int maxBlessedNum = CacheDisDataFriend.BLESSED_NUM.first();
        int blessedNum = 0;
        for (int pid : pids) {
            if (blessedNum >= maxBless) break;
            // 检查玩家是否存在
            if (myFriends.contains(pid)) {
                FriendBlessVO vo = getFriendBlessVO(pid);
                if (vo.getBlessedNum() < maxBlessedNum) {
                    vo.setBlessedNum(vo.getBlessedNum() + 1);
                    friendBlessDAO.save(vo);
                    blessedNum++;
                }
            }
        }

        // 设置
        myBlessVO.setBlessNum(blessedNum + myBlessVO.getBlessNum());
        friendBlessDAO.save(myBlessVO);
        // 奖励
        Map<String, Object> ret = GameUtil.createSimpleMap();
        Map<String, Object> prize = GameUtil.createSimpleMap();
//        FriendBlessCfg cfg = FriendBlessCfg.get(pvo.getLevel());
//        if (blessedNum > 0 && cfg != null) {
//            FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
//            List<FuncConfig> blessPrize = cfg.getBlessPrize();
//            if (null != blessPrize && blessPrize.size() > 0) {
//                for (FuncConfig config : blessPrize) {
//                    executor.addSyncFunc(config, blessedNum);
//                }
//            }
//            executor.exec(ret, prize, pvo);
//        }
        MsgFactoryFriend.instance().insertPrizeInfo(ret, prize);
        putCanBlessFriendMsg(ret, pvo.getFriendList());
        return MsgUtil.brmAll(MsgFactoryFriend.instance().getFriendBlessData(ret, myBlessVO), cmd);

    }

    @Override
    public Map<String, Object> blessGetPrize(CMD cmd, PlayerVO pvo) throws LogicException {
        // 领奖时间
        CodeFriend.BLESS_NOT_AT_GET_PRIZE_TIME.throwIfTrue(LocalTime.now().getHour() < CacheDisDataFriend.BLESS_GET_PRIZE_TIME.first());
        FriendBlessVO myBlessVO = getFriendBlessVO(pvo.getId());
        // 奖励次数
        CodeFriend.Max_BLESS_GET_PRIZE_TIMES.throwIfTrue(myBlessVO.getPrizeNum() >= CacheDisDataFriend.BLESS_PRIZE_NUM.first());
        // 领奖时间冷却中
        long cd = CacheDisDataFriend.BLESS_GET_PRIZE_INTERVAL.first() * DateUtil.MIN_MILLIS;
        long cur = System.currentTimeMillis();
        CodeFriend.BLESS_GET_PRIZE_IN_CD.throwIfTrue(myBlessVO.getLastPrizeTime() + cd > cur);

//        FriendBlessCfg cfg = FriendBlessCfg.get(pvo.getLevel());
        FriendBlessCfg cfg=null;
        Map<String, Object> ret = GameUtil.createSimpleMap();
        Map<String, Object> prize = GameUtil.createSimpleMap();
        if (null != cfg) {
            List<FuncConfig> blessedPrize = cfg.getBlessedPrize(myBlessVO.getBlessedNum());
            if (null != blessedPrize) {
                FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
                executor.addSyncFunc(blessedPrize);
                executor.exec(ret, prize, pvo);
                MsgFactoryFriend.instance().insertPrizeInfo(ret, prize);
            }
        }
        myBlessVO.setPrizeNum(myBlessVO.getPrizeNum() + 1);
        myBlessVO.setLastPrizeTime(cur);
        friendBlessDAO.save(myBlessVO);
        return MsgUtil.brmAll(MsgFactoryFriend.instance().getFriendBlessData(ret, myBlessVO), cmd);
    }

    @Override
    public void load() {
        List<DBObj> all = friendReqDAO.getAll();
        if (null != all && all.size() > 0) {
            for (DBObj dbObj : all) {
                FriendReqVO vo = new FriendReqVO();
                vo.readFromDBObj(dbObj);
                // 删除状态 跟拒绝状态 都无用了
                if (vo.getStatus() == FriendReqVO.STATUS_DELETE
                        || vo.getStatus() == FriendReqVO.STATUS_REJECT) {
                    friendReqDAO.delete(vo);
                } else
                    CacheFriendReq.add2Cache(vo);
            }
        }

        List<DBObj> allBless = friendBlessDAO.getAll();
        if (allBless != null && allBless.size() > 0) {
            for (DBObj bless : allBless) {
                FriendBlessVO vo = new FriendBlessVO();
                vo.readFromDBObj(bless);
                CacheFriendBlessing.addCache(vo);
            }
        }
        // 系统刷新好友祝福数据
        sysResetData(System.currentTimeMillis());
    }

    @Override
    public BaseDAO<FriendReqVO> getFriendReqDAO() {
        return friendReqDAO;
    }
    ///////////////////////////////战斗相关////////////////////////////////

    

    @Override
    public void sysResetData(long cur) {
        long recordTime = systemBO.getRecordTime(FriendRecType.FRIEND_BLESS_SYS_REST_TIME);
        if (SYSCons.notSameDate(cur, recordTime)) {
            systemBO.recordTime(FriendRecType.FRIEND_BLESS_SYS_REST_TIME, cur);
            CacheFriendBlessing.forEach(new BiConsumer<Integer, FriendBlessVO>() {
                @Override
                public void accept(Integer s, FriendBlessVO vo) {
                    sysResetData(vo, cur);
                }
            });
        }
    }

    @Override
    public void sysResetData(FriendBlessVO vo, long cur) {
        if (null == vo) return;
        if (SYSCons.notSameDate(vo.getLastRestTime(), cur)) {
            vo.setLastRestTime(cur);
            vo.setLastPrizeTime(0);
            vo.setPrizeNum(0);
            vo.setBlessNum(0);
            vo.setBlessedNum(0);
            friendBlessDAO.save(vo);
        }
    }

    private void saveAll(PlayerVO pvo) {
        if (pvo.getFriendList() != null)
            friendListDAO.save(pvo.getFriendList());
    }

    public void setPlayerBO(PlayerBO playerBO) {
        this.playerBO = playerBO;
    }

    public void setMailBO(MailBO mailBO) {
        this.mailBO = mailBO;
    }

    public void setSystemBO(SystemBO systemBO) {
        this.systemBO = systemBO;
    }
    
    
    @Override
    public EvtType[] getListenTypes() {
        return new EvtType[]{EvtTypePlayer.Offline, EvtTypePlayer.EnterGame};
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        Enum<?> type = event.getType();
        if (type == EvtTypePlayer.Offline) {
            // 创建阵型数据
            PlayerVO pvo = (PlayerVO) holder;
            saveAll(pvo);
        } else if (EvtTypePlayer.EnterGame == type) {
            PlayerVO pvo = (PlayerVO) holder;
            FriendBlessVO vo = CacheFriendBlessing.get(pvo.getId(), s -> null);
            if (null != vo)
                sysResetData(vo, System.currentTimeMillis());
        }
        return parent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
        List<DBObj> dbObjs = (List<DBObj>) data.get(DATA_SIGN_FRIEND_LIST);
        if (null != dbObjs && dbObjs.size() > 0) {
            FriendListVO vo = new FriendListVO();
            vo.readFromDBObj(dbObjs.get(0));
            player.setFriendList(vo);
            Collection<Integer> list = CacheFriendReq.getFriendByStatusSrcOrTgt(FriendReqVO.STATUS_ACCEPT, player.getId());
            Collection<Integer> reqList = CacheFriendReq.getFriendByStatusSrcOrTgt(FriendReqVO.STATUS_REQUEST_SEND, player.getId());
            checkAndRemoveReq(player, vo, reqList, false);
            // 移除在某些情况下造成的 黑名单/好友同时存在情况
            checkAndRemoveReq(player, vo, list, true);
            if (null != list) {
                vo.getMyFriends().addAll(list);
            }
        }
    }

    private void checkAndRemoveReq(PlayerVO player, FriendListVO vo, Collection<Integer> reqList, boolean remove) {
        if (reqList != null && reqList.size() > 0) {
            Iterator<Integer> iterator = reqList.iterator();
            while (iterator.hasNext()) {
                int pid = iterator.next();
                if (vo.getBlackList().contains(pid)) {
                    FriendReqVO rvo = CacheFriendReq.getSrcOrTgt(pid, player.getId());
                    if (null == rvo) continue;
                    if (CacheFriendReq.statusChg(rvo, rvo.getStatus(), FriendReqVO.STATUS_DELETE, true)) {
                       friendReqDAO.delete(rvo);
                    }
                    if (remove)
                        iterator.remove();
                }
            }
        }
    }

	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
	}
}
