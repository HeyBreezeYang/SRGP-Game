package com.cellsgame.game.module.log.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.mail.vo.MailVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import java.util.List;
import java.util.Map;

public class MailLogPro extends LogProcess {

	@Override
	public Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		MailVO mailVO = e.getParam(EvtParamType.MAIL_VO);
		result.put(MAIL_ID, mailVO.getId());
		result.put(MAIL_TITLE, mailVO.getTitle());
		result.put(TYPE, e.toString());
		List<Map<String, Object>> list = GameUtil.createList();
		for(FuncConfig func : mailVO.getItemList()){
			Map<String, Object> map = GameUtil.createSimpleMap();
			map.put(TYPE, func.getType());
			map.put(PARAMS, func.getParam());
			map.put(VALUE, func.getValue());
			list.add(map);
		}
		result.put(ATTS, list);
		return result;
	}

	private static String MAIL_ID = "id";
	private static String MAIL_TITLE = "title";



}
