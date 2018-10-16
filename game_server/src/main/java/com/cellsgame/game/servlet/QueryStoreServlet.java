package com.cellsgame.game.servlet;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.dispatch.DispatchType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.http.AsyncServlet;
import com.cellsgame.game.module.activity.cache.CacheActivity;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.store.cache.CacheStore;
import com.cellsgame.game.module.store.vo.StoreItemVO;
import com.cellsgame.game.module.store.vo.StoreVO;
import org.eclipse.jetty.continuation.Continuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class QueryStoreServlet extends AsyncServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void exec(Continuation continuation, HttpServletRequest request,
			HttpServletResponse response) {
		/**
		 *  [
		 *  	{
		 *  	 	id : 唯一ID
		 *  	 	type ： 商城类型
		 *  	 	sTime ： 开始时间
		 *  	 	eTime ： 结束时间
		 *  	 	ref ： 刷新时间
		 *  	 	items ： 商品配置项 [xxx,xxx,xx] 内部存放ID
		 *  	 	itemsNum ： 对应每个商品项的销售数量
		 *  	}
		 *  ]
		 * */
		try {
			Collection<StoreVO> stores = CacheStore.getAll();
			List<Map<String, Object>> map = GameUtil.createList();
			for (StoreVO store : stores) {
				Map<String, Object> actInfo = GameUtil.createSimpleMap();
				actInfo.put("id", store.getId());
				actInfo.put("type", store.getType());
				actInfo.put("sTime", DateUtil.getStringDate(store.getStartTime()));
				actInfo.put("eTime", DateUtil.getStringDate(store.getEndTime()));
				actInfo.put("ref", store.getRefreshHours());
				int[] items = new int[store.getItemVOs().size()];
				int[] itemNums = new int[store.getItemVOs().size()];
				int i = 0;
				for (StoreItemVO itemVO : store.getItemVOs()) {
					items[i] = itemVO.getCid();
					itemNums[i] = itemVO.getSold();
					i++;
				}
				actInfo.put("items", items);
				actInfo.put("itemsNum", itemNums);
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
