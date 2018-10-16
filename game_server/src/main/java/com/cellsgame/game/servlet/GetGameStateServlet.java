package com.cellsgame.game.servlet;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CacheServerState;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.http.AsyncServlet;
import org.eclipse.jetty.continuation.Continuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class GetGameStateServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		Map result = GameUtil.createSimpleMap();
		result.put("state" , CacheServerState.STATE);
		onComplete(continuation, result);
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }
}
