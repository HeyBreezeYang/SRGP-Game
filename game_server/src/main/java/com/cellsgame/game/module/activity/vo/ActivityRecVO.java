package com.cellsgame.game.module.activity.vo;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;

public class ActivityRecVO {
	
	private ActivityVO actVO;

	private Map<Integer, Map<Integer, ActivityCondRecVO>> condDatas = GameUtil.createMap();
  	
  	private Map<Integer, Map<Integer, ActivityBevRecVO>> bevDatas = GameUtil.createMap();
  	
  	public ActivityRecVO(ActivityVO actVO){
  		this.actVO = actVO;
  	}

	public ActivityVO getActVO() {
		return actVO;
	}

	public Map<Integer, Map<Integer, ActivityCondRecVO>> getCondDatas() {
		return condDatas;
	}

	public Map<Integer, Map<Integer, ActivityBevRecVO>> getBevDatas() {
		return bevDatas;
	}

	public void addCondRecVO(ActivityCondRecVO condRecVO){
		Map<Integer, ActivityCondRecVO> condsByGroup = condDatas.get(condRecVO.getGroup());
		if(condsByGroup == null){
			condDatas.put(condRecVO.getGroup(), condsByGroup = GameUtil.createSimpleMap());
		}
		condsByGroup.put(condRecVO.getIndex(), condRecVO);
	}
	
  	public void addBevRecVO(ActivityBevRecVO bevRecVO){
		Map<Integer, ActivityBevRecVO> bevsByGroup = bevDatas.get(bevRecVO.getGroup());
		if(bevsByGroup == null){
			bevDatas.put(bevRecVO.getGroup(), bevsByGroup = GameUtil.createSimpleMap());
		}
		bevsByGroup.put(bevRecVO.getIndex(), bevRecVO);
  	}
	
}
