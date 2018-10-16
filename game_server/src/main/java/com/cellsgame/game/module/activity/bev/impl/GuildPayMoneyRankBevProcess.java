package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.guild.cons.EvtTypeGuild;
import com.cellsgame.game.module.pay.cache.CacheOrderItem;
import com.cellsgame.game.module.pay.cons.EvtPay;
import com.cellsgame.game.module.pay.csv.OrderItemConfig;

public class GuildPayMoneyRankBevProcess extends GuildRankBevProcess {

    @Override
    protected long getRankValue(ActivityBev bev, GameEvent event) {
        String orderItemId = event.getParam(EvtParamType.ORDER_ITEM_ID);
        OrderItemConfig config = CacheOrderItem.getConfig(orderItemId);
        if(config == null) return 0;
        return config.getOrderMoney();
    }

    public EvtType[] getConcernType() {
        return new EvtType[]{EvtPay.Pay};
    }
}
