package com.cellsgame.game.module.activity.cons;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;

public enum SysType {
	
	Dup(1) {
		@Override
		public boolean check(GameEvent event, int type2, int subType) {
			int dupType = event.getParam(EvtParamType.DUP_Type);
			if(subType > 0)
				return type2 == this.getType() && dupType == subType;
			else
				return type2 == this.getType();
		}
	},
	Expedition(2),
	Tower(3),
	Shop(4) {
		@Override
		public boolean check(GameEvent event, int type2, int subType) {
			int dupType = event.getParam(EvtParamType.SHOP_CID, 0);
			if(subType > 0)
				return type2 == this.getType() && dupType == subType;
			else
				return type2 == this.getType();
		}
	},
	BuyAp(5),
	BuyMoney(6),
	BuyEnergy(7),
	;
	
	SysType(int t){
		this.type = t;
	}
	
	private int type;

	public int getType() {
		return type;
	}

	public boolean check(GameEvent event, int type2, int subType){
		return type2 == this.getType();
	}
}
