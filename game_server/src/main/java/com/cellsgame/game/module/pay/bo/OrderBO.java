package com.cellsgame.game.module.pay.bo;

import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.player.vo.PlayerVO;


@AModule(ModuleID.Pay)
public interface OrderBO extends IBuildData{

    public void testBuy(PlayerVO playerVO, String orderItemId);

    void reissuePayFailOrder(String playerId, String orderNumber, String goodsId);
}
