package com.cellsgame.game.servlet;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.module.chat.ChatBO;
import com.cellsgame.game.module.chat.cons.ChatType;
import com.cellsgame.game.module.chat.cons.NotifyType;
import org.eclipse.jetty.continuation.Continuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class NotifyServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ChatBO chatBO;

	public ChatBO getChatBO(){
		if(chatBO == null) chatBO = SpringBeanFactory.getBean(ChatBO.class);
		return chatBO;
	}

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String message = request.getParameter("msg");
		getChatBO().notifyMsg(null, ChatType.Sys, NotifyType.SYSTEM.getType(), new String[]{message});
		onComplete(continuation);
	}

	@Override
    public DispatchType getLogicDisruptor() {
        return DispatchType.NOTIFY;
    }
}
