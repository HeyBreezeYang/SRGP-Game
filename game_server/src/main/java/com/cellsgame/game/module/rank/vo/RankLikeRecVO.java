/**
 * 
 */
package com.cellsgame.game.module.rank.vo;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * @author peterveron
 *
 */
public class RankLikeRecVO extends DBVO {
	
	private int id;
	
	private int pid;

	@Save(ix = 1)
	private Map<Integer,Integer> typeLikeCount;
	
	@Save(ix = 2)
	private long lastUpdateTime;
	
	
	public Integer getLikeCount(int type){
		Integer count = typeLikeCount.get(type);
		if(count == null)
			return 0;
		return count;
	}
	
	
	/**
	 * @see com.cellsgame.orm.DBVO#getPrimaryKey()
	 */
	@Override
	protected Object getPrimaryKey() {
		return id;
	}

	/**
	 * @see com.cellsgame.orm.DBVO#setPrimaryKey(java.lang.Object)
	 */
	@Override
	protected void setPrimaryKey(Object pk) {
		this.id = (Integer) pk;
	}

	/**
	 * @see com.cellsgame.orm.DBVO#getRelationKeys()
	 */
	@Override
	protected Object[] getRelationKeys() {
		return new Object[]{pid};
	}

	/**
	 * @see com.cellsgame.orm.DBVO#setRelationKeys(java.lang.Object[])
	 */
	@Override
	protected void setRelationKeys(Object[] relationKeys) {
		pid = (Integer) relationKeys[0];
	}

	/**
	 * @see com.cellsgame.orm.DBVO#init()
	 */
	@Override
	protected void init() {
		setTypeLikeCount(GameUtil.createSimpleMap());
	}

	/**
	 * @see com.cellsgame.orm.DBVO#getCid()
	 */
	@Override
	public Integer getCid() {
		return null;
	}

	/**
	 * @see com.cellsgame.orm.DBVO#setCid(java.lang.Integer)
	 */
	@Override
	public void setCid(Integer cid) {
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public Map<Integer,Integer> getTypeLikeCount() {
		return typeLikeCount;
	}

	public void setTypeLikeCount(Map<Integer,Integer> typeLikeCount) {
		this.typeLikeCount = typeLikeCount;
	}


	/**
	 * @param likeType
	 */
	public void addLike(int likeType) {
		Integer count = typeLikeCount.get(likeType);
		if(count == null)
			count = 0;
		typeLikeCount.put(likeType, ++count);
	}

}
