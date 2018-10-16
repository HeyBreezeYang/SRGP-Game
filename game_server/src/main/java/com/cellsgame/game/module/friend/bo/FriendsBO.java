package com.cellsgame.game.module.friend.bo;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.friend.vo.FriendBlessVO;
import com.cellsgame.game.module.friend.vo.FriendReqVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;

import java.util.Map;

/**
 * @author Aly @2017-02-07.
 * 好友
 */
@AModule(ModuleID.Friends)
public interface FriendsBO extends IBuildData, StaticEvtListener{
    /**
     * 获取好友列表
     */
    @Client(Command.FRIEND_GET_MY_FRIEND)
    Map<String, Object> getMyFriendsList(CMD cmd, PlayerVO pvo) throws LogicException;

    /**
     * 查询好友
     */
    @Client(Command.FRIEND_QUERY)
    Map<String, Object> queryFriends(CMD cmd, PlayerVO pvo, @CParam("text") String text, @CParam("sp") boolean filterFriendAndBlackList) throws LogicException;

    /**
     * 查询好友
     */
    @Client(Command.FRIEND_QUERY_NEAR)
    Map<String, Object> queryFriendsNear(CMD cmd, PlayerVO pvo) throws LogicException;

    /**
     * 添加好友
     */
    @Client(Command.FRIEND_ADD_NEW)
    Map<String, Object> addNewFriend(CMD cmd, PlayerVO pvo, @CParam("pid") int pid) throws LogicException;

    /**
     * 同意/拒绝请求
     */
    @Client(Command.FRIEND_ADD_NEW_FEED_BACK)
    Map<String, Object> addNewFriendFeedback(CMD cmd, PlayerVO pvo, @CParam("pid") int pid, @CParam("accept") boolean accept) throws LogicException;

    /**
     * 删除好友
     */
    @Client(Command.FRIEND_DELETE)
    Map<String, Object> deleteFriend(CMD cmd, PlayerVO pvo, @CParam("pid") int pid) throws LogicException;

    /**
     * 添加到黑名单
     */
    @Client(Command.FRIEND_ADD_2_BLACK_LIST)
    Map<String, Object> addFriend2BlackList(CMD cmd, PlayerVO pvo, @CParam("pid") int pid) throws LogicException;

    /**
     * 从黑名单中删除
     */
    @Client(Command.FRIEND_REMOVE_FROM_BLACK_LIST)
    Map<String, Object> addRemoveFromBlackList(CMD cmd, PlayerVO pvo, @CParam("pid") int pid) throws LogicException;

    @Client(Command.FRIEND_RECOMMENDED)
    Map<String, Object> getRecommendedFriend(CMD cmd, PlayerVO pvo) throws LogicException;

    @Client(Command.FRIEND_FIGHT)
    Map<String, Object> fight(CMD cmd, PlayerVO pvo, @CParam("pid") int pid) throws LogicException;


    /**
     * 获取祝福数据
     */
    @Client(Command.FRIEND_GET_BLESS_DATA)
    Map<String, Object> getBlessDATA(CMD cmd, PlayerVO pvo) throws LogicException;

    /**
     * 祝福 好友
     */
    @Client(Command.FRIEND_BLESS_FRIEND)
    Map<String, Object> blessFriend(CMD cmd, PlayerVO pvo, @CParam("pids") int[] pids) throws LogicException;

    /**
     * 祝福 好友 获取奖励
     */
    @Client(Command.FRIEND_BLESS_GET_PRIZE)
    Map<String, Object> blessGetPrize(CMD cmd, PlayerVO pvo) throws LogicException;

    /**
     * 加载
     */
    void load();

    BaseDAO<FriendReqVO> getFriendReqDAO();

    /**
     * 系统刷新
     */
    void sysResetData(long cur);

    /**
     * 刷新自己的数据
     */
    void sysResetData(FriendBlessVO vo, long cur);
}
