package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.csv.ActivityRankPrizeConfig;
import com.cellsgame.game.module.activity.msg.MsgFactoryActivity;
import com.cellsgame.game.module.activity.vo.ActivityGroupDataVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.activity.vo.RankDataVO;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.player.vo.PlayerVO;
import jdk.nashorn.internal.ir.ReturnNode;

import java.util.Map;

public abstract class RankBevProcess extends ABevProcess {

	protected abstract int getRankItemKey(PlayerVO playerVO);

	protected abstract String getRankItemName(PlayerVO playerVO);

	protected abstract long getRankValue(ActivityBev bev, GameEvent event);

	protected abstract void recordDetailDate(RankDataVO rankDataVO, PlayerVO player, long changeVal);

	protected abstract boolean isRecordRank(RankDataVO rankDataVO, PlayerVO player);

	protected abstract void recordPlayer(RankDataVO rankDataVO, PlayerVO player);

	protected int getRank(PlayerVO player, ActivityGroupDataVO groupDataVO){
		if(groupDataVO == null) return 0;
		int itemKey = getRankItemKey(player);
		RankDataVO rankDataVO = groupDataVO.getRankDataVOMap().get(itemKey);
		if(rankDataVO == null) return 0;
		return groupDataVO.getRanks().indexOf(rankDataVO) + 1;
	}

	protected void prize(Map parent, CMD cmd, PlayerVO player, ActivityRankPrizeConfig config){
		Map prizeMap = GameUtil.createSimpleMap();
		// 奖励
		FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd).addSyncFunc(config.getPrizes());
		executor.exec(parent, prizeMap, player);
		MsgFactoryActivity.instance().createActivityPrizeMsg(parent, prizeMap);
	}

	@Override
	protected void listener(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, ActivityBev bev, ActivityGroupDataVO groupDataVO, GameEvent event) {
		long changeVal = getRankValue(bev, event);
		if(changeVal == 0) return;
		int itemKey = getRankItemKey(player);
		if(itemKey <= 0) return;
		RankDataVO rankDataVO = groupDataVO.getRankDataVOMap().get(itemKey);
		if(rankDataVO == null){
			groupDataVO.getRankDataVOMap().put(itemKey, rankDataVO = new RankDataVO());
			rankDataVO.setRankItemKey(itemKey);
			rankDataVO.setRankItemName(getRankItemName(player));
			rankDataVO.setValue(getInitRankValue(player));
			long updateTime = getUpdateTime(player);
			rankDataVO.setUpdateTime(updateTime);
			groupDataVO.getRanks().add(rankDataVO);
		}
		if(!isRecordRank(rankDataVO, player)) return;
		long oldValue = rankDataVO.getValue();
		if(addValue()) {
			rankDataVO.setValue(rankDataVO.getValue() + changeVal);
		}else{
			rankDataVO.setValue(changeVal);
		}
		if(oldValue != rankDataVO.getValue()) {
			long updateTime = getUpdateTime(player);
			rankDataVO.setUpdateTime(updateTime);
			//排序
			groupDataVO.sortRanks();
		}
		recordDetailDate(rankDataVO, player, changeVal);
		recordPlayer(rankDataVO, player);
		activityGroupDataDAO.save(groupDataVO);
	}

	protected long getUpdateTime(PlayerVO player) {
		return System.currentTimeMillis();
	}

	protected long getInitRankValue(PlayerVO player) {
		return 0;
	}

	protected boolean addValue() {
		return true;
	}
}
