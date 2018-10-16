package com.cellsgame.game.module.skill.vo;

import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

import java.util.List;

/**
 * 被动技能VO
 */
public class SkillVO extends DBVO {
    //技能唯一ID
    private Integer id;
    // 玩家ID
    private Integer playerId;
    // 技能持有者(英雄)ID
    private Integer holderId;

    @Save(ix=1)
    //技能配置档ID
    private Integer cid;

    @Save(ix=2)
    // 是否学习过
    private boolean isLearned;

    @Save(ix=3)
    // 是否装备(未学习不能装备）
    private boolean isEquiped;
    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object o) {
        id = (Integer)o;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{playerId, holderId};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys != null && relationKeys.length > 1) {
            playerId = (int) relationKeys[0];
            holderId = (int) relationKeys[1];
        }
    }

    @Override
    protected void init() {

    }

    @Override
    public Integer getCid() {
        return cid;
    }

    @Override
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }

    public boolean isEquiped() {
        return isEquiped;
    }

    public void setEquiped(boolean equiped) {
        isEquiped = equiped;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getHolderId() {
        return holderId;
    }

    public void setHolderId(Integer id) {
        this.holderId = id;
    }
}
