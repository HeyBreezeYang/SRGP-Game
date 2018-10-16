package com.cellsgame.game.module.pay.bo.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.conc.disruptor.SingleDisruptor;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.pay.bo.OrderBO;
import com.cellsgame.game.module.pay.cache.CacheOrderItem;
import com.cellsgame.game.module.pay.cons.EvtPay;
import com.cellsgame.game.module.pay.msg.CodePay;
import com.cellsgame.game.module.pay.msg.MsgFactoryPay;
import com.cellsgame.game.module.pay.csv.OrderItemConfig;
import com.cellsgame.game.module.pay.vo.OrderVO;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.sys.msg.CodeSystem;
import com.cellsgame.game.util.UUIDUtil;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;
import com.cellsgame.pay.AOrderHandler;
import com.cellsgame.pay.IOrder;
import com.cellsgame.pay.info.OrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public class OrderBOImpl extends AOrderHandler implements OrderBO {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBOImpl.class);

    @Resource
    private BaseDAO<OrderVO> orderDAO;

    private PlayerBO playerBO;

    @Override
    public int dealPaySucOrder(OrderInfo orderInfo) {
        try {
            //验证商品ID
            OrderItemConfig config = CacheOrderItem.getConfig(orderInfo.getGoodsId());
            if (config == null) {
                LOGGER.debug(">>>>> dealPaySucOrder  not find item ...orderInfo.getGoodsId() : " + orderInfo.getGoodsId());
                return CodePay.NotFincOrderItem.get();
            }
            int playerId = Integer.parseInt(orderInfo.getPid());
            PlayerVO playerVO = CachePlayer.getPlayerByPid(playerId);
            if (playerVO == null) {
                PlayerInfoVO infoVO = CachePlayerBase.getBaseInfo(playerId);
                if (infoVO == null) {
                    LOGGER.debug(">>>>> dealPaySucOrder  not find player ...playerId : " + playerId);
                    return CodePay.NotFindPlayer.get();
                }
                OrderVO orderVO = createOrderVO(orderInfo);
                orderDAO.save(orderVO);
                return CodeSystem.Suc.get();
            } else {
                OrderVO orderVO = createOrderVO(orderInfo);
                sendOrderPrize(playerVO, orderVO, true);
                orderVO.setPrize(true);
                orderDAO.save(orderVO);
                return CodeSystem.Suc.get();
            }
        }catch (Exception e){
            LOGGER.error("Exception : ", e);
            return CodePay.ExecFail.get();
        }
    }

    private OrderVO createOrderVO(OrderInfo orderInfo) {
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderInfo.getOrderNumber());
        orderVO.setPlayerId(Integer.parseInt(orderInfo.getPid()));
        orderVO.setItemId(orderInfo.getGoodsId());
        orderVO.setMny(orderInfo.getPrice());
        orderVO.setPrize(false);
        return orderVO;
    }

    private void sendOrderPrize(PlayerVO player, OrderVO orderVO, boolean sendMsg) {
        OrderItemConfig config = CacheOrderItem.getConfig(orderVO.getItemId());
        int buyTimes = player.getBuyOrderItemTimes().getOrDefault(orderVO.getItemId(), 0);
        boolean firstPay = player.getBuyOrderItemTimes().isEmpty();
        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(CMD.PayPrize.now());
        executor.addSyncFunc(config.getPrize(), EvtParamType.PAY.val(true));
        if(buyTimes < 1){
            if(config.getFristPrize() != null && !config.getFristPrize().isEmpty())
                executor.addSyncFunc(config.getFristPrize());
        }else{
            if(config.getExtraPrize() != null && !config.getExtraPrize().isEmpty())
                executor.addSyncFunc(config.getExtraPrize());
        }
        Map<String, Object> result = GameUtil.createSimpleMap();
        Map<String, Object> prizeMap = GameUtil.createSimpleMap();
        executor.exec(result, prizeMap, player);
        int currTimes = buyTimes + 1;
        player.getBuyOrderItemTimes().put(orderVO.getItemId(), currTimes);
        EvtPay.Pay.happen(result, CMD.system, player,
                EvtParamType.FIRST_PAY.val(firstPay),
                EvtParamType.ORDER_ITEM_ID.val(orderVO.getItemId()),
                EvtParamType.NUM.val(buyTimes)
                );
        playerBO.changeFirstPayPrizeState(result, player);
        LOGGER.debug(">>>>>>>>>>> sendOrderPrize >>>>>>>>>>> player.getName() : " + player.getName());
        if(sendMsg && player.isOnline()){
            result = MsgFactoryPay.instance().getPayPrizeMsg(result, prizeMap);
            result = MsgFactoryPay.instance().getBuyOrderGoodsUpdateMsg(result, orderVO.getItemId(), currTimes);
            Push.multiThreadSend(player, MsgUtil.brmAll(result, Command.Pay_Prize));
            LOGGER.debug(">>>>>>>>>>> sendOrderPrize >>>>>>>>>>> result : " + result);
        }
    }

    @Override
    public List<IOrder> loadAllOrder() {
        List<DBObj> objs = orderDAO.getAll();
        List<IOrder> orderVOS = GameUtil.createList();
        for (DBObj obj : objs) {
            OrderVO orderVO = new OrderVO();
            orderVO.readFromDBObj(obj);
            orderVOS.add(orderVO);
        }
        return orderVOS;
    }

    @Override
    public SingleDisruptor getExecDisruptor() {
        return DispatchType.GAME.getDisruptor();
    }

    @Override
    public SingleDisruptor getSendMsgDisruptor() {
        return DispatchType.HTTP.getDisruptor();
    }

   

    public void setPlayerBO(PlayerBO playerBO) {
        this.playerBO = playerBO;
    }

    @Override
    public void testBuy(PlayerVO playerVO, String orderItemId) {
        OrderItemConfig config = CacheOrderItem.getConfig(orderItemId);
        if(config == null) return;
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId("order" + UUIDUtil.getUUID());
        orderVO.setPlayerId(playerVO.getId());
        orderVO.setItemId(orderItemId);
        orderVO.setMny(0);
        orderVO.setPrize(false);
        orderDAO.save(orderVO);
        sendOrderPrize(playerVO, orderVO, true);
    }

    @Override
    public void reissuePayFailOrder(String playerId, String orderNumber, String goodsId) {
        reissue(orderNumber,playerId,goodsId);
    }

    
    @Override
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
        List<DBObj> dbObjs = (List<DBObj>) data.get(DATA_SIGN_ORDER);
        if (dbObjs == null)
            return;
        Map<String, Integer> buyOrderItemTimes = player.getBuyOrderItemTimes();
        for (DBObj dbObj : dbObjs) {
            OrderVO orderVO = new OrderVO();
            orderVO.readFromDBObj(dbObj);
            if(!orderVO.isPrize()){
                sendOrderPrize(player, orderVO, false);
            }
            Integer times = buyOrderItemTimes.get(orderVO.getItemId());
            if(times == null) times = 0;
            buyOrderItemTimes.put(orderVO.getItemId(), times + 1);
        }
    }
    
	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
		// TODO Auto-generated method stub
		
	}
}
