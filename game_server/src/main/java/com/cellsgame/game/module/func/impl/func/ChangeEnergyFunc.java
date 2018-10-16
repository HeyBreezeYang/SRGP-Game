//package com.cellsgame.game.module.func.impl.func;
//
//import java.util.Collection;
//import java.util.Map;
//import javax.annotation.Resource;
//
//import com.cellsgame.game.cache.CacheDisDataPlayer;
//import com.cellsgame.game.constant.Code;
//import com.cellsgame.game.constant.ToClientResultKey;
//import com.cellsgame.game.core.excption.LogicException;
//import com.cellsgame.game.module.MessageFactory;
//import com.cellsgame.game.module.func.CheckRec;
//import com.cellsgame.game.module.func.FuncParam;
//import com.cellsgame.game.module.func.SyncFunc;
//import com.cellsgame.game.module.func.constant.PrizeConstant;
//import com.cellsgame.game.module.player.PlayerMessageBuilder;
//import com.cellsgame.game.module.player.vo.PlayerVO;
//import com.cellsgame.orm.BaseDAO;
//
///**
// * @author Aly @2016-12-14.
// */
//public class ChangeEnergyFunc extends SyncFunc {
//    @Resource
//    private BaseDAO<PlayerVO> playerDAO;
//
//    @Override
//    protected Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) throws LogicException {
//        return null;
//    }
//
//    @Override
//    protected Object exec(Map<?, ?> parent, PlayerVO player, FuncParam param) throws LogicException {
//        //
//        Code.Player_Energy_Minus.throwIfTrue(param.getValue() < 0 && player.getEnergy() + param.getValue() < 0);
//        // 活跃度
//        player.setEnergy(Math.min(player.getEnergy() + param.getValue(), CacheDisDataPlayer.MAX_ENERGY.getData()[0]));
//        ///
//        PlayerMessageBuilder.getEnergyMessage(parent, player);
//        //
//        playerDAO.save(player);
//        //
//        return parent;
//    }
//
//    @Override
//    protected void dealWithPrizeMap(Map prizeMap, PlayerVO player, FuncParam param) {
//    	PrizeConstant.addPlayerEnergy(prizeMap, param.getValue());
//    }
//}