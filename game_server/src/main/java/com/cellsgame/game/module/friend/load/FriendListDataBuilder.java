//package com.cellsgame.game.module.friend.load;
//
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import com.cellsgame.common.util.SpringBeanFactory;
//import com.cellsgame.game.core.excption.LogicException;
//import com.cellsgame.game.core.message.CMD;
//import com.cellsgame.game.module.IBuildData;
//import com.cellsgame.game.module.friend.bo.FriendsBO;
//import com.cellsgame.game.module.friend.cache.CacheFriendReq;
//import com.cellsgame.game.module.friend.vo.FriendListVO;
//import com.cellsgame.game.module.friend.vo.FriendReqVO;
//import com.cellsgame.game.module.player.vo.PlayerVO;
//import com.cellsgame.orm.DBObj;
//
///**
// * @author Aly @2017-02-08.
// */
//public class FriendListDataBuilder implements IBuildData {
//    private static FriendsBO friendsBO;
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
//        List<DBObj> dbObjs = (List<DBObj>) data.get(DATA_SIGN_FRIEND_LIST);
//        if (null != dbObjs && dbObjs.size() > 0) {
//            FriendListVO vo = new FriendListVO();
//            vo.readFromDBObj(dbObjs.get(0));
//            player.setFriendList(vo);
//            Collection<Integer> list = CacheFriendReq.getFriendByStatusSrcOrTgt(FriendReqVO.STATUS_ACCEPT, player.getId());
//            Collection<Integer> reqList = CacheFriendReq.getFriendByStatusSrcOrTgt(FriendReqVO.STATUS_REQUEST_SEND, player.getId());
//            checkAndRemoveReq(player, vo, reqList, false);
//            // 移除在某些情况下造成的 黑名单/好友同时存在情况
//            checkAndRemoveReq(player, vo, list, true);
//            if (null != list) {
//                vo.getMyFriends().addAll(list);
//            }
//        }
//    }
//
//    private void checkAndRemoveReq(PlayerVO player, FriendListVO vo, Collection<Integer> reqList, boolean remove) {
//        if (reqList != null && reqList.size() > 0) {
//            Iterator<Integer> iterator = reqList.iterator();
//            while (iterator.hasNext()) {
//                int pid = iterator.next();
//                if (vo.getBlackList().contains(pid)) {
//                    FriendReqVO rvo = CacheFriendReq.getSrcOrTgt(pid, player.getId());
//                    if (null == rvo) continue;
//                    if (CacheFriendReq.statusChg(rvo, rvo.getStatus(), FriendReqVO.STATUS_DELETE, true)) {
//                        if (null == friendsBO) {
//                            friendsBO = SpringBeanFactory.getBean(FriendsBO.class);
//                        }
//                        friendsBO.getFriendReqDAO().delete(rvo);
//                    }
//                    if (remove)
//                        iterator.remove();
//                }
//            }
//        }
//    }
//}
