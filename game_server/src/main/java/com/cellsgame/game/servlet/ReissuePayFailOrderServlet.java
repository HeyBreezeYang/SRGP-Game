package com.cellsgame.game.servlet;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.module.activity.cache.CacheActivity;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.pay.bo.OrderBO;
import org.eclipse.jetty.continuation.Continuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class ReissuePayFailOrderServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OrderBO orderBO;

	public OrderBO getOrderBO(){
		if(orderBO == null) orderBO = SpringBeanFactory.getBean(OrderBO.class);
		return orderBO;
	}

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String playerId = request.getParameter("playerId");
		String orderNumber = request.getParameter("orderNumber");
		String goodsId = request.getParameter("goodsId");
		getOrderBO().reissuePayFailOrder(playerId, orderNumber, goodsId);
		onComplete(continuation);
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }

}
