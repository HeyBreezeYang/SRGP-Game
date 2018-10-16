package com.cellsgame.game.servlet;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.LoadPlayerJobFactory;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.cons.PlayerState;
import com.cellsgame.game.module.player.msg.CodePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.ChainLoadFinisher;
import org.eclipse.jetty.continuation.Continuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class ChangePlayerStateServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PlayerBO playerBO;

	private PlayerBO getPlayerBO(){
		if(playerBO == null){
			playerBO = SpringBeanFactory.getBean(PlayerBO.class);
		}
		return playerBO;
	}

	private boolean reset;

	public ChangePlayerStateServlet(boolean reset){
		this.reset = reset;
	}

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String playerName = request.getParameter("playerName");
		String stateStr = request.getParameter("state");
		final int state;
		try {
			if (stateStr == null) {
				onFinal(continuation, CodeGeneral.General_InvokeParamError.get());
				return;
			}
			state = Integer.parseInt(stateStr);
		}catch (Exception e){
			onFinal(continuation, CodeGeneral.General_ServerException.get());
			return;
		}
		Integer playerId = CachePlayerBase.getPIDByPname(playerName);
		if(playerId == null){
			onFinal(continuation, CodePlayer.Player_NotFindPlayer.get());
			return;
		}
		PlayerVO player = CachePlayer.getPlayerByPid(playerId);
		if(player == null){
			LoadPlayerJobFactory.loadByPlayerId(playerId, new ChainLoadFinisher() {
				@Override
				public void finishLoad(Map<String, Object> data) {
					PlayerBO playerBO = SpringBeanFactory.getBean(PlayerBO.class);
					PlayerVO player = playerBO.load(data, CMD.system.now());
					if(player == null){
						onFinal(continuation, CodePlayer.Player_NotFindPlayer.get());
					}else{
						changePlayerState(continuation, player, state);
					}
				}
			});
		}else{
			changePlayerState(continuation, player, state);
		}
	}

	private void changePlayerState(Continuation continuation, PlayerVO player, int state){
		if(reset){
			resetPlayerState(continuation, player, state);
		}else{
			setPlayerState(continuation, player, state);
		}
	}

	private void setPlayerState(Continuation continuation, PlayerVO playerVO, int state){
		PlayerState playerState = Enums.get(PlayerState.class, state);
		if(playerState == null){
			onFinal(continuation, CodePlayer.Player_NotFindConfig.get());
			return;
		}
		playerState.setState(playerVO);
		getPlayerBO().save(playerVO);
		if (playerVO.isOnline() && PlayerState.FROZEN.check(playerVO)) {
			getPlayerBO().offline(playerVO.getMessageController());
		}
		onComplete(continuation);
	}

	private void resetPlayerState(Continuation continuation, PlayerVO playerVO, int state){
		PlayerState playerState = Enums.get(PlayerState.class, state);
		if(playerState == null){
			onFinal(continuation, CodePlayer.Player_NotFindConfig.get());
			return;
		}
		playerState.resetState(playerVO);
		getPlayerBO().save(playerVO);
		onComplete(continuation);
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }

}
