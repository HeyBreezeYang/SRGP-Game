package com.cellsgame.game.module.shop.bo;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.shop.GroupShopItem;
import com.cellsgame.game.module.shop.vo.ShopVO;

/**
 * 商店相关业务逻辑.
 *
 * @author Yang
 */
@AModule(ModuleID.Shop)
public interface ShopBO extends IBuildData, StaticEvtListener{
    Map<Integer, GroupShopItem> ShopItemGroup = GameUtil.createSimpleMap();

    /**
     * 打开商店
     *
     * @param player 玩家数据
     * @param shopId 商店ID
     * @return 打开结棍
     * @throws LogicException
     */
    @Client(Command.Shop_Open)
    Map<?, ?> open(PlayerVO player, @CParam("shop") int shopId) throws LogicException;

    /**
     * 购买商店商品
     *
     * @param player   玩家数据
     * @param shopId   商店ID
     * @param index    商店商品下标
     * @param quantity 购买数量
     * @param cmd
     * @return 购买结果
     * @throws LogicException
     */
    @Client(Command.Shop_Buy)
    Map<?, ?> buy(PlayerVO player, @CParam("shop") int shopId, @CParam("itemIndex") int index, @CParam("quantity") int quantity, CMD cmd) throws LogicException;


    /**
     * 购买特殊商店商品
     *
     * @param player   玩家数据
     * @param itemId   商店商品ID
     * @param quantity 购买数量
     * @param cmd
     * @return 购买结果
     * @throws LogicException
     */
    @Client(Command.Shop_Buy_Special)
    Map<?, ?> buySpecial(PlayerVO player, @CParam("itemId") int itemId, @CParam("quantity") int quantity, CMD cmd) throws LogicException;

    /**
     * 手动刷新商店
     *
     * @param player 玩家数据
     * @param shopId 商店ID
     * @param cmd
     * @return 刷新结果
     * @throws LogicException
     */
    @Client(Command.Shop_Manual_Refresh)
    Map<?, ?> manualRefresh(PlayerVO player, @CParam("shop") int shopId, CMD cmd) throws LogicException;

    /**
     * 系统刷新商店。
     * <p>
     * 需要检测是否达到刷新条件。
     * <p>
     * <span style="color:red;">注意:</span>
     * <p>
     * 如果逻辑处理线程是多线程, 系统商店的刷新可能会造成并发异常。
     *
     * @param playerVO 玩家数据
     * @param shopVO   商店数据
     */
    boolean systemRefresh(PlayerVO playerVO, ShopVO shopVO) throws LogicException;

}
