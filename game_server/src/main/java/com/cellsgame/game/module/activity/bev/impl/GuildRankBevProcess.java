package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.common.util.GameUtil;
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
import com.cellsgame.game.module.guild.msg.CodeGuild;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.List;
import java.util.Map;

public abstract class GuildRankBevProcess extends RankBevProcess {

	protected  int getRankItemKey(PlayerVO playerVO) {
		GuildVO guildVO = playerVO.getGuild();
		if(guildVO == null) return 0;
		return guildVO.getId();
	}

	protected String getRankItemName(PlayerVO playerVO) {
		GuildVO guildVO = playerVO.getGuild();
		if(guildVO == null) return null;
		return guildVO.getName();
	}


	@Override
	protected boolean isRecordRank(RankDataVO rankDataVO, PlayerVO player) {
		return true;
	}

	@Override
	protected void recordPlayer(RankDataVO rankDataVO, PlayerVO player) {

	}

	@Override
	protected void recordDetailDate(RankDataVO rankDataVO, PlayerVO player, long changeVal) {
		long oldValue = rankDataVO.getDetailValues().getOrDefault(player.getId(), 0L);
		rankDataVO.getDetailValues().put(player.getId(), oldValue + changeVal);
	}

	private static final String LeaderRankPrizeGroup = "leaderPrizeGroup";
	private static final String MemberRankPrizeGroup = "memberPrizeGroup";


	private int getRankPrizeGroup(GuildVO guildVO, PlayerVO playerVO, ActivityBev bev){
		int leaderPrizeGroup = bev.getIntParam(LeaderRankPrizeGroup);
		int memberPrizeGroup = bev.getIntParam(MemberRankPrizeGroup);
		if(guildVO.getOwner() == playerVO.getId())
			return leaderPrizeGroup;
		else
			return memberPrizeGroup;
	}

	@Override
	protected int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) throws LogicException {
		GuildVO guildVO = player.getGuild();
		CodeGuild.NotJoinGuild.throwIfTrue(guildVO == null);
		ActivityGroupDataVO groupDataVO = getGroupData(act, group);
		int prizeGroup = getRankPrizeGroup(guildVO, player, bev);
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
