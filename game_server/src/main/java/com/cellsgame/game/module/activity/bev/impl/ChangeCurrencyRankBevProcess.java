package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.depot.cons.EventTypeDepot;

public class ChangeCurrencyRankBevProcess extends PlayerRankBevProcess {

    private static String Op = "op";
    private static String CurrencyType = "cType";

    private static final int OP_Get = 1;
    private static final int OP_Use = 2;

    @Override
    protected long getRankValue(ActivityBev bev, GameEvent event) {
        int tCurrencyType = bev.getIntParam(CurrencyType);
        int op = bev.getIntParam(Op);
        int cType = event.getParam(EvtParamType.CURRENCY_TYPE, 0);
        if(tCurrencyType != cType) return 0;
        long before = event.getParam(EvtParamType.BEFORE, 0L);
        long after = event.getParam(EvtParamType.AFTER, 0L);
        long change = after - before;
        if(op != OP_Get && op != OP_Use) return 0;
        if(op == OP_Get && change <= 0) return 0;
        if(op == OP_Use && change >= 0) return 0;
        return Math.abs(change);
    }

    public EvtType[] getConcernType() {
        return new EvtType[]{EventTypeDepot.Currency};
    }
}
