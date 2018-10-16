package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.depot.cons.EventTypeDepot;
import com.cellsgame.game.module.guild.cons.EvtTypeGuild;

public class GuildExpRankBevProcess extends GuildRankBevProcess {

    @Override
    protected long getRankValue(ActivityBev bev, GameEvent event) {
        int num = event.getParam(EvtParamType.NUM, 0);
        return num;
    }

    public EvtType[] getConcernType() {
        return new EvtType[]{EvtTypeGuild.AddExp};
    }
}
