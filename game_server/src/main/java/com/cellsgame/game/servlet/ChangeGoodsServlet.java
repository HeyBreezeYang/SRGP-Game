package com.cellsgame.game.servlet;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.LoadPlayerJobFactory;
import com.cellsgame.game.module.depot.bo.DepotBO;
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


public class ChangeGoodsServlet extends AsyncServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private DepotBO depotBO;

	private DepotBO getDepotBO(){
		if(depotBO == null){
			depotBO = SpringBeanFactory.getBean(DepotBO.class);
		}
		return depotBO;
	}

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		int playerId = Integer.parseInt(request.getParameter("playerId"));
		int goodsCid = Integer.parseInt(request.getParameter("goodsCid"));
		int changeNum = Integer.parseInt(request.getParameter("changeNum"));

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
						try {
							getDepotBO().changeGoodsNum(GameUtil.createSimpleMap(), player, goodsCid, changeNum, CMD.system.now());
							onComplete(continuation);
						}catch (LogicException e){
							onFinal(continuation, e.getCode());
						}
					}
				}
			});
		}else{
			try {
				Map result = GameUtil.createSimpleMap();
				getDepotBO().changeGoodsNum(result, player, goodsCid, changeNum, CMD.system.now());
				if(player.isOnline()){
					Push.multiThreadSend(player, MsgUtil.brmAll(result, Command.Depot_Ref));
				}
				onComplete(continuation);
			}catch (LogicException e){
				onFinal(continuation, e.getCode());
			}
		}
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }

}
