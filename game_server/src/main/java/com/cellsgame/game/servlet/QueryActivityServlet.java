package com.cellsgame.game.servlet;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.module.activity.bo.ActivityBO;
import com.cellsgame.game.module.activity.cache.CacheActivity;
import com.cellsgame.game.module.activity.msg.MsgFactoryActivity;
import com.cellsgame.game.module.activity.process.ActivityManager;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import org.eclipse.jetty.continuation.Continuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class QueryActivityServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Collection<ActivityVO> activitys = CacheActivity.getAllActivities();
			List<Map<String, String>> map = GameUtil.createList();
			for (ActivityVO activity : activitys) {
				Map<String, String> actInfo = GameUtil.createSimpleMap();
				actInfo.put("id", activity.getId());
				actInfo.put("clientAtts", activity.getClientAtts());
				actInfo.put("status", String.valueOf(activity.getStatus()));
				actInfo.put("startDate", String.valueOf(activity.getStartDate()));
				actInfo.put("endDate", String.valueOf(activity.getEndDate()));
				map.add( actInfo);
			}
			onComplete(continuation, map);
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
