package com.cellsgame.game.module.activity.bo.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.activity.ActivityUtil;
import com.cellsgame.game.module.activity.bo.ActivityBO;
import com.cellsgame.game.module.activity.cache.CacheActivity;
import com.cellsgame.game.module.activity.cache.CacheActivityGlobalData;
import com.cellsgame.game.module.activity.cons.ActivityConstant;
import com.cellsgame.game.module.activity.cons.EvtTypeActivity;
import com.cellsgame.game.module.activity.csv.ActivityConfig;
import com.cellsgame.game.module.activity.msg.CodeActivity;
import com.cellsgame.game.module.activity.msg.MsgFactoryActivity;
import com.cellsgame.game.module.activity.process.ActivityManager;
import com.cellsgame.game.module.activity.vo.*;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;

public class ActivityBOImpl implements ActivityBO{
	
	@Resource
    private BaseDAO<ActivityVO> activityDAO;
	@Resource
    private BaseDAO<ActivityCondRecVO> activityCondRecDAO;
	@Resource
    private BaseDAO<ActivityBevRecVO> activityBevRecDAO;
	@Resource
    private BaseDAO<ActivityGlobalVO> activityGlobalDAO;
	@Resource
	private BaseDAO<ActivityGroupDataVO> activityGroupDataDAO;

	@Override
	public void init() {
		List<DBObj> all = activityDAO.getAll();
        if (all != null) {
            for (DBObj dbObj : all) {
            	ActivityVO vo = new ActivityVO();
                vo.readFromDBObj(dbObj);
                CacheActivity.cacheActivity(vo);
				all = activityCondRecDAO.getByRelationKey(0, vo.getId());
				if (all != null) {
					List<ActivityCondRecVO> delete = GameUtil.createList();
					for (DBObj dbObj1 : all) {
						ActivityCondRecVO condRecVO = new ActivityCondRecVO();
						condRecVO.readFromDBObj(dbObj1);
						ActivityVO actVO = CacheActivity.getActivityById(condRecVO.getActivityId());
						if(actVO == null){
							delete.add(condRecVO);
							continue;
						}
						ActivityRecVO recVO = actVO.getRecVO();
						if(recVO == null) {
							recVO = new ActivityRecVO(actVO);
							actVO.setRecVO(recVO);
						}
						recVO.addCondRecVO(condRecVO);
					}
					if(!delete.isEmpty()) activityCondRecDAO.delete(delete);
				}
				all = activityBevRecDAO.getByRelationKey(0, vo.getId());
				if (all != null) {
					List<ActivityBevRecVO> delete = GameUtil.createList();
					for (DBObj dbObj1 : all) {
						ActivityBevRecVO bevRecVO = new ActivityBevRecVO();
						bevRecVO.readFromDBObj(dbObj1);
						ActivityVO actVO = CacheActivity.getActivityById(bevRecVO.getActivityId());
						if(actVO == null){
							delete.add(bevRecVO);
							continue;
						}
						ActivityRecVO recVO = actVO.getRecVO();
						if(recVO == null) {
							recVO = new ActivityRecVO(actVO);
							actVO.setRecVO(recVO);
						}
						recVO.addBevRecVO(bevRecVO);
					}
					if(!delete.isEmpty()) activityBevRecDAO.delete(delete);
				}


				all = activityGroupDataDAO.getByRelationKey(0, vo.getId());
				if (all != null) {
					List<ActivityGroupDataVO> delete = GameUtil.createList();
					for (DBObj dbObj1 : all) {
						ActivityGroupDataVO groupDataVO = new ActivityGroupDataVO();
						groupDataVO.readFromDBObj(dbObj1);
						ActivityVO actVO = CacheActivity.getActivityById(groupDataVO.getActivityId());
						if(actVO == null){
							delete.add(groupDataVO);
							continue;
						}
						ActivityGroupVO groupVO = actVO.getGroups().get(groupDataVO.getGroup());
						groupVO.setActivityGroupDataVO(groupDataVO);
						groupDataVO.initRanks();
					}
					if(!delete.isEmpty()) activityGroupDataDAO.delete(delete);
				}
            }
        }
        all = activityGlobalDAO.getAll();
        if (all != null) {
        	List<ActivityGlobalVO> delete = GameUtil.createList();
            for (DBObj dbObj : all) {
            	ActivityGlobalVO globalVO = new ActivityGlobalVO();
            	globalVO.readFromDBObj(dbObj);
                ActivityVO actVO = CacheActivity.getActivityById(globalVO.getActivityId());
                if(actVO == null){
                	delete.add(globalVO);
                }else{
                	CacheActivityGlobalData.get().cacheGlobalData(globalVO);
                }
            }
            if(!delete.isEmpty()) activityGlobalDAO.delete(delete);
        }
        Map<Integer, ActivityConfig> cfgs = CacheConfig.getCfgsByClz(ActivityConfig.class);
        if(cfgs != null) {
			for (ActivityConfig cfg : cfgs.values()) {
				ActivityVO activityVO = ActivityUtil.createActivity(cfg);
				ActivityVO oldActVO = CacheActivity.getActivityById(activityVO.getId());
				if (oldActVO == null) {
					activityVO.setStatus(ActivityConstant.ACTIVITY_STATUS_CRE);
					activityDAO.save(activityVO);
					activityVO.setRecVO(new ActivityRecVO(activityVO));
					CacheActivity.cacheActivity(activityVO);
				}
			}
		}
		loadGuildActivityData();
	}


