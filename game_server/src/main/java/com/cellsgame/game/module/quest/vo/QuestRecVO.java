package com.cellsgame.game.module.quest.vo;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;


/**
 * 记录任务完成后留下的各种数据
 * 
 * @author peterveron
 *
 */
public class QuestRecVO extends DBVO{
	
	private int id;
	
	private int holderId;
	
	/**
	 * 表明任务记录的类型比如
	 */
	@Save(ix = 1)
	private int type;
	
	@Save(ix = 2)
	private Map<Integer,QuestRecItemVO> questRecs;
	
	public QuestRecVO(){}

	public QuestRecVO(int holderID, int type) {
		this.holderId = holderID;
		this.type = type;
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
		return new Object[]{holderId};
	}

	@Override
	protected void setRelationKeys(Object[] relationKeys) {
		this.holderId = (Integer) relationKeys[0];
	}

	@Override
	protected void init() {
		questRecs = GameUtil.createSimpleMap();
	}

	@Override
	public Integer getCid() {
		return null;
	}

	@Override
	public void setCid(Integer cid) {
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getHolderId() {
		return holderId;
	}

	public void setHolderId(Integer holderId) {
		this.holderId = holderId;
	}

	public Map<Integer,QuestRecItemVO> getQuestRecs() {
		return questRecs;
	}

	public void setQuestRecs(Map<Integer,QuestRecItemVO> questRecs) {
		this.questRecs = questRecs;
	}

	
	
}
