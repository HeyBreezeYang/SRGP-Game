package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;

public class FightForceGuildRankBevProcess extends GuildRankBevProcess {

    @Override
    protected long getRankValue(ActivityBev bev, GameEvent event) {
        long before = event.getParam(EvtParamType.BEFORE, 0L);
        long after = event.getParam(EvtParamType.AFTER, 0L);
        long change = after - before;
        if(change < 0) return 0;
        return change;
    }

    public EvtType[] getConcernType() {
        return new EvtType[]{EvtTypePlayer.FightForce};
    }
}
