package com.cellsgame.game.module.goods.bo;

import java.util.Map;

import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.goods.csv.ItemGoodsConfig;
import com.cellsgame.game.module.player.vo.PlayerVO;

@AModule(ModuleID.Goods)
public interface GoodsBO extends IBuildData{

    /**
     *
     * @param parent 消息
     * @param prizeMap 如果传入为空,则默认会将消息封装到parent内部
     * @param pvo 玩家对象
     * @param cmd
     * @return
     * @throws LogicException
     */
    Map<?, ?> execGoodsFuncs(Map<?, ?> parent, Map<?, ?> prizeMap, PlayerVO pvo, ItemGoodsConfig funcGoods, CMD cmd) throws LogicException;

    /**
     * 解析奖励物品,item
     *
     * @param parent 消息结构
     * @param pvo    玩家对象
     * @param gid    物品id
     * @param cmd
     * @return 消息数据对象
     */
    Map execGoodsFuncs(Map parent, Map prizeMap, PlayerVO pvo, int gid, CMD cmd) throws LogicException;


    FuncsExecutor addGoodsFuncs(FuncsExecutor<?> executor, PlayerVO pvo, int gid);

    
    @Client(Command.Goods_UseGoods)
    Map playerUseGoods(PlayerVO pvo, @CParam("gid") int gid, @CParam("num") int num, CMD cmd) throws LogicException;
    
    @Client(Command.Goods_PlayerUseGoodsWithParam)
    Map playerUseGoodsWithParam(PlayerVO pvo, @CParam("gid") int gid, @CParam("num") int num, @CParam("chosen")Integer[] chosen,CMD cmd) throws LogicException;
}
