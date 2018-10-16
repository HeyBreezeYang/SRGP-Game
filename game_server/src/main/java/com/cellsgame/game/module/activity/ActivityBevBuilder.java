package com.cellsgame.game.module.activity;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.activity.bo.ActivityBO;
import com.cellsgame.game.module.activity.cache.CacheActivity;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityRecVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;


public final class ActivityBevBuilder implements IBuildData {

    private ActivityBO activityBO;
	
	@Resource
    private BaseDAO<ActivityBevRecVO> activityBevRecDAO;

    public void setActivityBO(ActivityBO activityBO) {
        this.activityBO = activityBO;
    }

    @Override
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
        List<DBObj> bevDB = (List<DBObj>) data.get(DATA_SIGN_ACTIVITY_BEV);
        if(bevDB == null || bevDB.isEmpty()) return;
        activityBO.loadActivityRec(player.getActivityRecs(), null, bevDB);
    }

	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
		
	}
}
