package com.cellsgame.game.module.activity.bev.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cache.CacheActivityGlobalData;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityGlobalVO;
import com.cellsgame.game.module.activity.vo.ActivityGroupDataVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.mail.bo.MailBO;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class CountCurrencyBevProcess extends ABevProcess {
	
	private static String CurrencyType = "cType";
	private static String MailTitle = "title";
	private static String MailContent = "content";
	private static String Rate = "rate";

	@Override
	public int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act,
					   int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) {
		int tCurrencyType = bev.getIntParam(CurrencyType);
		String title = bev.getStringParam(MailTitle, "default title");
		String content = bev.getStringParam(MailContent, "default content");
		long rate = bev.getIntParam(Rate);
		if(rate == 0) rate = 10000;
		if(tCurrencyType <= 0) return 0;
		Map<Integer, ActivityGlobalVO> globalVOs = CacheActivityGlobalData.get().getGlobalData(act);
		for (Entry<Integer, ActivityGlobalVO> entry : globalVOs.entrySet()) {
			int playerId = entry.getKey();
			ActivityGlobalVO globalVO = entry.getValue();
			Long currNum = globalVO.getCurrencyData().get(tCurrencyType);
			if(currNum != null) {
				long value = currNum *  rate / 10000L;
				MailBO mailBO = SpringBeanFactory.getBean(MailBO.class);
				List<FuncConfig> funcs = GameUtil.createList();
				funcs.add(new FuncConfig(SyncFuncType.ChangeCur.getType(), tCurrencyType, value));
				mailBO.sendSysMail(playerId, title, content, funcs);
			}
		}
        return 1;
	}

	@Override
	protected void listener(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, ActivityBev bev, ActivityGroupDataVO groupDataVO, GameEvent event) {

	}

	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{};
	}

}
