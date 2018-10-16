package com.cellsgame.game.module.card.bo;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

@AModule(ModuleID.Card)
public interface CardBO {

    public Map<?, ?> openCard(Map<?, ?> parent, PlayerVO playerVO, int cardId) throws LogicException;

    @Client(Command.Card_Prize)
    public Map<String, Object> revCardDayPrize(CMD cmd, PlayerVO playerVO, @CParam("cardId") int cardId) throws LogicException;

}
