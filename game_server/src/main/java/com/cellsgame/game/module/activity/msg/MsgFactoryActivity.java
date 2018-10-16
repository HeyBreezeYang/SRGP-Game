package com.cellsgame.game.module.activity.msg;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cache.CacheActivity;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.process.ActivityManager;
import com.cellsgame.game.module.activity.vo.*;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * Created by yfzhang on 2016/8/22.
 */
@SuppressWarnings({ "rawtypes" })
public class MsgFactoryActivity extends MsgFactory {
	
	private static final String ACTIVITY = "act";
	private static final String ACTIVITY_OP_INFO = "info";
	private static final String ACTIVITY_OP_LIST = "list";
	private static final String ACTIVITY_OP_ADD = "add";
	private static final String ACTIVITY_OP_UPDATE = "upt";
	private static final String ACTIVITY_OP_REMOVE = "rm";
	private static final String ACTIVITY_OP_PRIZE = "prz";
	private static final String ACTIVITY_OP_RANK = "rank";
	private static final String ACTIVITY_OP_RANK_DETAIL_VALUE = "vals";


	private static final String ACTIVITY_ID = "id";
	private static final String ACTIVITY_STATUS = "stus";
	private static final String ACTIVITY_STARTDATE = "sdate";
	private static final String ACTIVITY_BEV_EXEC_START_DATE = "exsdate";
	private static final String ACTIVITY_BEV_LISTEN_END_DATE = "lstedate";
	private static final String ACTIVITY_ENDDATE = "edate";
	private static final String ACTIVITY_WORKMODE = "work";
	private static final String ACTIVITY_REFINTERVA = "ref";
	private static final String ACTIVITY_AUTOBEV = "auto";
	private static final String ACTIVITY_CLIENTATTS = "catts";
	private static final String ACTIVITY_GROUPS = "grp";
	private static final String ACTIVITY_GROUP_CONDS = "conds";
	private static final String ACTIVITY_GROUP_COND_TYPE = "ctype";
	private static final String ACTIVITY_GROUP_COND_SCOPE = "cscope";
	private static final String ACTIVITY_GROUP_COND_PARAMS = "cpms";
	private static final String ACTIVITY_GROUP_COND_VALUE = "cval";
	private static final String ACTIVITY_GROUP_COND_FINISH = "cfin";
	private static final String ACTIVITY_GROUP_COND_DETAIL = "cdtl";
	private static final String ACTIVITY_GROUP_BEVS = "bevs";
	private static final String ACTIVITY_GROUP_BEV_TYPE = "btype";
	private static final String ACTIVITY_GROUP_BEV_EXECLIMIT = "blimit";
	private static final String ACTIVITY_GROUP_BEV_FUNCS = "bfuncs";
	private static final String ACTIVITY_GROUP_BEV_COSTS = "bcosts";
	private static final String ACTIVITY_GROUP_BEV_PARAMS = "bpms";
	private static final String ACTIVITY_GROUP_BEV_EXEC = "bexec";
	private static final String ACTIVITY_EXEC_BEV = "exec";

    
    private static final MsgFactoryActivity instance  = new MsgFactoryActivity();
    
    public static MsgFactoryActivity instance(){
    	return instance;
    }

	@Override
	public String getModulePrefix() {
		return ACTIVITY;
	}

	/**
	 * 创建活动奖励消息
	 * */
	public Map createActivityPrizeMsg(Map<?, ?> parent, Map<?, ?> prizeInfo) {
		Map msdInfoOp = gocOpMap(parent);
		addPrizeMsg(msdInfoOp, prizeInfo);
		return parent;
	}

	/**
	 * 创建活动状态变更消息
	 * */
	public Map<String, Object> createActivityStatusChangeMsg(
			Map<String, Object> parent, String id, int status) {
		Map<String, Object> msdInfoOp = gocOpMap(parent);
		List<Map<String, Object>> list = gocLstIn(msdInfoOp, ACTIVITY_OP_UPDATE);
		Map<String, Object> info = GameUtil.createSimpleMap();
		info.put(ACTIVITY_ID, id);
		info.put(ACTIVITY_STATUS, status);
		list.add(info);
		return parent;
	}

