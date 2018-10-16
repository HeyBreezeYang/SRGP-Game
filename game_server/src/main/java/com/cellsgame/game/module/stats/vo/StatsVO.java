/**
 * 
 */
package com.cellsgame.game.module.stats.vo;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * 统计数据类  用于统计游戏中各种数据 提供给各类任务,活动功能使用
 * @author peterveron
 *
 */
public class StatsVO extends DBVO{
	
	private int id;
	
	private int pid;
	
	@Save(ix = 1)
	private Map<Integer,Long> data;
	
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
		this.id = (Integer)pk;
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
		this.pid = (Integer) relationKeys[0];
	}

	/**
	 * @see com.cellsgame.orm.DBVO#init()
	 */
	@Override
	protected void init() {
		data = GameUtil.createSimpleMap();
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
	

	public Map<Integer,Long> getData() {
		return data;
	}

	public void setData(Map<Integer,Long> data) {
		this.data = data;
	}
	
	public void add(Integer type, Long change){
		Long now = data.get(type) ;
		if(now == null)
			data.put(type, change);
		else
			data.put(type,now+change);
	}
	
	public void update(Integer type, Long update){
		data.put(type, update);
	}
	
	public Long getData(Integer type){
		Long ret = data.get(type);
		return ret == null? 0: ret;
	}

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
	
}
