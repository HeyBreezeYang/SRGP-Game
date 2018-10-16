package com.cellsgame.game.servlet;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.StringUtil;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.mail.bo.MailBO;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.msg.CodePlayer;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.google.common.reflect.TypeToken;
import org.eclipse.jetty.continuation.Continuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class MailServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MailBO mailBO;

	public MailBO getMailBO(){
		if(mailBO == null) mailBO = SpringBeanFactory.getBean(MailBO.class);
		return mailBO;
	}

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String playerIds = request.getParameter("playerId");
		String playerNames = request.getParameter("playerName");
		String title = request.getParameter("title");
		String context = request.getParameter("context");
		String funcsJson = request.getParameter("funcsJson");
		List<FuncConfig> funcs = GameUtil.createList();
		if(StringUtil.isNotEmpty(funcsJson)){
			funcs = JSONUtils.fromJson(funcsJson, new TypeToken<List<FuncConfig>>(){}.getType());
		}
		if("all".equals(playerIds)){
			Collection<PlayerInfoVO> playerInfoVOS =  CachePlayerBase.getAllBaseInfo();
			for (PlayerInfoVO playerInfoVO : playerInfoVOS){
				getMailBO().sendSysMail(playerInfoVO.getPid(), title, context, funcs);
			}
			onComplete(continuation);
		}else{
			Map<String, List<String>> result = GameUtil.createSimpleMap();
			List<String> finish = GameUtil.createList();
			List<String> error = GameUtil.createList();
			result.put("finish", finish);
			result.put("error", error);
			String[] ids = null;
			String[] names = null;
			if(playerIds != null){
				ids = playerIds.split(",");
				if(ids.length < 1) {
					onFinal(continuation, CodeGeneral.General_InvokeParamError.getCode());
					return;
				}
			}else{
				if(playerNames == null)  {
					onFinal(continuation, CodeGeneral.General_InvokeParamError.getCode());
					return;
				}
				names = playerNames.split(",");
				if(names.length < 1) {
					onFinal(continuation, CodeGeneral.General_InvokeParamError.getCode());
					return;
				}
			}
			if(ids != null) {
				for (String playerId : ids) {
					int pid = Integer.parseInt(playerId);
					PlayerInfoVO playerInfoVO = CachePlayerBase.getBaseInfo(pid);
					if(playerInfoVO != null){
						finish.add(playerId);
						getMailBO().sendSysMail(playerInfoVO.getPid(), title, context, funcs);
					}else
						error.add(playerId);
				}
			}

			if(names != null) {
				for (String playerName : names) {
					Integer playerId = CachePlayerBase.getPIDByPname(playerName);
					if(playerId != null) {
						PlayerInfoVO playerInfoVO = CachePlayerBase.getBaseInfo(playerId);
						if (playerInfoVO != null) {
							finish.add(playerName);
							getMailBO().sendSysMail(playerInfoVO.getPid(), title, context, funcs);
						}else{
							error.add(playerName);
						}
					}else
						error.add(playerName);
				}
			}
			onComplete(continuation, result);
		}
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.GAME;
    }

}