	/**
	 * 创建删除活动消息
	 * */
	public Map<String, Object> createRemoveActivityMsg(Map<String, Object> parent, String id) {
		Map<String, Object> msdInfoOp = gocOpMap(parent);
		List<Map<String, Object>> list = gocLstIn(msdInfoOp, ACTIVITY_OP_REMOVE);
		Map<String, Object> info = GameUtil.createSimpleMap();
		info.put(ACTIVITY_ID, id);
		list.add(info);
		return parent;
	}

	public Map<String, Object> createNewActivityMsg(Map<String, Object> parent, ActivityVO actvo, PlayerVO player) {
		Map<String, Object> msdInfoOp = gocOpMap(parent);
		List<Map<String, Object>> list = gocLstIn(msdInfoOp, ACTIVITY_OP_ADD);
		Map actInfo = createActivityMsg(actvo, player);
		actInfo.put(ACTIVITY_STATUS, actvo.getStatus());
		actInfo.put(ACTIVITY_EXEC_BEV, ActivityManager.canExecBev(actvo, player));
		list.add(actInfo);
		return parent;
	}

	private static final String GUILD_RIGHT = "gldRgt";

	private Map<String, Object> createActivityMsg(ActivityVO actvo, PlayerVO player) {
		Map<String, Object> result = GameUtil.createSimpleMap();
		result.put(ACTIVITY_ID, actvo.getId());
		result.put(ACTIVITY_STARTDATE, String.valueOf(actvo.getStartDate()));
		result.put(ACTIVITY_ENDDATE, String.valueOf(actvo.getEndDate()));
		if(actvo.getBevListenEndDate() > 0)
			result.put(ACTIVITY_BEV_LISTEN_END_DATE, String.valueOf(actvo.getBevListenEndDate()));
		if(actvo.getExecBevStartDate() > 0)
			result.put(ACTIVITY_BEV_EXEC_START_DATE, String.valueOf(actvo.getExecBevStartDate()));
		result.put(ACTIVITY_WORKMODE, actvo.getWorkMode());
		result.put(ACTIVITY_REFINTERVA, actvo.getRefInterval());
		result.put(ACTIVITY_AUTOBEV, actvo.isAutoBev());
		result.put(ACTIVITY_CLIENTATTS, actvo.getClientAtts());
		Map<Integer, Map> groupsInfo = GameUtil.createSimpleMap();
		result.put(ACTIVITY_GROUPS, groupsInfo);
		for(Map.Entry<Integer, ActivityGroupVO> entry : actvo.getGroups().entrySet()){
			int group = entry.getKey();
			ActivityGroupVO groupVO = entry.getValue();
			Map<String, Map> groupInfo = GameUtil.createSimpleMap();
			Map<Integer, Map> condsInfo = GameUtil.createSimpleMap();
			for (Map.Entry<Integer, ActivityCond> e : groupVO.getConds().entrySet()) {
				int index = e.getKey();
				ActivityCond cond = e.getValue();
				Map<String, Object> condMap = GameUtil.createSimpleMap();
				condMap.put(ACTIVITY_GROUP_COND_TYPE, cond.getType());
				condMap.put(ACTIVITY_GROUP_COND_SCOPE, cond.getScope());
				condMap.put(ACTIVITY_GROUP_COND_PARAMS, cond.getParam());
				condMap.put(ACTIVITY_GROUP_COND_VALUE, cond.getValue());
				ActivityScopeType scopeType = cond.getScopeType();
				ActivityCondRecVO condRecVO = scopeType.getCondRecVO(actvo, group, index, player);
				condMap.put(ACTIVITY_GROUP_COND_FINISH, condRecVO == null ? 0 : condRecVO.getFinishData());
				List<Map> lst = gocLstIn(condMap, ACTIVITY_GROUP_COND_DETAIL);
				if(condRecVO != null){
					for (Map.Entry<Integer, Long> details : condRecVO.getRecordDetails().entrySet()) {
						int playerId = details.getKey();
						long val = details.getValue();
						Map m = GameUtil.createSimpleMap();
						PlayerInfoVO infoVO = CachePlayerBase.getBaseInfo(playerId);
						if(infoVO != null) {
							m.put(ID, playerId);
							m.put(NAME, infoVO.getName());
						}
						m.put(VALUE, val);
						GuildVO guildVO = CacheGuild.getGuildByID(infoVO.getGuildId());
						if(guildVO != null){
							m.put(GUILD_RIGHT, guildVO.getMemberRights().getOrDefault(playerId, 0));
						}
						lst.add(m);
					}
				}
				condsInfo.put(index, condMap);
			}
			groupInfo.put(ACTIVITY_GROUP_CONDS, condsInfo);

			Map<Integer, Map> bevsInfo = GameUtil.createSimpleMap();
			for (Map.Entry<Integer, ActivityBev> e : groupVO.getBevs().entrySet()) {
				int index = e.getKey();
				ActivityBev bev = e.getValue();
				Map<String, Object> bevMap = GameUtil.createSimpleMap();
				bevMap.put(ACTIVITY_GROUP_BEV_TYPE, bev.getType());
				bevMap.put(ACTIVITY_GROUP_BEV_EXECLIMIT, bev.getExecLimit());
				bevMap.put(ACTIVITY_GROUP_BEV_PARAMS, bev.getParam());
				List<Map<?, ?>> funcsInfo = gocLstIn(bevMap, ACTIVITY_GROUP_BEV_FUNCS);
				for(FuncConfig func : bev.getFuncs()){
					Map<String, Object> funcMap = GameUtil.createSimpleMap();
					funcMap.put(TYPE, func.getType());
					funcMap.put(PARAM, func.getParam());
					funcMap.put(NUM, func.getValue());
					funcsInfo.add(funcMap);
				}
				List<Map<?, ?>> costsInfo = gocLstIn(bevMap, ACTIVITY_GROUP_BEV_COSTS);
				for(FuncConfig func : bev.getCost()){
					Map<String, Object> funcMap = GameUtil.createSimpleMap();
					funcMap.put(TYPE, func.getType());
					funcMap.put(PARAM, func.getParam());
					funcMap.put(NUM, func.getValue());
					costsInfo.add(funcMap);
				}
				ABevProcess process = bev.getBevType().getProcess();
				Map<ActivityScopeType, ActivityBevRecVO> bevRecs = process.getBevRecVOs(actvo, bev, group, index, player);
				Map<Integer, Object> execInfo = GameUtil.createSimpleMap();
				if(bevRecs != null) {
					for (Map.Entry<ActivityScopeType, ActivityBevRecVO> execEntry : bevRecs.entrySet()) {
						execInfo.put(execEntry.getKey().getType(), execEntry.getValue().getExecNum());
					}
				}
				bevMap.put(ACTIVITY_GROUP_BEV_EXEC, execInfo);
				bevsInfo.put(index, bevMap);
			}
			groupInfo.put(ACTIVITY_GROUP_BEVS, bevsInfo);
			groupsInfo.put(group, groupInfo);
		}
		return result;
	}


