package com.cellsgame.game.module.chat.vo;

import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

public class ChatVO extends DBVO {

	private int id;

    private int pid;
    
    @Save(ix = 0)
    private List<ChatMsgVO> privateMsg;
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public List<ChatMsgVO> getPrivateMsg() {
		return privateMsg;
	}

	public void setPrivateMsg(List<ChatMsgVO> privateMsg) {
		this.privateMsg = privateMsg;
	}


	@Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = (int) pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{pid};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys.length > 0)
            pid = (int) relationKeys[0];
    }

    @Override
    protected void init() {
        id = 0;
        pid = 0;
        privateMsg = GameUtil.createList();
    }


	@Override
	public Integer getCid() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setCid(Integer cid) {
		// TODO Auto-generated method stub
		
	}
}
