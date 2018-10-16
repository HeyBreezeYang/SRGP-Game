package com.cellsgame.game.module.quest.vo;

import com.cellsgame.orm.DBVO;

public class QuestRecItemVO extends DBVO {
	
	private int cid;

	
	/**
	 * @param cid
	 */
	public QuestRecItemVO(Integer cid) {
		this.cid = cid;
	}
	
	public QuestRecItemVO(){}

	@Override
	protected Object getPrimaryKey() {
		return null;
	}

	@Override
	protected void setPrimaryKey(Object pk) {

	}

	@Override
	protected Object[] getRelationKeys() {
		return null;
	}

	@Override
	protected void setRelationKeys(Object[] relationKeys) {

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



}
