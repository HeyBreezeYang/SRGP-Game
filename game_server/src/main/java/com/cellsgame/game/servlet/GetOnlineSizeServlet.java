package com.cellsgame.game.servlet;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cache.CacheServerState;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.eclipse.jetty.continuation.Continuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class GetOnlineSizeServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		Map result = GameUtil.createSimpleMap();
		result.put("size" , CachePlayer.getOnlinePlayers().size());
		onComplete(continuation, result);
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }
}
