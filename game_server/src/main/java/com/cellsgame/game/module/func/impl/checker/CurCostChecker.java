package com.cellsgame.game.module.func.impl.checker;

import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Resource;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IRecChecker;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecCur;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class CurCostChecker extends CurTypeChecker implements IRecChecker<CheckRecCur>{
	@Resource
	private DepotBO depotBO;
	
	
	@Override
	public void check(PlayerVO player, FuncParam param) throws LogicException {
		super.check(player, param);
		if(param.getValue()<0){
			CurrencyType cType = Enums.get(CurrencyType.class, param.getParam());
			depotBO.checkCurEnough(player, cType, Math.abs(param.getValue()));
		}
	}
	
	

	@Override
	public void checkRec(PlayerVO player, CheckRecCur rec) throws LogicException {
		Set<Entry<CurrencyType, Long>> es = rec.getCurCostRec().entrySet();
		for (Entry<CurrencyType, Long> e : es) {
			depotBO.checkCurEnough(player, e.getKey(), Math.abs(e.getValue()));
		}
		
	}
	
}
