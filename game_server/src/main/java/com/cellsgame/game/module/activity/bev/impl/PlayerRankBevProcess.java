package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.csv.ActivityRankPrizeConfig;
import com.cellsgame.game.module.activity.msg.CodeActivity;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityGroupDataVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.activity.vo.RankDataVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.List;
import java.util.Map;

public abstract class PlayerRankBevProcess extends RankBevProcess {

	protected  int getRankItemKey(PlayerVO playerVO) {
		return playerVO.getId();
	}

	protected String getRankItemName(PlayerVO playerVO) {
		return playerVO.getName();
	}

	@Override
	protected boolean isRecordRank(RankDataVO rankDataVO, PlayerVO player) {
		return true;
	}

	@Override
	protected void recordPlayer(RankDataVO rankDataVO, PlayerVO player) {

	}

	@Override
	protected  void recordDetailDate(RankDataVO rankDataVO, PlayerVO player, long changeVal) {

	}

	private static final String RankPrizeGroup = "prizeGroup";

	@Override
	protected int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) throws LogicException {
		ActivityGroupDataVO groupDataVO = getGroupData(act, group);
		int prizeGroup = bev.getIntParam(RankPrizeGroup);
		List<ActivityRankPrizeConfig> configs = ActivityRankPrizeConfig.configs.get(prizeGroup);
		CodeActivity.NotFindPrize.throwIfTrue(configs == null);
		int rank = getRank(player, groupDataVO);
		CodeActivity.NotEnterRank.throwIfTrue(rank <= 0);
		for (ActivityRankPrizeConfig config : configs) {
			if(rank <= config.getRank()){
				prize(parent, cmd, player, config);
				break;
			}
		}
		return 1;
	}
}
