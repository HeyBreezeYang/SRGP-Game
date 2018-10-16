package com.cellsgame.game.servlet;

import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.module.activity.bo.ActivityBO;
import com.cellsgame.game.module.store.bo.StoreBO;
import org.eclipse.jetty.continuation.Continuation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class CreateStoreServlet extends AsyncServlet {

	private static final Logger log = LoggerFactory.getLogger(CreateStoreServlet.class);

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
		String store = request.getParameter("store");
		Map<String, String> info = JSONUtils.fromJson(store, Map.class);
		log.info("CreateStore info : {}", info);
		try {
			getStoreBO().createStore(info);
			onComplete(continuation);
		}catch (LogicException e){
			onFinal(continuation, e.getCode());
		}catch (Exception e){
			e.printStackTrace();
			onFinal(continuation, CodeGeneral.General_ServerException.get());
		}
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }

}