	public Map<String, Object> getActivityListMsg(Map<String, Object> parent, PlayerVO player){
		Map<String, Object> msdInfoOp = gocInfoMap(parent);
		List<Map<String, Object>> list = gocLstIn(msdInfoOp, ACTIVITY_OP_LIST);
		Collection<ActivityVO> activitys = CacheActivity.getAllActivities();
		for (ActivityVO activity : activitys) {
			Map<String,Object> actInfo = GameUtil.createSimpleMap();
			actInfo.put(ACTIVITY_ID, activity.getId());
			actInfo.put(ACTIVITY_CLIENTATTS, activity.getClientAtts());
			actInfo.put(ACTIVITY_STATUS, activity.getStatus());
			actInfo.put(ACTIVITY_EXEC_BEV, ActivityManager.canExecBev(activity, player));
			actInfo.put(ACTIVITY_STARTDATE, String.valueOf(activity.getStartDate()));
			actInfo.put(ACTIVITY_ENDDATE, String.valueOf(activity.getEndDate()));
			if(activity.getBevListenEndDate() > 0)
				actInfo.put(ACTIVITY_BEV_LISTEN_END_DATE, String.valueOf(activity.getBevListenEndDate()));
			if(activity.getExecBevStartDate() > 0)
				actInfo.put(ACTIVITY_BEV_EXEC_START_DATE, String.valueOf(activity.getExecBevStartDate()));
			list.add(actInfo);
		}
		return parent;
	}

