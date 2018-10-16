package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.vo.RankDataVO;
import com.cellsgame.game.module.pay.cons.EvtPay;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class GuildPayMemberBevRankProcess extends GuildRankBevProcess {

    @Override
    protected long getRankValue(ActivityBev bev, GameEvent event) {
        return 1;
    }

    @Override
    protected boolean isRecordRank(RankDataVO rankDataVO, PlayerVO player) {
        return !rankDataVO.getRecordPlayer().contains(player.getId());
    }

    @Override
    protected void recordPlayer(RankDataVO rankDataVO, PlayerVO player) {
        rankDataVO.getRecordPlayer().add(player.getId());
    }


    public EvtType[] getConcernType() {
        return new EvtType[]{EvtPay.Pay};
    }
}
