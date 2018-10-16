package com.cellsgame.game.module;

import org.junit.Test;

import java.util.LinkedList;

import static org.mockito.Mockito.mock;

/**
 * @author tiandong
 * @date 2018/09/20 11:10 星期四
 * @description羁绊实体对象
 */

public class Fetter {

    private String relationId;//羁绊id
    private int fetterAccount=100;//羁绊初始点数
    private String relationName;//羁绊名称
    private String relationDesc;//羁绊描述s
    private String  actorAid; //参与者A
    private String  actorBid; //参与者B
    private String  actorC; //预留字段
    private String  grade="C";//当前等级---C-B-A-S
    private String  fullLevelReward;//满级奖励
    private String  sonId;//子嗣id



    public void setFetterAccount(int fetterAccount) {
        this.fetterAccount = fetterAccount;
        //监听羁绊点数用来控制羁绊等级变化
    }

    public int getFetterAccount() {
        return fetterAccount;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public void setRelationDesc(String relationDesc) {
        this.relationDesc = relationDesc;
    }

    public void setActorAid(String actorAid) {
        this.actorAid = actorAid;
    }

    public void setActorBid(String actorBid) {
        this.actorBid = actorBid;
    }

    public String getActorAid() {
        return actorAid;
    }

    public String getActorBid() {
        return actorBid;
    }

    public void setActorC(String actorC) {
        this.actorC = actorC;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setFullLevelReward(String fullLevelReward) {
        this.fullLevelReward = fullLevelReward;
    }

    public void setSonId(String sonId) {
        this.sonId = sonId;
    }

    public String getRelationName() {
        return relationName;
    }

    public String getRelationDesc() {
        return relationDesc;
    }


    public String getActorC() {
        return actorC;
    }

    public String getGrade() {
        return grade;
    }

    public String getFullLevelReward() {
        return fullLevelReward;
    }

    public String getSonId() {
        return sonId;
    }

    @Override
    public String toString() {
        return "Fetter{" +
                "relationId='" + relationId + '\'' +
                ", fetterAccount=" + fetterAccount +
                ", relationName='" + relationName + '\'' +
                ", relationDesc='" + relationDesc + '\'' +
                ", actorAid='" + actorAid + '\'' +
                ", actorBid='" + actorBid + '\'' +
                ", actorC='" + actorC + '\'' +
                ", grade='" + grade + '\'' +
                ", fullLevelReward='" + fullLevelReward + '\'' +
                ", sonId='" + sonId + '\'' +
                '}';
    }

}