	public void loadActivityRec(Map<String, ActivityRecVO> recVOMap, List<DBObj> condRecs, List<DBObj> bevRecs){
		if(condRecs != null) {
			List<ActivityCondRecVO> delete = GameUtil.createList();
			for (DBObj dbObj1 : condRecs) {
				ActivityCondRecVO condRecVO = new ActivityCondRecVO();
				condRecVO.readFromDBObj(dbObj1);
				ActivityVO actVO = CacheActivity.getActivityById(condRecVO.getActivityId());
				if (actVO == null) {
					delete.add(condRecVO);
					continue;
				}
				ActivityRecVO recVO = recVOMap.get(condRecVO.getActivityId());
				if (recVO == null) {
					recVO = new ActivityRecVO(actVO);
					actVO.setRecVO(recVO);
					recVOMap.put(condRecVO.getActivityId(), recVO);
				}
				recVO.addCondRecVO(condRecVO);
			}
			if (!delete.isEmpty()) activityCondRecDAO.delete(delete);
		}
		if(bevRecs != null){
			List<ActivityBevRecVO> delete = GameUtil.createList();
			for (DBObj dbObj1 : bevRecs) {
				ActivityBevRecVO bevRecVO = new ActivityBevRecVO();
				bevRecVO.readFromDBObj(dbObj1);
				ActivityVO actVO = CacheActivity.getActivityById(bevRecVO.getActivityId());
				if(actVO == null){
					delete.add(bevRecVO);
					continue;
				}
				ActivityRecVO recVO = recVOMap.get(bevRecVO.getActivityId());
				if(recVO == null) {
					recVO = new ActivityRecVO(actVO);
					actVO.setRecVO(recVO);
					recVOMap.put(bevRecVO.getActivityId(), recVO);
				}
				recVO.addBevRecVO(bevRecVO);
			}
			if(!delete.isEmpty()) activityBevRecDAO.delete(delete);
		}
	}


	@Override
	public void loadGuildActivityData() {
		for(GuildVO guildVO : CacheGuild.getAllGuild()){
			List<DBObj> condRecs  = activityCondRecDAO.getByRelationKey(0, guildVO.getId());
			List<DBObj> bevRecs = activityBevRecDAO.getByRelationKey(0, guildVO.getId());
			loadActivityRec(guildVO.getActivityRecs(), condRecs, bevRecs);
		}
	}

	@Override
	public void sysUpdateActivityStatus() {
		Dispatch.dispatchGameLogic(new Runnable() {
			@Override
			public void run() {
				long ms = System.currentTimeMillis();
				Collection<ActivityVO> allActivities = CacheActivity.getAllActivities();
				List<ActivityVO> startActivities = GameUtil.createList();
				List<ActivityVO> updateActivities = GameUtil.createList();
				List<ActivityVO> stopedActivities = GameUtil.createList();
				Map<String, Object> updateInfoMap = GameUtil.createSimpleMap();
				for (ActivityVO actvo : allActivities) {
					long startMs = actvo.getStartDate();
					long endMs = actvo.getEndDate();
					switch (actvo.getStatus()) {
						case ActivityConstant.ACTIVITY_STATUS_CRE:
							if (ms >= startMs && ms <= endMs) {
								actvo.setStatus(ActivityConstant.ACTIVITY_STATUS_RUNNING);
								actvo.setRefTime(startMs);
								CacheActivity.runActivity(actvo);// 添加全局监听条件数据
								startActivities.add(actvo);
								activityDAO.save(actvo);
							}
							break;
						case ActivityConstant.ACTIVITY_STATUS_RUNNING:
							if (ms > endMs) {
								actvo.setStatus(ActivityConstant.ACTIVITY_STATUS_STOPED);
								ActivityManager.execStopExecAllBev(CMD.system.now(), actvo);
								CacheActivity.removeRunningActivity(actvo);// 移除全局监听条件数据
								stopedActivities.add(actvo);
								activityDAO.save(actvo);
							}else{
								long updateMs = 0;
								if ((updateMs = calActivityRefTime(actvo, ms)) > 0) {
									actvo.setRefTime(updateMs);
									updateInfoMap = resetActivityRec(updateInfoMap, actvo);
									for(GuildVO guild : CacheGuild.getAllGuild()){
										updateInfoMap = resetGuildActivityRec(updateInfoMap, actvo, guild);
									}
									ActivityManager.execAutoBevActivity(CMD.system.now(), updateInfoMap, actvo);
									updateActivities.add(actvo);
									activityDAO.save(actvo);
								}
							}
							break;
					}
				}
				refOnlinePlayerActivityInfo(updateInfoMap, startActivities, updateActivities, stopedActivities);
			}
		});
	}
	
