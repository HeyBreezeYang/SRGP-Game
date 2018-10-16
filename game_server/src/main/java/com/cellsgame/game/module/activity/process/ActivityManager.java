package com.cellsgame.game.module.activity.process;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.cellsgame.game.Bootstrap;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cond.ACondProcess;
import com.cellsgame.game.module.activity.cons.ActivityConstant;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.csv.ActivityCond;
import com.cellsgame.game.module.activity.msg.MsgFactoryActivity;
import com.cellsgame.game.module.activity.vo.ActivityGroupVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.LoggerFactory;

public class ActivityManager {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(ActivityManager.class);

	static boolean isListener(GameEvent event, ActivityVO act) {
    	return act.getConcernEvents().contains(event.getType());
	}
	
	
	static void condAndBevListener(CMD cmd, Map<String, Object> parent, GameEvent event, Collection<ActivityVO> actVOs, PlayerVO player){
		for(ActivityVO act : actVOs){
    		if(ActivityManager.isListener(event, act)){
    			ActivityManager.condListener(cmd, parent, event, act, player);
    			try{
    				ActivityManager.bevListener(cmd, parent, event, act, player);
    			}catch(LogicException e){
					log.error("exec condAndBevListener error : {}", e);
				};
    		}
    	}
	}
	
	public static void execAutoBevActivity(CMD cmd, Map<String, Object> parent, ActivityVO act){
		for(Entry<Integer, ActivityGroupVO> entry : act.getGroups().entrySet()){
			if(act.isAutoBev() && act.getWorkMode() == ActivityConstant.WORK_MODE_FINISH_EXEC){
				execCheckOneExecOneBev(cmd, parent, act, entry.getKey(), null, null, null);
			}
		}
	}
	
	
	private static void bevListener(CMD cmd, Map<String, Object> parent, GameEvent event, ActivityVO act, PlayerVO player){
		for(Entry<Integer, ActivityGroupVO> entry : act.getGroups().entrySet()){
			Integer group = entry.getKey();
			ActivityGroupVO groupVO = entry.getValue();
			for(Entry<Integer, ActivityBev> e : groupVO.getBevs().entrySet()){
				ActivityBev bev = e.getValue();
				ABevProcess process = bev.getBevType().getProcess();
				if(process.isListener(event) && act.isListener()){
					process.listener(parent, cmd, player, act, bev,group, event);
				}
			}
			if(act.isAutoBev() && act.getWorkMode() == ActivityConstant.WORK_MODE_FINISH_EXEC){
				execCheckOneExecOneBev(cmd, parent, act, entry.getKey(), player, event, null);
			}
		}
		
	}
	
	static void condListener(CMD cmd, Map<String, Object> parent, GameEvent event, Collection<ActivityVO> actVOs, PlayerVO player){
		for(ActivityVO act : actVOs){
    		if(ActivityManager.isListener(event, act)){
    			ActivityManager.condListener(cmd, parent, event, act, player);
    		}
    	}
	}
	
	
	static void condListener(CMD cmd, Map<String, Object> parent, GameEvent event, ActivityVO act, PlayerVO player){
		boolean isChange = false;
		for(Entry<Integer, ActivityGroupVO> entry : act.getGroups().entrySet()){
			Integer group = entry.getKey();
			ActivityGroupVO groupVO = entry.getValue();
			boolean groupCondChange = false;
			for(Entry<Integer, ActivityCond> e : groupVO.getConds().entrySet()){
				int index = e.getKey();
				ActivityCond cond = e.getValue();
				ACondProcess process = cond.getCondType().getProcess();
				if(process.isListener(event)){
					boolean result = process.record(parent, act, cond, group, index, player, event);
					if(result) {
						isChange = result;
						groupCondChange = result;
					}
				}
			}
			if(groupCondChange && act.isAutoBev() && act.getWorkMode() == ActivityConstant.WORK_MODE_FINISH_EXEC){
				execCheckOneExecOneBev(cmd, parent, act, group, player, event, null);
			}
		}
		if(isChange && !act.isAutoBev()) MsgFactoryActivity.instance().activityBevStateUpdateMsg(parent, act, player);
	}
	
	public static void execStopExecAllBev(CMD cmd, ActivityVO act){
		if(act.getWorkMode() != ActivityConstant.WORK_MODE_STOP_EXEC) return;
		try{
			for(Entry<Integer, ActivityGroupVO> entry : act.getGroups().entrySet()){
				Integer group = entry.getKey();
				ActivityGroupVO groupVO = entry.getValue();
				execBev(cmd, null, act, group, groupVO.getBevs(), null, null, null);
			}
		} catch (LogicException e){
			log.error("exec execStopExecAllBev error : {}", e);
		}
	}
	
	public static void execCheckOneExecOneBev(CMD cmd, Map<String, Object> parent, ActivityVO act, int group, PlayerVO player, GameEvent event, Map<?, ?> inputAtts) throws LogicException {
		ActivityGroupVO groupVO = act.getGroups().get(group);
		if(groupVO == null) return;
		if(checkFinishCond(act, groupVO, player)){
			execBev(cmd, parent, act, groupVO.getGroup(), groupVO.getBevs(), player, event, inputAtts);
			MsgFactoryActivity.instance().activityBevUpdateMsg(parent, act, groupVO, player);
		}
	}

	private static void execBev(CMD cmd, Map<?, ?> parent, ActivityVO act, int group,
			Map<Integer, ActivityBev> bevs, PlayerVO player, GameEvent event, Map<?, ?> inputAtts) throws LogicException{
		for(Entry<Integer, ActivityBev> entry : bevs.entrySet()){
			int index = entry.getKey();
			ActivityBev bev = entry.getValue();
			ABevProcess process = bev.getBevType().getProcess();
			process.exec(parent, cmd, act, bev, group, index, player, event, inputAtts);
		}
	}


	private static boolean checkFinishCond(ActivityVO act, ActivityGroupVO groupVO,
											PlayerVO player) {
		boolean finish = true;
		Map<Integer, ActivityCond> conds = groupVO.getConds();
		for(Entry<Integer, ActivityCond> e : conds.entrySet()){
			int index = e.getKey();
			ActivityCond cond = e.getValue();
			ACondProcess process = cond.getCondType().getProcess();
			boolean result = process.check(act, cond, groupVO.getGroup(), index, player);
			if(!result) {
				finish = false;
			}
		}
		return finish;
	}

	private static boolean canExecBev(ActivityVO act, ActivityGroupVO groupVO, PlayerVO player) {
		boolean canExec = true;
		Map<Integer, ActivityBev> bevs = groupVO.getBevs();
		for(Entry<Integer, ActivityBev> e : bevs.entrySet()){
			int index = e.getKey();
			ActivityBev bev = e.getValue();
			ABevProcess process = bev.getBevType().getProcess();
			boolean result = process.canExecBev(act, bev, groupVO.getGroup(), index, player);
			if(!result) {
				canExec = false;
			}
		}
		return canExec;
	}

	public static boolean canExecBev(ActivityVO act, PlayerVO player) {
		for(Entry<Integer, ActivityGroupVO> entry : act.getGroups().entrySet()){
			boolean isFinishCond = checkFinishCond(act, entry.getValue(), player);
			boolean canExecBev = canExecBev(act, entry.getValue(), player);
			if(isFinishCond && canExecBev) return true;
		}
		return false;
	}
}
