package com.cellsgame.game.module.activity.bev.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.activity.bev.ABevProcess;
import com.cellsgame.game.module.activity.cons.ActivityScopeType;
import com.cellsgame.game.module.activity.csv.ActivityBev;
import com.cellsgame.game.module.activity.msg.MsgFactoryActivity;
import com.cellsgame.game.module.activity.vo.ActivityBevRecVO;
import com.cellsgame.game.module.activity.vo.ActivityGroupDataVO;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.func.AbstractFunc;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.goods.bo.GoodsBO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SearchHeroBevProcess extends ABevProcess {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(SearchHeroBevProcess.class);

	private static String TargetFuncParam = "bigPrzId";
	private static String BoxCids = "boxCids";

	@Resource
	private DepotBO depotBO;

	@Resource
	private GoodsBO goodsBO;

	public void setGoodsBO(GoodsBO goodsBO) {
		this.goodsBO = goodsBO;
	}

	public void setDepotBO(DepotBO depotBO) {
		this.depotBO = depotBO;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int execBev(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act,
					   int group, int index, ActivityBev bev, GameEvent event, Map<?, ?> inputAtts, Map<ActivityScopeType, ActivityBevRecVO> bevRecVOs) {
//		if(player == null) return 0;
//		if(bev.getCost() == null || bev.getCost().isEmpty()){
//			log.error("Activity execBev SearchHeroBevProcess cost is empty ...");
//			return 0;
//		}
//		int tFuncParam = bev.getIntParam(TargetFuncParam);
//		String boxCids = bev.getStringParam(BoxCids, "");
//		Map prizeMap = GameUtil.createSimpleMap();
//        // 奖励
//		FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd, new Function<List<AbstractFunc>, Object>() {
//			@Override
//			public Object apply(List<AbstractFunc> o) {
//				for (AbstractFunc func : o) {
//					if(func.getParam().getParam() == tFuncParam) {
//						FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
//						List<FuncConfig> funcs = GameUtil.createList();
//						funcs.add(new FuncConfig(
//						        SyncFuncType.ChangeCur.getType(),
//                                CurrencyType.ActivityHonor.getType(),
//                                (int)(-depotBO.getCurByType(player, CurrencyType.ActivityHonor))
//                        ));
//						executor.addSyncFunc(funcs);
//						executor.exec(parent, player);
//						return null;
//					}
//				}
//				return null;
//			}
//		});
//		long mny = depotBO.getCurByType(player, CurrencyType.ActivityHonor);
//		int boxCid = getBoxCid(mny, boxCids.split("\\|"));
//		executor.addSyncFunc(bev.getCost());
//		if(bev.getFuncs() != null || !bev.getFuncs().isEmpty())
//			executor.addSyncFunc(bev.getFuncs());
//		goodsBO.addGoodsFuncs(executor, player, boxCid);
//		executor.exec(parent, prizeMap, player);
//        MsgFactoryActivity.instance().createActivityPrizeMsg(parent, prizeMap);
        return 1;
	}

	@Override
	protected void listener(Map<?, ?> parent, CMD cmd, PlayerVO player, ActivityVO act, ActivityBev bev, ActivityGroupDataVO groupDataVO, GameEvent event) {

	}

	private int getBoxCid(long mny, String[] boxCids) {
		int index = mny <= 0 ? 0 : (int) (mny / 25);
		if(index >= boxCids.length) return Integer.parseInt(boxCids[boxCids.length - 1]);
		return Integer.parseInt(boxCids[index]);
	}


	@Override
	public EvtType[] getConcernType() {
		return new EvtType[]{};
	}

}
