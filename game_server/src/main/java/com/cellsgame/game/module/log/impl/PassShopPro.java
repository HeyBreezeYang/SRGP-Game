package com.cellsgame.game.module.log.impl;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.log.LogProcess;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * File Description.
 *
 * @author Yang
 */
public class PassShopPro extends LogProcess {

    @Override
    protected Map<String, Object> builderInfo(EvtHolder holder, GameEvent e) {
        Map<String, Object> result = GameUtil.createSimpleMap();
        result.put(TYPE, e.getType());
        result.put(Shop_Id, e.getParam(EvtParamType.SHOP_CID, -1));
        result.put(Shop_TYPE, e.getParam(EvtParamType.SHOP_Type).getType());
        result.put(CID, e.getParam(EvtParamType.GOODS_CID, -1));
        result.put(VALUE, e.getParam(EvtParamType.NUM, 0));
        return result;
    }
}
