package com.cellsgame.game.module.activity.cons;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.activity.msg.CodeActivity;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityCondRecVO;
import com.cellsgame.game.module.activity.vo.ActivityRecVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.guild.msg.CodeGuild;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

public enum ActivityScopeType {
	Global(1) {
		@Override
		public ActivityCondRecVO getCondRecVO(ActivityVO act, int group, int index, PlayerVO player) {
			ActivityRecVO recVO = act.getRecVO();
			return getCondRecVO(recVO, group, index);
		}

		@Override
		public ActivityCondRecVO newCondRecVO(ActivityVO act, int group, int index,
				PlayerVO player) {
			ActivityRecVO recVO = act.getRecVO();
			if(recVO == null) {
				recVO = new ActivityRecVO(act);
				act.setRecVO(recVO);
			}
			ActivityCondRecVO condRecVO = newCondRecVO(act, recVO, group, index);
			condRecVO.setRelId(act.getId());
			return condRecVO;
		}

		@Override
		public ActivityBevRecVO getBevRecVO(ActivityVO act, int group,
				int index, PlayerVO player) {
			ActivityRecVO recVO = act.getRecVO();
			return getBevRecVO(recVO, group, index);
		}

		@Override
		public ActivityBevRecVO newBevRecVO(ActivityVO act, int group,
				int index, PlayerVO player) {
			ActivityRecVO recVO = act.getRecVO();
			if(recVO == null) {
				recVO = new ActivityRecVO(act);
				act.setRecVO(recVO);
			}
			ActivityBevRecVO bevRecVO = newBevRecVO(act, recVO, group, index);
			bevRecVO.setRelId(act.getId());
			return bevRecVO;
		}
	},
	Guild(2) {
		@Override
		public ActivityCondRecVO getCondRecVO(ActivityVO act, int group, int index,
				PlayerVO player) {
			GuildVO guild = player.getGuild();
			if(guild == null)
				return null;
			ActivityRecVO recVO = guild.getActivityRecs().get(act.getId());
			return getCondRecVO(recVO, group, index);
		}

		@Override
		public ActivityCondRecVO newCondRecVO(ActivityVO act, int group, int index,
				PlayerVO player) {
			GuildVO guild = player.getGuild();
			CodeGuild.NotJoinGuild.throwIfTrue(guild == null);
			ActivityRecVO recVO = guild.getActivityRecs().get(act.getId());
			if(recVO == null) {
				recVO = new ActivityRecVO(act);
				guild.getActivityRecs().put(act.getId(), recVO);
			}
			ActivityCondRecVO condRecVO = newCondRecVO(act, recVO, group, index);
			condRecVO.setRelId(String.valueOf(guild.getId()));
			return condRecVO;
		}

		@Override
		public ActivityBevRecVO getBevRecVO(ActivityVO act, int group,
				int index, PlayerVO player) {
			GuildVO guild = player.getGuild();
			if(guild == null)
				return null;
			ActivityRecVO recVO = guild.getActivityRecs().get(act.getId());
			return getBevRecVO(recVO, group, index);
		}

		@Override
		public ActivityBevRecVO newBevRecVO(ActivityVO act, int group,
				int index, PlayerVO player) throws LogicException {
			GuildVO guild = player.getGuild();
			CodeGuild.NotJoinGuild.throwIfTrue(guild == null);
			ActivityRecVO recVO = guild.getActivityRecs().get(act.getId());
			if(recVO == null) {
				recVO = new ActivityRecVO(act);
				guild.getActivityRecs().put(act.getId(), recVO);
			}
			ActivityBevRecVO bevRecVO = newBevRecVO(act, recVO, group, index);
			bevRecVO.setRelId(String.valueOf(guild.getId()));
			return bevRecVO;
		}
	},
	Personal(3) {
		@Override
		public ActivityCondRecVO getCondRecVO(ActivityVO act, int group, int index,
				PlayerVO player) {
			if( player == null ) return null;
			ActivityRecVO recVO = player.getActivityRecs().get(act.getId());
			return getCondRecVO(recVO, group, index);
		}

		@Override
		public ActivityCondRecVO newCondRecVO(ActivityVO act, int group, int index,
				PlayerVO player) {
			CodeActivity.ACTIVITY_BEV_SCOPE_CONF_ERROR.throwIfTrue(player == null);
			ActivityRecVO recVO = player.getActivityRecs().get(act.getId());
			if(recVO == null) {
				recVO = new ActivityRecVO(act);
				player.getActivityRecs().put(act.getId(), recVO);
			}
			ActivityCondRecVO condRecVO = newCondRecVO(act, recVO, group, index);
			condRecVO.setRelId(String.valueOf(player.getId()));
			return condRecVO;
		}

		@Override
		public ActivityBevRecVO getBevRecVO(ActivityVO act, int group,
				int index, PlayerVO player) {
			if( player == null ) return null;
			ActivityRecVO recVO = player.getActivityRecs().get(act.getId());
			return getBevRecVO(recVO, group, index);
		}

		@Override
		public ActivityBevRecVO newBevRecVO(ActivityVO act, int group,
				int index, PlayerVO player) {
			CodeActivity.ACTIVITY_BEV_SCOPE_CONF_ERROR.throwIfTrue(player == null);
			ActivityRecVO recVO = player.getActivityRecs().get(act.getId());
			if(recVO == null) {
				recVO = new ActivityRecVO(act);
				player.getActivityRecs().put(act.getId(), recVO);
			}
			ActivityBevRecVO bevRecVO = newBevRecVO(act, recVO, group, index);
			bevRecVO.setRelId(String.valueOf(player.getId()));
			return bevRecVO;
		}
	},
	;
	ActivityScopeType(int t){
		this.type = t;
	}
	
