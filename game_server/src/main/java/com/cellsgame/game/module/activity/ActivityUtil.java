package com.cellsgame.game.module.activity;

import java.util.Map;
import java.util.Map.Entry;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.csv.ActivityConfig;
import com.cellsgame.game.module.activity.msg.CodeActivity;
import com.cellsgame.game.module.activity.vo.ActivityGroupVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.google.common.reflect.TypeToken;

public class ActivityUtil {

	public static ActivityVO createActivity(ActivityConfig config) throws LogicException {
		ActivityVO act = new ActivityVO();
		act.setId(String.valueOf(config.getId()));
		act.setAutoBev(config.isAutoBev());
		act.setClientAtts(config.getClientAtts());
		act.setEndDate(DateUtil.stringToDate(config.getEndDate()).getTime());
		act.setRefInterval(config.getRefInterval());
		act.setStartDate(DateUtil.stringToDate(config.getStartDate()).getTime());
		act.setWorkMode(config.getWorkMode());
		CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(act.getStartDate() > act.getEndDate());
		for(Entry<Integer, Map<Integer, ActivityCond>> entry : config.getConds().entrySet()){
			int group = entry.getKey();
			Map<Integer, ActivityCond> conds = entry.getValue();
			CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(!config.getBevs().containsKey(group));
			ActivityGroupVO groupVO = new ActivityGroupVO();
			groupVO.setGroup(group);
			groupVO.setConds(conds);
			groupVO.setBevs(config.getBevs().get(group));
			act.getGroups().put(group, groupVO);
		}
		return act;
	}

	private static final String ID = "id";
    private static final String AUTO_BEV = "autoBev";
    private static final String CLIENT_ATTS = "clientAtts";
    private static final String END_DATE = "endDate";
    private static final String REF_INTERVAL = "refInterval";
    private static final String START_DATE = "startDate";
    private static final String EXEC_BEV_START_DATE = "execBevStartDate";
    private static final String BEV_LISTEN_END_DATE = "bevListenEndDate";
    private static final String WORK_MODE = "workMode";
    private static final String CONDS = "conds";
    private static final String BEVS = "bevs";

    public static ActivityVO createActivity(Map<String, String> activityInfo) throws LogicException {
        ActivityVO act = new ActivityVO();
        String id = activityInfo.get(ID);
        CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(id == null);
        act.setId(id);
        String autoBev = activityInfo.get(AUTO_BEV);
        CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(autoBev == null);
        act.setAutoBev(Boolean.valueOf(autoBev));
        String clientAtts = activityInfo.get(CLIENT_ATTS);
        CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(clientAtts == null);
        act.setClientAtts(clientAtts);
        String endDate = activityInfo.get(END_DATE);
        CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(endDate == null);
        act.setEndDate(DateUtil.stringToDate(endDate).getTime());
        String refInterval = activityInfo.get(REF_INTERVAL);
        CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(refInterval == null);
        act.setRefInterval(Integer.valueOf(refInterval));
        String startDate = activityInfo.get(START_DATE);
        CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(startDate == null);
        act.setStartDate(DateUtil.stringToDate(startDate).getTime());
        String execBevStartDate = activityInfo.get(EXEC_BEV_START_DATE);
        if(execBevStartDate == null){
            act.setExecBevStartDate(0);
        }else{
            act.setExecBevStartDate(DateUtil.stringToDate(execBevStartDate).getTime());
            CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(act.getExecBevStartDate() > act.getEndDate());
        }
        String bevListenEndDate = activityInfo.get(BEV_LISTEN_END_DATE);
        if(bevListenEndDate == null){
            act.setBevListenEndDate(0);
        }else{
            act.setBevListenEndDate(DateUtil.stringToDate(bevListenEndDate).getTime());
            CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(act.getBevListenEndDate() > act.getExecBevStartDate());
        }
        String workMode = activityInfo.get(WORK_MODE);
        CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(workMode == null);
        act.setWorkMode(Integer.valueOf(workMode));
        CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(act.getStartDate() > act.getEndDate());
        String condsStr = activityInfo.get(CONDS).replace("\"", "");
        String bevsStr = activityInfo.get(BEVS).replace("\"", "");
        Map<Integer, Map<Integer, ActivityCond>> condsMap = JSONUtils.fromJson(condsStr, new TypeToken<Map<Integer, Map<Integer, ActivityCond>>>(){}.getType());
        Map<Integer, Map<Integer, ActivityBev>> bevsMap = JSONUtils.fromJson(bevsStr, new TypeToken<Map<Integer, Map<Integer, ActivityBev>>>(){}.getType());
        for(Entry<Integer, Map<Integer, ActivityCond>> entry : condsMap.entrySet()){
            int group = entry.getKey();
            Map<Integer, ActivityCond> conds = entry.getValue();
            Map<Integer, ActivityBev> bevs = bevsMap.get(group);
            CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(bevs == null);
            checkActivityCondParams(conds);
            checkActivityBevParams(bevs);
            ActivityGroupVO groupVO = new ActivityGroupVO();
            groupVO.setGroup(group);
            groupVO.setConds(conds);
            groupVO.setBevs(bevs);
            act.getGroups().put(group, groupVO);
        }
        return act;
    }

    private static void checkActivityBevParams(Map<Integer, ActivityBev> bevs) throws LogicException  {
        for(ActivityBev bev : bevs.values()){
            CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(bev.getType() == null);
            CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(bev.getExecLimit() == null);
        }
    }

    private static void checkActivityCondParams(Map<Integer, ActivityCond> conds) throws LogicException  {
        for(ActivityCond cond : conds.values()){
            CodeActivity.ACTIVITY_PARAMS_ERROR.throwIfTrue(cond.getType() == null);
        }
    }

    public static void main(String[] args) {
        String str = "\"{1:{1:{type:passDup,scope:3,value:3,param:{dType:2,dStar:3}}}}\"".replace("\"", "");;

        Map<Integer, Map<Integer, ActivityCond>> condsMap = JSONUtils.fromJson(str, new TypeToken<Map<Integer, Map<Integer, ActivityCond>>>(){}.getType());
        System.out.println("yes");
    }
}
