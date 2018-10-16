package com.cellsgame.game.servlet;

import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.game.cache.CacheIP;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.http.AsyncServlet;
import com.google.common.reflect.TypeToken;
import org.eclipse.jetty.continuation.Continuation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public class UpdateAllowLoginIpServlet extends AsyncServlet {

	private static final Logger log = LoggerFactory.getLogger(UpdateAllowLoginIpServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String ips = request.getParameter("ips");
		List<Map> lst = JSONUtils.fromJson(ips, new TypeToken<List<Map>>(){}.getType());
		log.info("UpdateAllowLoginIp ips : {}", ips);
		try {
			CacheIP.allow.clear();
			for (Map map : lst) {
				String allowIp = map.get("ip").toString();
				CacheIP.allow.add(allowIp);
			}
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
