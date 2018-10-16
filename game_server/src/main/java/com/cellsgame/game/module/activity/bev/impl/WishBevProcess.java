package com.cellsgame.game.module.activity.bev.impl;

import java.util.Map;


import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.msg.CodeActivity;
import com.cellsgame.game.module.activity.msg.MsgFactoryActivity;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityGroupDataVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class WishBevProcess extends ABevProcess {

	private static String InputAtt_IX = "ix";
	
	private static String ExecAtt_RecordTime = "time";
	private static String ExecAtt_IX = "ix";
	
	@Override
	public int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act,
					   int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) {
		if(player == null) return 0;
		ActivityBevRecVO recVO = bevRecVOs.get(ActivityScopeType.Personal);
		if(recVO == null) return 0;
		String excTime = recVO.getExecParams().get(ExecAtt_RecordTime);
		if(excTime == null){
			//首次执行该行为
			Integer ix = (Integer) inputAtts.get(InputAtt_IX);
			if(ix == null) return 0;
			CodeActivity.ACTIVITY_EXEC_PARAMS_ERROR.throwIfTrue(ix >= bev.getFuncs().size());
			recVO.getExecParams().put(ExecAtt_RecordTime, String.valueOf(System.currentTimeMillis()));
			recVO.getExecParams().put(ExecAtt_IX, String.valueOf(ix));
		}else{
			//第二次执行行为
			String excIx = recVO.getExecParams().get(ExecAtt_IX);
			CodeActivity.ACTIVITY_SERVER_DATA_ERROR.throwIfTrue(excIx == null);
			long currTime = System.currentTimeMillis();
			CodeActivity.ACTIVITY_EXEC_CD_EXISTS.throwIfTrue(currTime - Long.valueOf(excTime) < DateUtil.DAY_MILLIS);
			Map prizeMap = GameUtil.createSimpleMap();
	        // 奖励
	        FuncsExecutorsType.Base.getExecutor(cmd).addSyncFunc(bev.getFuncs().get(Integer.valueOf(excIx))).exec(parent, prizeMap, player);
	        MsgFactoryActivity.instance().createActivityPrizeMsg(parent, prizeMap);
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
