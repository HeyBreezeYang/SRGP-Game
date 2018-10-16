package com.cellsgame.game.module.activity.bo;

import java.util.List;
import java.util.Map;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.activity.vo.ActivityRecVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.DBObj;

@AModule(ModuleID.Activity)
public interface ActivityBO extends StaticEvtListener{
	
	void init();

	void loadGuildActivityData();

	void loadActivityRec(Map<String, ActivityRecVO> recVOMap, List<DBObj> condRecs, List<DBObj> bevRecs);
	
	public void sysUpdateActivityStatus();

	public void deleteActivity(String actid) throws LogicException;
	
	public void createActivity(Map<String, String> activityInfo) throws LogicException;

	@Client(Command.Activity_GetInfo)
	public Map getActivityInfo(CMD cmd, PlayerVO player, @CParam("actId")String activityId) throws LogicException;

	@Client(Command.Activity_ExecBevs)
	public Map execBevs(CMD cmd, PlayerVO player, @CParam("actId") String actId, @CParam("group")int group) throws LogicException;

	@Client(Command.Activity_BatchExecBevs)
	public Map batchExecBevs(CMD cmd, PlayerVO player, @CParam("actId") String actId, @CParam("group")int group, @CParam("execNum")int execNum) throws LogicException;

	@Client(Command.Activity_GetRank)
	public Map getRank(CMD cmd, PlayerVO player, @CParam("actId")String activityId, @CParam("group")int group) throws LogicException;


}