	public long calActivityRefTime(ActivityVO act, long ms) {
		if( act.getRefInterval() <= 0 )//刷新间隔小于等于0不刷新
			return 0;
			long numOfInterval = (ms - act.getRefTime())/(act.getRefInterval()*DateUtil.MIN_MILLIS);
			if(numOfInterval == 0)
				return 0;
			return act.getRefTime()+numOfInterval*(act.getRefInterval()*DateUtil.MIN_MILLIS);	
	}
	
	private void refOnlinePlayerActivityInfo(Map<String, Object> updateInfoMap, List<ActivityVO> startActivities,
			List<ActivityVO> updateActivities, List<ActivityVO> stopedActivities){
		CMD cmd = CMD.system.now();
		Collection<PlayerVO> onlinePlayers = CachePlayer.getOnlinePlayers();
		if (updateInfoMap.isEmpty() && updateActivities.isEmpty() && startActivities.isEmpty()
				&& stopedActivities.isEmpty())
			return;
		for (PlayerVO pvo : onlinePlayers) {
			Map<String, Object> info = GameUtil.createSimpleMap();
			info.putAll(updateInfoMap);
			for (ActivityVO actvo : startActivities) {
				info =  MsgFactoryActivity.instance().createActivityStatusChangeMsg(info, actvo.getId(),
						actvo.getStatus());
			}
			for (ActivityVO actvo : updateActivities) {
				info = resetPlayerActivityRec(cmd, info, actvo ,pvo);
			}
			for (ActivityVO actvo : stopedActivities) {
				info = MsgFactoryActivity.instance().createActivityStatusChangeMsg(info, actvo.getId(),
						actvo.getStatus());
			}
			if(!startActivities.isEmpty()) {
				EvtTypeActivity.AcceptNewActivity.happen(info, cmd, pvo, EvtParamType.ACTIVITY_LIST.val(startActivities));
			}
			Map<String, Object> result = MsgUtil.brmAll(info, Command.Activity_SysRef);
			Push.multiThreadSend(pvo.getMessageController(), result);
		}
	}
	

	@Override
	public void deleteActivity(String actid) throws LogicException {
		ActivityVO act = CacheActivity.removeActivity(actid);
		CodeActivity.ACTIVITY_NOT_FIND.throwIfTrue(act == null);
		List<ActivityRecVO> deleteRec = GameUtil.createList();
		deleteRec.add(act.getRecVO());
		CacheActivity.removeRunningActivity(act);
		Collection<GuildVO> allGuild = CacheGuild.getAllGuild();
		for (GuildVO guild : allGuild) {
			ActivityRecVO recVO = guild.getActivityRecs().remove(act.getId());
			if (recVO != null)
				deleteRec.add(recVO);
		}
		Collection<PlayerVO> allPlayers = CachePlayer.getOnlinePlayers();
		for (PlayerVO pvo : allPlayers) {
			Map<String, Object> info = GameUtil.createSimpleMap();
			ActivityRecVO recVO = pvo.getActivityRecs().remove(act.getId());
			if (recVO != null)
				deleteRec.add(recVO);
			info = MsgFactoryActivity.instance().createRemoveActivityMsg(info, act.getId());
			Map<String, Object> result = MsgUtil.brmAll(info, Command.Activity_SysRef);
			Push.multiThreadSend(pvo.getMessageController(), result);
		}
		activityDAO.delete(act);
		for(ActivityRecVO recVO : deleteRec){
			if(!recVO.getCondDatas().isEmpty()){
				for(Map<Integer, ActivityCondRecVO> condRecVOs : recVO.getCondDatas().values()){
					activityCondRecDAO.delete(condRecVOs.values());
				}
			}
			if(!recVO.getBevDatas().isEmpty()){
				for(Map<Integer, ActivityBevRecVO> bevRecVOs : recVO.getBevDatas().values()){
					activityBevRecDAO.delete(bevRecVOs.values());
				}
			}
		}
	}

	

