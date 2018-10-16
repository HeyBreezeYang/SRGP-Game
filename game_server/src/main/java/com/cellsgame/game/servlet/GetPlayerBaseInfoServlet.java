package com.cellsgame.game.servlet;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.LoadPlayerJobFactory;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.msg.CodePlayer;
import com.cellsgame.game.module.player.msg.MsgFactoryPlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.ChainLoadFinisher;
import org.eclipse.jetty.continuation.Continuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class GetPlayerBaseInfoServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String accountId = request.getParameter("accountId");
		String serverId = request.getParameter("serverId");
		PlayerVO player = CachePlayer.getPlayerByAccount(Integer.valueOf(serverId), accountId);
		if(player == null){
			LoadPlayerJobFactory.loadByServerIdAndAccountId(accountId, Integer.valueOf(serverId), new ChainLoadFinisher() {
				@Override
				public void finishLoad(Map<String, Object> data) {
					PlayerBO playerBO = SpringBeanFactory.getBean(PlayerBO.class);
					PlayerVO player = playerBO.load(data, CMD.system.now());
					if(player == null){
						onFinal(continuation, CodePlayer.Player_NotFindPlayer.get());
					}else{
						Map<String, Object> result = MsgFactoryPlayer.instance().getPlayerBaseInfoMessage(player);
						onComplete(continuation, result);
					}
				}
			});
		}else{
			Map<String, Object> result = MsgFactoryPlayer.instance().getPlayerBaseInfoMessage(player);
			onComplete(continuation, result);
		}
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }

}
