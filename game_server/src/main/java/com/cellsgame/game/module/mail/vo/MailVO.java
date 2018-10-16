package com.cellsgame.game.module.mail.vo;

import java.util.List;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * Created by yfzhang on 2016/8/19.
 */
public class MailVO extends DBVO {

    private int id;

    private int playerId;

    /** 邮件发送者*/
    @Save(ix = 0)
    private String senderName;

    /** 邮件发送时间*/
    @Save(ix = 1)
    private long sendTime;

    /** 邮件类型*/
    @Save(ix = 2)
    private int type;

    /** 邮件标题*/
    @Save(ix = 3)
    private String title;

    /** 邮件内容*/
    @Save(ix = 4)
    private String context;

    /** 附件*/
    @Save(ix = 5)
    private List<FuncConfig> itemList;

    /** 是否已读*/
    @Save(ix = 6)
    private boolean read;

    /** 是否已经领取附件*/
    @Save(ix = 7)
    private boolean pick;

    /** 有效期*/
    @Save(ix = 8)
    private long valid;
    
    /** 功能*/
    @Save(ix = 9)
    private int funcType;
    
    /** 功能参数*/
    @Save(ix = 10)
    private String[] funcParams;
    
    @Save(ix = 11)
    private long limitDate;

    public List<FuncConfig> getItemList() {
		return itemList;
	}

	public void setItemList(List<FuncConfig> itemList) {
		this.itemList = itemList;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isPick() {
        return pick;
    }

    public void setPick(boolean pick) {
        this.pick = pick;
    }

    public long getValid() {
        return valid;
    }

    public void setValid(long valid) {
        this.valid = valid;
    }

    public int getFuncType() {
		return funcType;
	}

	public void setFuncType(int funcType) {
		this.funcType = funcType;
	}

	public String[] getFuncParams() {
		return funcParams;
	}

	public void setFuncParams(String[] funcParams) {
		this.funcParams = funcParams;
	}


    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = (Integer) pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{playerId};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        this.playerId = (int) relationKeys[0];
    }

    @Override
    protected void init() {
        type = 0;
        title = "";
        context = "";
        itemList = GameUtil.createList();
        senderName = "";
        sendTime = 0;
        read = false;
        pick = false;
        valid = 0;
        funcParams = new String[0];
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {

    }

    public long getLimitDate() {
        return limitDate;
    }

	public void setLimitDate(long limitDate) {
		this.limitDate = limitDate;
	}
}
