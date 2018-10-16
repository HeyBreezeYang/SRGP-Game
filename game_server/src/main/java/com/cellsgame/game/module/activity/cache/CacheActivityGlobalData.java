package com.cellsgame.game.module.activity.cache;


import java.util.Map;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.module.activity.vo.ActivityGlobalVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class CacheActivityGlobalData {
	
	
	private Table<String, Integer, ActivityGlobalVO> cache = HashBasedTable.create();

    private BaseDAO<ActivityGlobalVO> activityGlobalDAO;
	
	private CacheActivityGlobalData(){
		activityGlobalDAO = SpringBeanFactory.getBean("activityGlobalDAO");
	}
	
	private static CacheActivityGlobalData data;
	
	public static CacheActivityGlobalData get(){
		if(data == null)
			data = new CacheActivityGlobalData();
		return data;
	}
	

	public void cacheGlobalData(ActivityGlobalVO globalVO) {
		cache.put(globalVO.getActivityId(), globalVO.getPlayerId(), globalVO);
	}

	public ActivityGlobalVO getGlobalData(ActivityVO act, PlayerVO player) {
		ActivityGlobalVO vo = cache.get(act.getId(), player.getId());
		if(vo == null){
			vo = new ActivityGlobalVO();
			vo.setActivityId(act.getId());
			vo.setPlayerId(player.getId());
			activityGlobalDAO.save(vo);
			cacheGlobalData(vo);
		}
		return vo;
	}
	
	public Map<Integer, ActivityGlobalVO> getGlobalData(ActivityVO act) {
		return cache.row(act.getId());
	}

}
