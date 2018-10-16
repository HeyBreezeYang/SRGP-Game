package com.cellsgame.game.module.func.impl.checkrec;


import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.IRecChecker;
import com.cellsgame.game.module.func.cons.IRecCheckerType;

public class CheckRecCur extends CheckRec<CheckRecCur>{
	
	
	private Map<CurrencyType,Long> curCostRec = GameUtil.createSimpleMap();
	
	
	@Override
	protected void accept(CheckRecCur rec) {
		Map<CurrencyType, Long> curCost = rec.getCurCostRec();
		Set<Entry<CurrencyType, Long>> es = curCost.entrySet();
		for (Entry<CurrencyType, Long> e : es) {
			addCost(e.getKey(), e.getValue());
		}
	}

	@Override
	public IRecChecker<CheckRecCur> getChecker() {
		return IRecCheckerType.CurCost.getChecker();
	}

	@Override
	public CheckRecCur create() {
		return new CheckRecCur();
	}

	public void addCost(CurrencyType curType,long num){
		Long oldNum = curCostRec.get(curType);
		if(oldNum == null)
			curCostRec.put(curType, num);
		else
			curCostRec.put(curType, oldNum+num);
	}

	
	public Map<CurrencyType,Long> getCurCostRec(){
		return curCostRec;
	}


	

}
