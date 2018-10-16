package com.cellsgame.game.module.mail;

/**
 * Created by yfzhang on 2016/8/22.
 */
public enum MailType {

    /** 系统邮件*/
    System(1),
    /** 活动邮件*/
    Activity(2),
    /** 战斗邮件*/
    Fight(3),
    /** 家族邮件*/
    Guild(4),
    /** 采集邮件*/
    Gather(5);

    private int value;
    
    MailType(int value){
    	this.value = value;
    }
    
    public int getValue(){
    	return value;
    }
}
