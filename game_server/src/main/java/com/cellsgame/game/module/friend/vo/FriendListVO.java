package com.cellsgame.game.module.friend.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * @author Aly @2017-02-07.
 *         好友列表
 */
public class FriendListVO extends DBVO {
    private int pid;
    private int id;


    @Save(ix = 1)
    private Set<Integer> myFriends;     // 好友列表
    @Save(ix = 2)
    private Set<Integer> blackList;     // 黑名单
    @Save(ix = 3)
    private List<Integer> nearCharPlayer;    // 最近联系玩家


    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = ((int) pk);
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{pid};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        pid = ((int) relationKeys[0]);
    }

    @Override
    protected void init() {
        blackList = new HashSet<>();
        myFriends = new HashSet<>();
        nearCharPlayer = GameUtil.createList();
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {

    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public Set<Integer> getMyFriends() {
        return myFriends;
    }

    public void setMyFriends(Set<Integer> myFriends) {
        this.myFriends = myFriends;
    }

    public Set<Integer> getBlackList() {
        return blackList;
    }

    public void setBlackList(Set<Integer> blackList) {
        this.blackList = blackList;
    }

    public List<Integer> getNearCharPlayer() {
        return nearCharPlayer;
    }

    public void setNearCharPlayer(List<Integer> nearCharPlayer) {
        this.nearCharPlayer = nearCharPlayer;
    }
}
