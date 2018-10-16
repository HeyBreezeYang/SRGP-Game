package com.cellsgame.game.servlet;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.module.rank.bo.RankBO;
import com.cellsgame.game.module.rank.cache.CacheRank;
import com.cellsgame.game.module.rank.cache.RankBoard;
import com.cellsgame.game.module.rank.vo.RankVO;
import org.eclipse.jetty.continuation.Continuation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public class GetRankServlet extends AsyncServlet {

	private static final Logger log = LoggerFactory.getLogger(GetRankServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		String rankType = request.getParameter("rankType");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		try {
			RankBoard<RankVO, Integer> board = CacheRank.getBoard(Integer.parseInt(rankType));
			List<Map<String, Object>> info =  board.getRankInfo(Integer.parseInt(start), Integer.parseInt(end));
			onComplete(continuation, info);
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