	private void enterGame(CMD cmd, PlayerVO player) {
		Collection<ActivityVO> runningActivitys = CacheActivity.getRunningactivities();
		Map<String, Object> result = GameUtil.createSimpleMap();
		List<ActivityVO> startActVOs = GameUtil.createList();
		for (ActivityVO actvo : runningActivitys) {
			ActivityRecVO precvo = player.getActivityRecs().get(actvo.getId());
			if (precvo == null) {
				startActVOs.add(actvo);
			} else {
				result = resetPlayerActivityRec(cmd, result, actvo, player);
			}
		}
		if(!startActVOs.isEmpty()) 
			EvtTypeActivity.AcceptNewActivity.happen(result, cmd, player, EvtParamType.ACTIVITY_LIST.val(startActVOs));
	}

	@Override
	public void createActivity(Map<String, String> activityInfo) throws LogicException {
		ActivityVO actvo = ActivityUtil.createActivity(activityInfo);
		ActivityVO oldActVO = CacheActivity.getActivityById(actvo.getId());
		CodeActivity.ACTIVITY_ID_ALREADY.throwIfTrue(oldActVO != null);
		actvo.setStatus(ActivityConstant.ACTIVITY_STATUS_CRE);
		activityDAO.save(actvo);
		actvo.setRecVO(new ActivityRecVO(actvo));
		CacheActivity.cacheActivity(actvo);
		Collection<PlayerVO> onlinePlayers = CachePlayer.getOnlinePlayers();
		for (PlayerVO pvo : onlinePlayers) {
			Map<String, Object> info = GameUtil.createSimpleMap();
			info = MsgFactoryActivity.instance().createNewActivityMsg(info, actvo, pvo);
			Map<String, Object> result = MsgUtil.brmAll(info, Command.Activity_SysRef);
			Push.multiThreadSend(pvo.getMessageController(), result);
		}
	}

	@Override
	public Map getActivityInfo(CMD cmd, PlayerVO player, String activityId) throws LogicException {
		ActivityVO actVO = CacheActivity.getActivityById(activityId);
		CodeActivity.ACTIVITY_NOT_EXISTS.throwIfTrue(actVO == null);
		CodeActivity.ACTIVITY_NOT_IN_ACTION.throwIfTrue(!actVO.vaildate());
		Map<String, Object> result = GameUtil.createSimpleMap();
		result = MsgFactoryActivity.instance().getActivityInfoMsg(result, actVO, player);
		return MsgUtil.brmAll(result, cmd.getCmd());
	}

	public Map<String, Object> resetActivityRec(Map<String, Object> parentInfo, ActivityVO actvo) {
		ActivityRecVO recVO = actvo.getRecVO();
		if(recVO == null) return parentInfo;
		resetActivityRec(parentInfo, actvo, recVO);
		return parentInfo;
	}
	
	public Map<String, Object> resetGuildActivityRec(Map<String, Object> parentInfo, ActivityVO actvo, GuildVO guild) {
		ActivityRecVO recVO = guild.getActivityRecs().get(actvo.getId());
		if(recVO == null) return parentInfo;
		resetActivityRec(parentInfo, actvo, recVO);
		return parentInfo;
	}
	
	public Map<String, Object> resetPlayerActivityRec(CMD cmd, Map<String, Object> parentInfo, ActivityVO actvo, PlayerVO pvo) {
		ActivityRecVO recVO = pvo.getActivityRecs().get(actvo.getId());
		if(recVO == null) return parentInfo;
		boolean isRef = resetActivityRec(parentInfo, actvo, recVO);
		if(isRef) EvtTypeActivity.RefActivity.happen(parentInfo, cmd, pvo, EvtParamType.ACTIVITY.val(actvo));
		return parentInfo;
	}
	
