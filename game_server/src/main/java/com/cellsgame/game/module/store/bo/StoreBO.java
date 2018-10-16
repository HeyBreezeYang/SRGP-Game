package com.cellsgame.game.module.store.bo;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Map;

@AModule(ModuleID.Store)
public interface StoreBO {

    public void init();

    public Map getAllStore() throws LogicException;

    public ICode createStore(Map params) throws LogicException;

    public ICode updateStore(Map params) throws LogicException;

    public ICode deleteStore(int storeId) throws LogicException;

    @Client(Command.Store_Open)
    Map<?, ?> open(PlayerVO player, @CParam("storeId") int storeId, CMD cmd) throws LogicException;

    @Client(Command.Store_Buy)
    Map<?, ?> buy(PlayerVO player, @CParam("storeId") int storeId, @CParam("idx") int index, @CParam("quantity") int quantity, CMD cmd) throws LogicException;


    public void systemRef();
}
