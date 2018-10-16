package com.cellsgame.game.servlet;

import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.module.activity.bo.ActivityBO;
import org.eclipse.jetty.continuation.Continuation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class ReleaseActivityServlet extends AsyncServlet {

	private static final Logger log = LoggerFactory.getLogger(ReleaseActivityServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ActivityBO activityBO;

	public ActivityBO getActivityBO(){
		if(activityBO == null) activityBO = SpringBeanFactory.getBean(ActivityBO.class);
		return activityBO;
	}

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String activity = request.getParameter("activity");
		Map<String, String> activityInfo = JSONUtils.fromJson(activity, Map.class);
		log.info("releseActivity activityInfo : {}", activityInfo);
		try {
			getActivityBO().createActivity(activityInfo);
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
