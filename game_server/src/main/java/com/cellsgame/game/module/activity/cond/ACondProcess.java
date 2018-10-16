package com.cellsgame.game.module.activity.cond;

import java.util.Map;

import javax.annotation.Resource;

import com.cellsgame.game.core.event.EvtNoticer;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.vo.ActivityCondRecVO;
import com.cellsgame.game.module.activity.vo.ActivityGlobalVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;

public abstract class ACondProcess extends EvtNoticer {
	
    @Resource
    private BaseDAO<ActivityCondRecVO> activityCondRecDAO;

	@Resource
    private BaseDAO<ActivityGlobalVO> activityGlobalDAO;
	
	protected abstract boolean recordCondData(Map<?, ?> parent, ActivityVO act, ActivityCond cond, ActivityCondRecVO condRecVO, PlayerVO player, GameEvent event);
	
	public boolean check(ActivityVO act, ActivityCond cond, int group, int index, PlayerVO player){
		ActivityScopeType scopeType = cond.getScopeType();
		ActivityCondRecVO condRecVO = scopeType.getCondRecVO(act, group, index, player);
		if(condRecVO == null) return false;
		return checkCondData(cond, condRecVO);
	}

	public boolean record(Map<?, ?> parent, ActivityVO act,
			ActivityCond cond, int group, int index,
			PlayerVO player, GameEvent event) {
		ActivityScopeType scopeType = cond.getScopeType();
		ActivityCondRecVO condRecVO = scopeType.getCondRecVO(act, group, index, player);
		if(condRecVO == null){
			condRecVO = scopeType.newCondRecVO(act, group, index, player);
			if(condRecVO == null)
				return false;
			activityCondRecDAO.save(condRecVO);
		}
		if(checkCondData(cond, condRecVO)) return false;
		boolean result = recordCondData(parent, act, cond, condRecVO, player, event);
		if(result){
			condRecVO.setFinish(checkCondData(cond, condRecVO));
			activityCondRecDAO.save(condRecVO);
		}
		return result;
	}
	
	public boolean checkCondData(ActivityCond cond, ActivityCondRecVO condRecVO) {
		if(condRecVO.isFinish()) return true;
		long limit = cond.getValue();
		if(limit <= 0)return false;
		long currRec = condRecVO.getFinishData();
		if(currRec >= limit)
			return true;
		return false;
	}

	public BaseDAO<ActivityCondRecVO> getActivityCondRecDAO() {
		return activityCondRecDAO;
	}

	public BaseDAO<ActivityGlobalVO> getActivityGlobalDAO() {
		return activityGlobalDAO;
	}
	
	
}
