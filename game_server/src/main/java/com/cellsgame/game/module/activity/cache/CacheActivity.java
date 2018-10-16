package com.cellsgame.game.module.activity.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.vo.ActivityVO;

public class CacheActivity {

	private static final Map<String, ActivityVO> cacheActivity = GameUtil
			.createMap();

	private static final Set<ActivityVO> runningActivities = new HashSet<ActivityVO>();
	

	public static Collection<ActivityVO> getAllActivities() {
		return cacheActivity.values();
	}

	public static ActivityVO getActivityById(String actId) {
		return cacheActivity.get(actId);
	}

	public static void cacheActivity(ActivityVO actvo) {
		cacheActivity.put(actvo.getId(), actvo);
		actvo.initConcernEvents();
		runActivity(actvo);
	}

	public static void runActivity(ActivityVO actvo) {
		if (actvo == null)
			return;
		if (actvo.vaildate()) {
			getRunningactivities().add(actvo);
		}
	}

	public static void removeRunningActivity(ActivityVO actvo) {
		getRunningactivities().remove(actvo);
	}

	public static ActivityVO removeActivity(String actid) {
		return cacheActivity.remove(actid);
	}

	public static Set<ActivityVO> getRunningactivities() {
		return runningActivities;
	}
}
