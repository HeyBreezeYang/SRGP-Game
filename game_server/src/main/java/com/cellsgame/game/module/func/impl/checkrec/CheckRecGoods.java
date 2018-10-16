package com.cellsgame.game.module.func.impl.checkrec;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.IRecChecker;
import com.cellsgame.game.module.func.cons.IRecCheckerType;

public class CheckRecGoods extends CheckRec<CheckRecGoods> {

	
	private Map<Integer,Integer> goodsCost = GameUtil.createSimpleMap();
	
	@Override
	public CheckRecGoods create() {
		return new CheckRecGoods();
	}

	@Override
	protected void accept(CheckRecGoods rec) {
		Set<Entry<Integer, Integer>> es = rec.getGoodsCost().entrySet();
		for (Entry<Integer, Integer> e : es) {
			add(e.getKey(),e.getValue());
		}
	}

	@Override
	public IRecChecker<CheckRecGoods> getChecker() {
		return IRecCheckerType.GoodsCost.getChecker();
	}
	
	
	public void add(Integer gid, Integer num){
		Integer gnum = goodsCost.get(gid);
		if(gnum == null)
			goodsCost.put(gid, num);
		else
			goodsCost.put(gid,gnum+num);
	}

	public Map<Integer,Integer> getGoodsCost() {
		return goodsCost;
	}



}