	public Map<String, Object> activityBevStateUpdateMsg(Map<String, Object> parent, ActivityVO activity, PlayerVO player){
		Map<String, Object> msdInfoOp = gocOpMap(parent);
		List<Map<String, Object>> list = gocLstIn(msdInfoOp, ACTIVITY_OP_UPDATE);
		Map<String, Object> info = GameUtil.createSimpleMap();
		info.put(ACTIVITY_ID, activity.getId());
		info.put(ACTIVITY_EXEC_BEV, ActivityManager.canExecBev(activity, player));
		list.add(info);
		return parent;
	}

	public Map<String, Object> activityBevUpdateMsg(Map<String, Object> parent, ActivityVO activity, ActivityGroupVO groupVO, PlayerVO player){
		Map<String, Object> msdInfoOp = gocOpMap(parent);
		List<Map<String, Object>> list = gocLstIn(msdInfoOp, ACTIVITY_OP_UPDATE);
		Map<String, Object> info = GameUtil.createSimpleMap();
		info.put(ACTIVITY_ID, activity.getId());
		info.put(ACTIVITY_EXEC_BEV, ActivityManager.canExecBev(activity, player));
		Map<Integer, Map> bevsInfo = GameUtil.createSimpleMap();
		for (Map.Entry<Integer, ActivityBev> e : groupVO.getBevs().entrySet()) {
			int index = e.getKey();
			ActivityBev bev = e.getValue();
			Map<String, Object> bevMap = GameUtil.createSimpleMap();
			ABevProcess process = bev.getBevType().getProcess();
			Map<ActivityScopeType, ActivityBevRecVO> bevRecs = process.getBevRecVOs(activity, bev, groupVO.getGroup(), index, player);
			Map<Integer, Object> execInfo = GameUtil.createSimpleMap();
			if(bevRecs != null) {
				for (Map.Entry<ActivityScopeType, ActivityBevRecVO> execEntry : bevRecs.entrySet()) {
					execInfo.put(execEntry.getKey().getType(), execEntry.getValue().getExecNum());
				}
			}
			bevMap.put(ACTIVITY_GROUP_BEV_EXEC, execInfo);
			bevsInfo.put(index, bevMap);
		}

		Map<Integer, Map> groupsInfo = GameUtil.createSimpleMap();
		info.put(ACTIVITY_GROUPS, groupsInfo);
		Map<String, Map> groupInfo = GameUtil.createSimpleMap();
		groupsInfo.put(groupVO.getGroup(), groupInfo);
		groupInfo.put(ACTIVITY_GROUP_BEVS, bevsInfo);
		list.add(info);
		return parent;
	}

	public Map<String, Object> getActivityInfoMsg(Map<String, Object> parent, ActivityVO actvo, PlayerVO player){
		Map<String, Object> msdInfoOp = gocInfoMap(parent);
		msdInfoOp.put(ACTIVITY_OP_INFO, createActivityMsg(actvo, player));
		return parent;
	}

	public void getActivityRankMsg(Map parent, List<RankDataVO> ranks) {
		Map<String, Object> msdInfoOp = gocInfoMap(parent);
		List<Map<String, Object>> list = gocLstIn(msdInfoOp, ACTIVITY_OP_RANK);
		for (RankDataVO rank : ranks) {
			Map<String, Object> info = GameUtil.createSimpleMap();
			info.put(ID, rank.getRankItemKey());
			info.put(NAME, rank.getRankItemName());
			info.put(VALUE, rank.getValue());
			List<Map> lst = gocLstIn(info, ACTIVITY_OP_RANK_DETAIL_VALUE);
			for (Map.Entry<Integer, Long> entry : rank.getDetailValues().entrySet()) {
				int playerId = entry.getKey();
				long val = entry.getValue();
				Map m = GameUtil.createSimpleMap();
				PlayerInfoVO infoVO = CachePlayerBase.getBaseInfo(playerId);
				if(infoVO != null) {
					m.put(ID, playerId);
					m.put(NAME, infoVO.getName());
				}
				m.put(VALUE, val);
				lst.add(m);
			}
			list.add(info);
		}

	}
}
