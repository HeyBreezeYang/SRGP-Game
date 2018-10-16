package com.cellsgame.game.core.http;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cellsgame.common.buffer.Helper;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.game.core.dispatch.DispatchType;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AsyncServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AsyncServlet.class);
    /**
     *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final Continuation continuation = ContinuationSupport.getContinuation(request);
		continuation.setTimeout(10000);
		if (continuation.isExpired()) {
			onTimeout(continuation);
			return;
	    }
	    continuation.suspend(response); // response may be wrapped.
        getLogicDisruptor().dispatch(() -> {
                try {
                     exec(continuation, request, response);
                }catch (Exception e){
                    e.printStackTrace();
                    onFinal(continuation, 300);
                }
        });
    }

	public abstract void exec(Continuation continuation, HttpServletRequest request, HttpServletResponse response);

    private void onTimeout(Continuation continuation) {
        getSendDisruptor().dispatch(() -> {
            log.warn("onTimeout : " + 999);
            try {
                Map<String, Object> result = GameUtil.createSimpleMap();
                result.put("code", 999);
                sendMsg(continuation, result);
                continuation.complete();
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }
	
	
	public void onComplete(Continuation continuation, Object r) {
        getSendDisruptor().dispatch(() -> {
            log.warn("onComplete : " + r.toString());
            try {
                Map<String, Object> result = GameUtil.createSimpleMap();
                result.put("code", 0);
                result.put("ret", r);
                sendMsg(continuation, result);
                continuation.complete();
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    public void onComplete(Continuation continuation) {
        getSendDisruptor().dispatch(() -> {
            try {
                Map<String, Object> result = GameUtil.createSimpleMap();
                result.put("code", 0);
                sendMsg(continuation, result);
                continuation.complete();
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }


	protected void onFinal(Continuation continuation, int code) {
        getSendDisruptor().dispatch(() -> {
            log.warn("onFinal : " + code);
            try {
                Map<String, Object> result = GameUtil.createSimpleMap();
                result.put("code", code);
                sendMsg(continuation, result);
                continuation.complete();
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    private void sendMsg(Continuation continuation, Map result) throws IOException {
        HttpServletResponse response = (HttpServletResponse) continuation.getServletResponse();
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONUtils.toJSONString(result));
	}

    public abstract DispatchType getLogicDisruptor();

    public DispatchType getSendDisruptor(){
        return DispatchType.GM_RESPONSE;
    }
}
