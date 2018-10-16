package com.cellsgame.game.servlet;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cache.CacheServerState;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.chat.ChatBO;
import com.cellsgame.game.module.chat.cons.ChatType;
import com.cellsgame.game.module.chat.cons.NotifyType;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.eclipse.jetty.continuation.Continuation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class GameStateChangeServlet extends AsyncServlet {

	private static final Logger log = LoggerFactory.getLogger(GameStateChangeServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String state = request.getParameter("state");
		CacheServerState.STATE = Integer.valueOf(state);
		if (CacheServerState.isClose()) {
			Collection<PlayerVO> onlinePlayers = CachePlayer.getOnlinePlayers();
			List<PlayerVO> al = new ArrayList<>(onlinePlayers);
			PlayerBO bean = SpringBeanFactory.getBean(PlayerBO.class);
			for (PlayerVO playerVO : al) {
				Push.multiThreadSend(playerVO, MsgUtil.brmAll(Command.General_ServerClose));
				bean.offline(playerVO.getMessageController());
			}
		}
		log.error("change server state : " + state);
		onComplete(continuation);
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }
}
