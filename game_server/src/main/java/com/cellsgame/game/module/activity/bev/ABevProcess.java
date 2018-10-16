package com.cellsgame.game.module.activity.bev;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtNoticer;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.msg.CodeActivity;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityGroupDataVO;
import com.cellsgame.game.module.activity.vo.ActivityGroupVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;

public abstract class ABevProcess extends EvtNoticer {

	@Resource
	protected BaseDAO<ActivityBevRecVO> activityBevRecDAO;
	@Resource
	protected BaseDAO<ActivityGroupDataVO> activityGroupDataDAO;
	
	protected abstract int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) throws LogicException;

	protected abstract void listener(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, ActivityBev bev, ActivityGroupDataVO groupDataVO, GameEvent event);

	public void listener(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, ActivityBev bev, int group, GameEvent event){
		ActivityGroupDataVO groupDataVO = getGroupData(act, group);
		listener(parent, cmd, player, act, bev, groupDataVO, event);
	}


	protected ActivityGroupDataVO getGroupData(ActivityVO act, int group) {
		ActivityGroupVO groupVO = act.getGroups().get(group);
		ActivityGroupDataVO dataVO = groupVO.getActivityGroupDataVO();
		if(dataVO == null){
			dataVO = new ActivityGroupDataVO();
			dataVO.setActivityId(act.getId());
			dataVO.setGroup(group);
			activityGroupDataDAO.save(dataVO);
			groupVO.setActivityGroupDataVO(dataVO);
		}
		return dataVO;
	}

	public void exec(Map<?, ?> parent, CMD cmd, ActivityVO act, ActivityBev bev,
			int group, int index, PlayerVO player, GameEvent event, Map<?, ?> inputAtts) throws LogicException{
		Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs = getBevRecVOs(act, bev, group, index, player);
		CodeActivity.ACTIVITY_EXEC_BEV_ERROR.throwIfTrue(bevRecVOs == null);
		if(canExec(bevRecVOs)){
			int exeNum = execBev(parent, cmd, player, act, group, index, bev, event, inputAtts, bevRecVOs);
			if(exeNum > 0){
				recordExecNum(bev, bevRecVOs, exeNum);
				activityBevRecDAO.save(bevRecVOs.values());
			}
		}else
			CodeActivity.ACTIVITY_EXEC_BEV_LIMIT.throwException();
	}
	
	private void recordExecNum(ActivityBev bev, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs, int exeNum) {
		for (Entry<ActivityScopeType, ActivityBevRecVO> entry : bevRecVOs.entrySet()) {
			ActivityBevRecVO recVO = entry.getValue();
			recVO.setExecNum(recVO.getExecNum() + exeNum);
			ActivityScopeType scopeType = entry.getKey();
			int limit = bev.getExecLimit().get(scopeType.getType());
			if(limit > 0)
				if(recVO.getExecNum() >= limit) recVO.setCanExec(false);
		}
	}

	private boolean canExec(Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) {
		for (Entry<ActivityScopeType, ActivityBevRecVO> entry : bevRecVOs.entrySet()) {
			ActivityBevRecVO recVO = entry.getValue();
			if(!recVO.isCanExec()) return false;
		}
		return true;
	}

	public boolean canExecBev(ActivityVO act, ActivityBev bev, int group, int index, PlayerVO player) {
		Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs = getBevRecVOs(act, bev, group, index, player);
		if(bevRecVOs == null) return false;
		return canExec(bevRecVOs);
	}


	public Map<ActivityScopeType, ActivityBevRecVO> getBevRecVOs(ActivityVO act, ActivityBev bev,
			int group, int index, PlayerVO player) throws LogicException {
		List<ActivityScopeType> scopes = bev.getScopeTypes();
		Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs = GameUtil.createSimpleMap();
		for(ActivityScopeType type : scopes){
			ActivityBevRecVO bevRecVO = type.getBevRecVO(act, group, index, player);
			if(bevRecVO == null){
				bevRecVO = type.newBevRecVO(act, group, index, player);
				bevRecVO.setCanExec(true);
				activityBevRecDAO.save(bevRecVO);
			}
			bevRecVOs.put(type, bevRecVO);
		}
		return bevRecVOs;
	}
//
//	private static String BevExecResult = "bevResult";
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	protected static Map gocBevExecResult(Map parent) {
//		if(parent == null) return null;
//		Map bevResult = (Map<?, ?>) parent.get(BevExecResult);
//		if(bevResult == null){
//			bevResult = GameUtil.createSimpleMap();
//			parent.put(BevExecResult, bevResult);
//		}
//		return bevResult;
//	}
//
//	@SuppressWarnings({ "rawtypes" })
//	public static Map getBevExecResult(Map parent) {
//		if(parent == null) return null;
//		return (Map) parent.remove(BevExecResult);
//	}
}
