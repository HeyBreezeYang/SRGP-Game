package com.cellsgame.game.servlet;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.module.activity.bo.ActivityBO;
import com.cellsgame.game.module.store.bo.StoreBO;
import org.eclipse.jetty.continuation.Continuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DeleteStoreServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private StoreBO storeBO;

	public StoreBO getStoreBO(){
		if(storeBO == null) storeBO = SpringBeanFactory.getBean(StoreBO.class);
		return storeBO;
	}

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String storeId = request.getParameter("storeId");
		try {
			getStoreBO().deleteStore(Integer.parseInt(storeId));
			onComplete(continuation);
		}catch (LogicException e){
			onFinal(continuation, e.getCode());
		}catch (Exception e){
			onFinal(continuation, CodeGeneral.General_ServerException.get());
		}
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }
}
