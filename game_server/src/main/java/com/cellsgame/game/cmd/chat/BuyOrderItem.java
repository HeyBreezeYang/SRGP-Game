package com.cellsgame.game.cmd.chat;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.pay.bo.OrderBO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Aly @2016-12-01.
 */
public class BuyOrderItem extends AChatCMD {
    @Override
    public Object getName() {
        return "buyord";
    }

    @Resource
    private OrderBO orderBO;

    @Override
    public Map execute(PlayerVO src, CMD cmd, String command, String[] parm) throws Exception {
        Map<Object, Object> ret = GameUtil.createSimpleMap();
        orderBO.testBuy(src, parm[1]);
        return ret;
    }
}
