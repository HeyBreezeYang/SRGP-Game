package com.cellsgame.game.servlet;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.StringUtil;
import com.cellsgame.game.cons.CodeGeneral;
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


public class LoadPlayerServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String playerIdStr = request.getParameter("playerId");
		String playerName = request.getParameter("playerName");
		Integer playerId = null;
		if(StringUtil.isEmpty(playerIdStr)){
			if(playerName == null) {
				onFinal(continuation, CodeGeneral.General_InvokeParamError.get());
				return;
			}else{
				playerId = CachePlayerBase.getPIDByPname(playerName);
				if(playerId == null){
					onFinal(continuation, CodePlayer.Player_NotFindPlayer.get());
					return;
				}
			}
		}else{
			playerId = Integer.parseInt(playerIdStr);
		}
		PlayerVO player = CachePlayer.getPlayerByPid(playerId);
		if(player == null){
			LoadPlayerJobFactory.loadByPlayerId(playerId, new ChainLoadFinisher() {
				@Override
				public void finishLoad(Map<String, Object> data) {
					PlayerBO playerBO = SpringBeanFactory.getBean(PlayerBO.class);
					PlayerVO player = playerBO.load(data, CMD.system.now());
					if(player == null){
						onFinal(continuation, CodeGeneral.General_ServerException.get());
					}else{
						Map<String, Object> result = MsgFactoryPlayer.instance().getPlayerMessage(player);
						result.put("actId", player.getAccountId());
						onComplete(continuation, result);
					}
				}
			});
		}else{
			Map<String, Object> result = MsgFactoryPlayer.instance().getPlayerMessage(player);
			result.put("actId", player.getAccountId());
			onComplete(continuation, result);
		}
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }

}
