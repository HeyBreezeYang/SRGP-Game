package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityGroupDataVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.mail.bo.MailBO;
import com.cellsgame.game.module.pay.cons.EvtPay;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

public class RechargeBevProcess extends ABevProcess {

	@Resource
	private MailBO mailBO;

	public void setMailBO(MailBO mailBO) {
		this.mailBO = mailBO;
	}

	private static String MailTitle = "title";
	private static String MailContent = "content";
	private static String OrderItem = "itemId";

	private static org.slf4j.Logger log = LoggerFactory.getLogger(RechargeBevProcess.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act,
					   int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) {
		if(player == null) return 0;
		if(bev.getFuncs() == null || bev.getFuncs().isEmpty()){
			log.error("Activity execBev RechargeBevProcess funcs is empty ...");
			return 0;
		}
		String orderItemId = event.getParam(EvtParamType.ORDER_ITEM_ID);
		String title = bev.getStringParam(MailTitle, "default recharge activity title");
		String content = bev.getStringParam(MailContent, "default recharge activity content");
		String itemId = bev.getStringParam(OrderItem, null);
		if(itemId == null || itemId.equals(orderItemId)){
			mailBO.sendSysMail(player.getId(), title, content, bev.getFuncs());
			return 1;
		}
		return 0;
	}

	@Override
	protected void listener(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, ActivityBev bev, ActivityGroupDataVO groupDataVO, GameEvent event) {

	}

	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{EvtPay.Pay};
	}

}