	private int type;
	
	public abstract ActivityCondRecVO getCondRecVO(ActivityVO act, int group, int index, PlayerVO player);
	
	public abstract ActivityCondRecVO newCondRecVO(ActivityVO act, int group, int index, PlayerVO player) throws LogicException;

	public abstract ActivityBevRecVO getBevRecVO(ActivityVO act, int group, int index, PlayerVO player);
	
	public abstract ActivityBevRecVO newBevRecVO(ActivityVO act, int group, int index, PlayerVO player) throws LogicException;
	
	protected ActivityBevRecVO getBevRecVO(ActivityRecVO recVO, int group, int index){
		if(recVO == null)
			return null;
		Map<Integer, ActivityBevRecVO> recs = recVO.getBevDatas().get(group);
		if(recs == null || recs.size() <= 0)
			return null;
		return recs.get(index);
	}
	
	protected ActivityBevRecVO newBevRecVO(ActivityVO act, ActivityRecVO recVO, int group, int index){
		Map<Integer, ActivityBevRecVO> recs = recVO.getBevDatas().get(group);
		if(recs == null)
			recVO.getBevDatas().put(group, recs = GameUtil.createMap());
		ActivityBevRecVO bevRecVO = recs.get(index);
		if(bevRecVO == null){
			bevRecVO = new ActivityBevRecVO();
			bevRecVO.setActivityId(act.getId());
			bevRecVO.setGroup(group);
			bevRecVO.setIndex(index);
			bevRecVO.setExecNum(0);
			bevRecVO.setRefTime(act.getRefTime());
		}
		recs.put(index, bevRecVO);
		return bevRecVO;
	}
	
	protected ActivityCondRecVO getCondRecVO(ActivityRecVO recVO, int group, int index){
		if(recVO == null)
			return null;
		Map<Integer, ActivityCondRecVO> recs = recVO.getCondDatas().get(group);
		if(recs == null || recs.size() <= 0)
			return null;
		return recs.get(index);
	}
	
	protected ActivityCondRecVO newCondRecVO(ActivityVO act, ActivityRecVO recVO, int group, int index){
		Map<Integer, ActivityCondRecVO> recs = recVO.getCondDatas().get(group);
		if(recs == null)
			recVO.getCondDatas().put(group, recs = GameUtil.createMap());
		ActivityCondRecVO condRecVO = recs.get(index);
		if(condRecVO == null){
			condRecVO = new ActivityCondRecVO();
			condRecVO.setActivityId(act.getId());
			condRecVO.setGroup(group);
			condRecVO.setIndex(index);
			condRecVO.setFinishData(0);
			condRecVO.setRefTime(act.getRefTime());
		}
		recs.put(index, condRecVO);
		return condRecVO;
	}

	public int getType() {
		return type;
	}
	
	
}
