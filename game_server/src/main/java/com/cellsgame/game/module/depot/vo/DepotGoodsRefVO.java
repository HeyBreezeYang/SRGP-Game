package com.cellsgame.game.module.depot.vo;

import java.util.List;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;
/**
 * 仓库道具引用值对象
 */

public class DepotGoodsRefVO extends DBVO {

	@Save(ix = 1)
	private List<Integer> goodsRefs;
	
	@Override
	protected Object initPrimaryKey() {
		return null;
	}

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
		goodsRefs = GameUtil.createList();
	}

	@Override
	public Integer getCid() {
		return null;
	}

	@Override
	public void setCid(Integer cid) {

	}

	public List<Integer> getGoodsRefs() {
		return goodsRefs;
	}

	public void setGoodsRefs(List<Integer> goodsRefs) {
		this.goodsRefs = goodsRefs;
	}

}