	public boolean resetActivityRec(Map<?, ?> parentInfo, ActivityVO actvo, ActivityRecVO recVO) {
		if(actvo.getRefInterval() <= 0) return false;
		boolean isRef = false;
		List<ActivityCondRecVO> updateConds = GameUtil.createList(); 
		for(Map<Integer, ActivityCondRecVO> condRecVOs : recVO.getCondDatas().values()){
			for(ActivityCondRecVO condRecVO : condRecVOs.values()){
				if(condRecVO.getRefTime() != actvo.getRefTime()){
					condRecVO.setFinish(false);
					condRecVO.setFinishData(0);
					condRecVO.setRefTime(actvo.getRefTime());
					condRecVO.getRecordPlayers().clear();
					condRecVO.getRecordDetails().clear();
					updateConds.add(condRecVO);
					isRef = true;
				}
			}
		}
		activityCondRecDAO.save(updateConds);
		List<ActivityBevRecVO> updateBevs = GameUtil.createList(); 
		for(Map<Integer, ActivityBevRecVO> bevRecVOs : recVO.getBevDatas().values()){
			for(ActivityBevRecVO bevRecVO : bevRecVOs.values()){
				if(bevRecVO.getRefTime() != actvo.getRefTime()){
					bevRecVO.setCanExec(true);
					bevRecVO.setExecNum(0);
					bevRecVO.setRefTime(actvo.getRefTime());
					updateBevs.add(bevRecVO);
					isRef = true;
				}
			}
		}
		activityBevRecDAO.save(updateBevs);
		return isRef;
	}

	@Override
	public Map execBevs(CMD cmd, PlayerVO player, String actId, int group)
			throws LogicException {
		ActivityVO actVO = CacheActivity.getActivityById(actId);
		CodeActivity.ACTIVITY_NOT_EXISTS.throwIfTrue(actVO == null);
		CodeActivity.ACTIVITY_NOT_IN_ACTION.throwIfTrue(!actVO.vaildate());
		long systime = System.currentTimeMillis();
		CodeActivity.ExecBevTimeError.throwIfTrue(actVO.getExecBevStartDate() > 0 && actVO.getExecBevStartDate()>systime);
		Map parent = GameUtil.createSimpleMap();
		if(actVO.getWorkMode() == ActivityConstant.WORK_MODE_FINISH_EXEC)
			ActivityManager.execCheckOneExecOneBev(cmd, parent, actVO, group, player, null, GameUtil.createSimpleMap());
		else
			CodeActivity.ACTIVITY_WORK_MODE_ERROR.throwException();
		return MsgUtil.brmAll(parent, cmd.getCmd());
	}

	@Override
	public Map batchExecBevs(CMD cmd, PlayerVO player, @CParam("actId") String actId, @CParam("group")int group, @CParam("execNum")int execNum) throws LogicException{
		ActivityVO actVO = CacheActivity.getActivityById(actId);
		CodeActivity.ACTIVITY_NOT_EXISTS.throwIfTrue(actVO == null);
		CodeActivity.ACTIVITY_NOT_IN_ACTION.throwIfTrue(!actVO.vaildate());
		Map parent = GameUtil.createSimpleMap();
		if(actVO.getWorkMode() == ActivityConstant.WORK_MODE_FINISH_EXEC) {
			for(int i = 0; i < execNum; i ++){
				ActivityManager.execCheckOneExecOneBev(cmd, parent, actVO, group, player, null, GameUtil.createSimpleMap());
			}
		}else
			CodeActivity.ACTIVITY_WORK_MODE_ERROR.throwException();
		return MsgUtil.brmAll(parent, cmd.getCmd());
	}

	@Override
	public Map getRank(CMD cmd, PlayerVO player, String actId, int group) throws LogicException {
		ActivityVO actVO = CacheActivity.getActivityById(actId);
		CodeActivity.ACTIVITY_NOT_EXISTS.throwIfTrue(actVO == null);
		CodeActivity.ACTIVITY_NOT_IN_ACTION.throwIfTrue(!actVO.vaildate());
		ActivityGroupVO groupVO = actVO.getGroups().get(group);
		CodeActivity.NotFindGroup.throwIfTrue(groupVO == null);
		ActivityGroupDataVO groupDataVO = groupVO.getActivityGroupDataVO();
		Map result = GameUtil.createSimpleMap();
		if(groupDataVO != null){
			MsgFactoryActivity.instance().getActivityRankMsg(result, groupDataVO.getRanks());
		}
		return MsgUtil.brmAll(result, cmd.getCmd());
	}

	@Override
	public EvtType[] getListenTypes() {
		return new EvtType[]{EvtTypePlayer.EnterGame};
	}

	@SuppressWarnings({ "incomplete-switch", "rawtypes" })
	@Override
	public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
		Enum<?> o = event.getType();
        if (o instanceof EvtTypePlayer) {
            switch ((EvtTypePlayer) o) {
                case EnterGame:
                    enterGame(cmd, ((PlayerVO) holder));
                    break;
            }
        }
        return parent;
	}

}
