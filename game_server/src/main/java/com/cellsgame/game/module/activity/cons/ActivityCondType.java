package com.cellsgame.game.module.activity.cons;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.cond.impl.*;

public enum ActivityCondType {
	ChangeCur("chgCur", new ChangeCurrencyCondProcess()),//货币变更
	CountCur("countCur", new CountCurrencyCondProcess()),//货币统计
	GoodsCollect("collectGoods", new GoodsCollectCondProcess()),//物品收集
	Login("login", new LoginCondProcess()),//登录
	PlayerLv("playerLv", new PlayerLevelCondProcess()),//玩家等级
	ChangeGoods("chgGoods", new ChangeGoodsCondProcess()),//物品变更
	GuildBossHurt("guildBossHurt", new GuildBossHurtCondProcess()),//公会BOSS伤害
	GuildKillBoss("guildKillBoss", new GuildKillBossCondProcess()),//公会BOSS击杀
	PayTimes("payTimes", new PayTimesCondProcess()),//充值次数
	PayDays("payDays", new PayDaysCondProcess()),//充值天数
	PayMoney("payMoney", new PayMoneyCondProcess()),//充值金额
	FightForce("fightForce", new FightForceCondProcess()),//战斗力
	GuildPayMember("guildPayMember", new GuildPayMemberCondProcess()),//公会充值人数
	;
	ActivityCondType(String t, ACondProcess process){
		this.type = t;
		this.process = process;
		SpringBeanFactory.autowireBean(process);
	}
	
	private String type;
	
	private ACondProcess process;

	public String getType() {
		return type;
	}

	public ACondProcess getProcess() {
		return process;
	}
	
}
