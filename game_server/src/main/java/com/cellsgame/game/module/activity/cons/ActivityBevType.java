package com.cellsgame.game.module.activity.cons;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.bev.impl.*;

public enum ActivityBevType {
	
	Func("func", new FuncBevProcess()),
	DoublePrize("doublePrize", new DoublePrizeBevProcess()),
	CountCur("countCur", new CountCurrencyBevProcess()),
	Wish("wish", new WishBevProcess()),
	Exchange("exchange", new ExchangeBevProcess()),
	TreasureBowl("treBowl", new TreasureBowlBevProcess()),
	Recharge("recharge", new RechargeBevProcess()),
	DoubleCollectibleGoodsPrize("doubleCollectibleGoodsPrize", new DoubleCollectibleGoodsBevProcess()),
	Rank_ChangeCurrency("rank_changeCur", new ChangeCurrencyRankBevProcess()),
	Rank_PlayerFightForce("rank_playerFightForce", new FightForcePlayerRankBevProcess()),
	Rank_GuildFightForce("rank_guildFightForce", new FightForceGuildRankBevProcess()),
	Rank_GuildExp("rank_guildExp", new GuildExpRankBevProcess()),
	Rank_GuildPayMember("rank_guildPayMember", new GuildPayMemberBevRankProcess()),
	Rank_GuildPayMoney("rank_guildPayMoney", new GuildPayMoneyRankBevProcess()),
	
	;
	ActivityBevType(String t, ABevProcess process){
		this.type = t;
		this.process = process;
		SpringBeanFactory.autowireBean(process);
	}
	
	private String type;
	
	private ABevProcess process;

	public String getType() {
		return type;
	}

	public ABevProcess getProcess() {
		return process;
	}
	
}
